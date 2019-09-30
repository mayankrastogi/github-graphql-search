package com.mayankrastogi.cs474.hw1

import com.mayankrastogi.cs474.hw1.github.GitHubGraphQLQueries
import com.mayankrastogi.cs474.hw1.github.enums.SearchType
import com.mayankrastogi.cs474.hw1.github.enums.SearchType.SearchType
import com.mayankrastogi.cs474.hw1.github.interfaces.SearchResult
import com.mayankrastogi.cs474.hw1.graphql.Query
import com.mayankrastogi.cs474.hw1.utils.{GitHubSearchAppUtils, Settings}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.io.StdIn

object GitHubSearchApp extends App with LazyLogging {

  // Load the settings from the config file
  val settings = new Settings(ConfigFactory.load())
  logger.trace(s"Loaded settings: $settings")

  // Extract the token from the command-line arguments
  val token = extractTokenFromArgs
  logger.debug(s"Using personal token: $token")

  // Initialize the GraphQL client
  val client = GitHubSearchAppUtils.createGitHubGraphQLClient(settings.apiEndpoint, token)
  logger.trace(s"Using client: $client")

  printWelcomeMessage()

  val searchChoice = askWhatToSearch
  val searchTerm = askForSearchTerm

  // Track what user wants to search
  val searchType = searchChoice match {
    case 1 => SearchType.User
    case 2 => SearchType.Repository
  }

  // Get the GraphQL query depending on what user wants to search
  val query = searchType match {
    case SearchType.User => GitHubGraphQLQueries.searchUsers(searchTerm, settings.numberOfResultsInPage)
    case SearchType.Repository => GitHubGraphQLQueries.searchRepositories(searchTerm, settings.numberOfResultsInPage)
  }

  // Query GitHub server, display and page the results
  searchAndPaginate(query, searchType)

  /**
   * Extracts the personal token from the first command-line argument.
   *
   * Exits the application with an error message if a token was not specified as the first argument.
   *
   * @return The personal token.
   */
  def extractTokenFromArgs: String = {
    if (args.nonEmpty) {
      args.head
    }
    else {
      println("Please specify your GitHub Personal Token as the first argument.")
      sys.exit(-1)
    }
  }

  /**
   * Prints the welcome message for the application.
   */
  def printWelcomeMessage(): Unit = {
    println(
      """
        |===============================================================================================================
        |                                         GitHub Search Application
        |===============================================================================================================
        |This application allows you to search for users or repositories on GitHub.
        |
        |First choose what you want to search (Users or Repositories) and then specify the search term. You will be able
        |to page through the results by entering 'N' to go to the next page. The number of results to show in a page can
        |be configured using the "github-graphql-client.number-of-results-in-page" setting in the config file.
        |
        |---------------------------------------------------------------------------------------------------------------
        |""".stripMargin)
  }

  /**
   * Prints a menu and waits for the user to input an integer denoting whether she wants to search for users or
   * repositories.
   *
   * The method is called recursively if the user enters an invalid input until she enters a valid number. If `0` is
   * entered, the application is terminated.
   *
   * @return `1` if the user wants to search for Users, or `2` if the user wants to search for repositories.
   */
  def askWhatToSearch: Int = {
    print(
      """
        |What do you want to search?
        |
        |1. Users
        |2. Repositories
        |0. Exit
        |
        |Please enter your choice:
        |""".stripMargin)
    try {
      val choice = StdIn.readInt()
      if (choice < 0 || choice > 2) {
        println("\nPlease enter a value between 0 and 2!\n")
        askWhatToSearch
      }
      else if (choice == 0)
        sys.exit()
      else
        choice
    }
    catch {
      case _: NumberFormatException =>
        println("\nInvalid value entered. Please try again!\n")
        askWhatToSearch
    }
  }

  /**
   * Asks the user the term she wishes to search with.
   *
   * @return The search term entered by the user.
   */
  def askForSearchTerm: String = {
    println("Please enter your search term:")
    StdIn.readLine()
  }

  /**
   * Performs a search query on GitHub and displays the results.
   *
   * A fixed number of results are displayed at a time. If more results are available, the user will be asked to enter
   * any value to see the next set of results. The user may skip rest of the results and exit the application by
   * entering 'Q'. This prompt will not be shown and the application will exit if more results are not available.
   *
   * The number of results displayed at a time can be configured using the
   * "github-graphql-client.number-of-results-in-page" configuration setting.
   *
   * @param query          The [[Query]] to execute on the GitHub server.
   * @param searchType     The type of object the user wants to search.
   * @param nextPageCursor Optional value that specifies the cursor marking the start of next page of the results.
   *                       This is only needed for pagination. If `None`, the first page of results is shown.
   */
  @scala.annotation.tailrec
  def searchAndPaginate(query: Query[SearchResult], searchType: SearchType, nextPageCursor: Option[String] = None): Unit = {
    // Perform search and get the results
    val results = GitHubSearchAppUtils.performSearch(query, searchType, client, nextPageCursor)

    // Print the results
    GitHubSearchAppUtils.printSearchResults(results)

    // If another page of results is available, recursively call this method when user enters any input, until either
    // all the pages are exhausted or the user enters 'Q'
    if (results.data.search.pageInfo.hasNextPage) {
      println("\nEnter anything to show next page of results. Enter 'Q' to quit:")
      val choice = StdIn.readLine()
      if (choice.equalsIgnoreCase("Q")) {
        sys.exit()
      }
      else {
        searchAndPaginate(query, searchType, Some(results.data.search.pageInfo.endCursor))
      }
    }
  }
}
