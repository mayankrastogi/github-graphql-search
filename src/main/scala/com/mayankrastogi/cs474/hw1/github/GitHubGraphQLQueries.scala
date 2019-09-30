package com.mayankrastogi.cs474.hw1.github

import com.mayankrastogi.cs474.hw1.github.enums.SearchType
import com.mayankrastogi.cs474.hw1.github.objects.{RepositorySearchResult, UserSearchResult}
import com.mayankrastogi.cs474.hw1.graphql.Query

/**
 * Definition of all queries supported by this application, along with the types of results returned by them.
 */
object GitHubGraphQLQueries {

  /**
   * Create a query for searching repositories on GitHub.
   *
   * @param query          The search term for finding the repositories.
   * @param numOfResults   The number of results to return at a time in one page.
   * @param nextPageCursor The cursor pointing to the item after which the set of results should start.
   * @return A [[Query]] of type [[RepositorySearchResult]]
   */
  def searchRepositories(query: String, numOfResults: Int, nextPageCursor: Option[String] = None): Query[RepositorySearchResult] = {
    val graphQLQuery =
      """
        |query search($query: String!, $type: SearchType!, $numOfResults: Int!, $nextPageCursor: String) {
        |  search(type: $type, query: $query, first: $numOfResults, after: $nextPageCursor) {
        |    pageInfo {
        |      hasNextPage
        |      endCursor
        |    }
        |    nodes {
        |      ... on Repository {
        |        name
        |        nameWithOwner
        |        description
        |        watchers {
        |          totalCount
        |        }
        |        stargazers {
        |          totalCount
        |        }
        |        languages(first: 100) {
        |          nodes {
        |            name
        |          }
        |        }
        |        repositoryTopics(first: 100) {
        |          nodes {
        |            topic {
        |              name
        |            }
        |          }
        |        }
        |        sshUrl
        |        url
        |      }
        |    }
        |  }
        |}
        |""".stripMargin

    val variables =
      Map(
        "query" -> query,
        "type" -> SearchType.Repository.toString,
        "numOfResults" -> numOfResults,
        "nextPageCursor" -> nextPageCursor.orNull
      )

    Query(graphQLQuery, variables)
  }

  /**
   * Create a query for searching users on GitHub.
   *
   * @param query          The search term for finding the users.
   * @param numOfResults   The number of results to return at a time in one page.
   * @param nextPageCursor The cursor pointing to the item after which the set of results should start.
   * @return A [[Query]] of type [[UserSearchResult]]
   */
  def searchUsers(query: String, numOfResults: Int, nextPageCursor: Option[String] = None): Query[UserSearchResult] = {
    val graphQLQuery =
      """
        |query search($query: String!, $type: SearchType!, $numOfResults: Int!, $nextPageCursor: String) {
        |  search(type: $type, query: $query, first: $numOfResults, after: $nextPageCursor) {
        |    pageInfo {
        |      hasNextPage
        |      endCursor
        |    }
        |    nodes {
        |      ... on User {
        |        name
        |        login
        |        location
        |        bio
        |        url
        |        followers {
        |          totalCount
        |        }
        |        following {
        |          totalCount
        |        }
        |        repositories(first: 100) {
        |          nodes {
        |            languages(first: 100) {
        |              nodes {
        |                name
        |              }
        |            }
        |            repositoryTopics(first: 100) {
        |              nodes {
        |                topic {
        |                  name
        |                }
        |              }
        |            }
        |          }
        |          totalCount
        |        }
        |        repositoriesContributedTo(first: 100) {
        |          nodes {
        |            languages(first: 100) {
        |              nodes {
        |                name
        |              }
        |            }
        |            repositoryTopics(first: 100) {
        |              nodes {
        |                topic {
        |                  name
        |                }
        |              }
        |            }
        |          }
        |          totalCount
        |        }
        |      }
        |    }
        |  }
        |}
        |""".stripMargin

    val variables =
      Map(
        "query" -> query,
        "type" -> SearchType.User.toString,
        "numOfResults" -> numOfResults,
        "nextPageCursor" -> nextPageCursor.orNull
      )

    Query(graphQLQuery, variables)
  }
}
