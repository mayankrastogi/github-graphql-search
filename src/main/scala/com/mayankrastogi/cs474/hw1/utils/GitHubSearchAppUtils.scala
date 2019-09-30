package com.mayankrastogi.cs474.hw1.utils

import com.mayankrastogi.cs474.hw1.github.enums.SearchType
import com.mayankrastogi.cs474.hw1.github.enums.SearchType.SearchType
import com.mayankrastogi.cs474.hw1.github.interfaces.SearchResult
import com.mayankrastogi.cs474.hw1.github.objects.{Repository, RepositorySearchResult, User, UserSearchResult}
import com.mayankrastogi.cs474.hw1.graphql.json.factories.JacksonJsonConverterFactory
import com.mayankrastogi.cs474.hw1.graphql.{GraphQLClient, Query, QueryResult}
import com.typesafe.scalalogging.LazyLogging

/**
 * Provides helper methods for the [[com.mayankrastogi.cs474.hw1.GitHubSearchApp]]
 */
object GitHubSearchAppUtils extends LazyLogging {

  /**
   * Creates a new [[GraphQLClient]] using the "Builder" pattern, configured for querying GitHub's GraphQL API.
   *
   * @param endpoint The URL of GitHub's GraphQL API server.
   * @param token    The Personal Token to use for authorization on GitHub.
   * @return The [[GraphQLClient]] for querying GitHub.
   */
  def createGitHubGraphQLClient(endpoint: String, token: String): GraphQLClient = {
    GraphQLClient
      .newBuilder
      .apiEndpoint(endpoint)
      //      .jsonConverter(GsonJsonConverterFactory.create)
      //      .jsonConverter(LiftJsonConverterFactory.create)
      .jsonConverter(JacksonJsonConverterFactory.create)
      .headersPopulator { _ =>
        Map(
          "Authorization" -> s"Bearer $token",
          "Content-Type" -> "application/json",
          "Accept" -> "application/json"
        )
      }
      .build
  }

  /**
   * Performs a search query on GitHub
   *
   * @param query          The [[Query]] to execute on the GitHub server.
   * @param searchType     The type of object the user wants to search.
   * @param client         The [[GraphQLClient]] to use for querying.
   * @param nextPageCursor Optional value that specifies the cursor marking the start of next page of the results.
   *                       This is only needed for pagination. If `None`, the first page of results is shown.
   * @return
   */
  def performSearch(query: Query[SearchResult], searchType: SearchType, client: GraphQLClient, nextPageCursor: Option[String] = None): QueryResult[SearchResult] = {
    logger.trace(s"performSearch(query: $query, searchType: $searchType, client: $client, nextPageCursor: $nextPageCursor)")

    // Update the nextPageCursor variable in the query if it is defined
    // (This will happen during recursive calls for pagination)
    val queryToMake =
    if (nextPageCursor.isDefined)
      query.copy(variables = query.variables ++ Map("nextPageCursor" -> nextPageCursor.orNull))
    else
      query

    logger.debug(s"Performing query: $queryToMake")

    // Execute the GraphQL query and get the deserialized results
    // We need to explicitly cast here since the type information of the generic `Query` gets lost due to type erasure
    // and the runtime won't be able to determine the correct class for deserializing JSON response.
    // Ideally, I would have liked to avoid this, but couldn't make it to work without it.
    searchType match {
      case SearchType.User => client.executeQuery(queryToMake.asInstanceOf[Query[UserSearchResult]])
      case SearchType.Repository => client.executeQuery(queryToMake.asInstanceOf[Query[RepositorySearchResult]])
    }
  }

  /**
   * Prints the results obtained from the search query.
   *
   * @param results The results to display.
   */
  def printSearchResults(results: QueryResult[SearchResult]): Unit = {
    val nodes = results.data.search.nodes

    if (nodes.nonEmpty) {
      nodes.foreach {
        case repo: Repository => printRepositoryDetails(repo)
        case user: User => printUserDetails(user)
      }
    }
    else {
      println("\nNo results found.")
    }
  }

  /**
   * Prints details of a [[Repository]].
   *
   * @param repo The repository object.
   */
  def printRepositoryDetails(repo: Repository): Unit = {
    println(
      s"""
         |Name: ${repo.name}
         |Owner: ${repo.nameWithOwner.split("/").head}
         |Description: ${repo.description}
         |Watchers: ${repo.watchers.totalCount.getOrElse(0)}
         |Stargazers: ${repo.stargazers.totalCount.getOrElse(0)}
         |Languages: ${repo.languages.nodes.map(_.name).mkString(", ")}
         |Topics: ${repo.repositoryTopics.nodes.map(_.topic.name).mkString(", ")}
         |GitHub URL: ${repo.url}
         |Clone URL: ${repo.sshUrl}
         |""".stripMargin)
  }

  /**
   * Prints details of a [[User]].
   *
   * @param user The user object.
   */
  def printUserDetails(user: User): Unit = {

    // Consider all repositories (owned + collaborated-on) for languages and topics analysis
    val allRepositories = user.repositories.nodes ++ user.repositoriesContributedTo.nodes

    // Extract all unique languages and topics from the list of all repositories
    val languages = allRepositories.flatMap(_.languages.nodes.map(_.name)).toSet.mkString(", ")
    val topics = allRepositories.flatMap(_.repositoryTopics.nodes.map(_.topic.name)).toSet.mkString(", ")

    println(
      s"""
         |Name: ${user.name}
         |Username: ${user.login}
         |Bio: ${user.bio}
         |Followers: ${user.followers.totalCount.getOrElse(0)}
         |Following: ${user.following.totalCount.getOrElse(0)}
         |Repositories Owned: ${user.repositories.totalCount.getOrElse(0)}
         |Repositories Collaborated On: ${user.repositoriesContributedTo.totalCount.getOrElse(0)}
         |Languages Used: $languages
         |Topics Interested In: $topics
         |Profile URL: ${user.url}
         |""".stripMargin)
  }
}
