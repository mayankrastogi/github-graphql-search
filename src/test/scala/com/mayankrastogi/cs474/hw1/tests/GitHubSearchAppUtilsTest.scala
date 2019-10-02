package com.mayankrastogi.cs474.hw1.tests

import java.io.ByteArrayOutputStream

import com.mayankrastogi.cs474.hw1.github.connections.{LanguageConnection, RepositoryConnection, RepositoryTopicConnection, UserConnection}
import com.mayankrastogi.cs474.hw1.github.objects.{Repository, RepositorySearchResult, User, UserSearchResult}
import com.mayankrastogi.cs474.hw1.graphql.json.converters.JacksonJsonConverter
import com.mayankrastogi.cs474.hw1.graphql.{Query, QueryResult}
import com.mayankrastogi.cs474.hw1.utils.GitHubSearchAppUtils
import org.scalatest.{FunSuite, Matchers}

class GitHubSearchAppUtilsTest extends FunSuite with Matchers {

  private val dummyUserConnection = UserConnection(List(), null, None)
  private val dummyRepositoryConnection = RepositoryConnection(List(), null, None)

  test("createGitHubGraphQLClient should return a client which adds authorization header with the passed personal token to all queries.") {
    val testEndpoint = "https://test.endpoint"
    val testToken = "testToken"

    val dummyQuery = Query("query dummyQuery {}", Map())

    val client = GitHubSearchAppUtils.createGitHubGraphQLClient("https://test.endpoint", "testToken")

    client.apiEndpoint shouldBe testEndpoint
    client.jsonConverter shouldBe a[JacksonJsonConverter]

    val addedHeaders = client.headersPopulator
    addedHeaders should not be empty
    addedHeaders.get(dummyQuery) should contain("Authorization" -> s"Bearer $testToken")
  }

  test("printSearchResult should invoke printRepositoryDetails when QueryResult of type RepositorySearchResult is passed.") {
    val testRepository = Repository("test", "", "", dummyUserConnection, dummyUserConnection, "", LanguageConnection(List(), null, None), RepositoryTopicConnection(List(), null, None), "", "", "")
    val testQueryResult = QueryResult(RepositorySearchResult(RepositoryConnection(List(testRepository), null, None)))

    // Capture stdout for comparison

    val expectedOutputStream = new ByteArrayOutputStream()
    Console.withOut(expectedOutputStream) {
      GitHubSearchAppUtils.printRepositoryDetails(testRepository)
    }

    val actualOutputStream = new ByteArrayOutputStream()
    Console.withOut(actualOutputStream) {
      GitHubSearchAppUtils.printSearchResults(testQueryResult)
    }

    // Assert
    actualOutputStream.toString shouldBe expectedOutputStream.toString
  }

  test("printSearchResult should invoke printUserDetails when QueryResult of type UserSearchResult is passed.") {
    val testUser = User("test", "", "", "", dummyUserConnection, dummyUserConnection, dummyRepositoryConnection, dummyRepositoryConnection, "", "", "")
    val testQueryResult = QueryResult(UserSearchResult(UserConnection(List(testUser), null, None)))

    // Capture stdout for comparison

    val expectedOutputStream = new ByteArrayOutputStream()
    Console.withOut(expectedOutputStream) {
      GitHubSearchAppUtils.printUserDetails(testUser)
    }

    val actualOutputStream = new ByteArrayOutputStream()
    Console.withOut(actualOutputStream) {
      GitHubSearchAppUtils.printSearchResults(testQueryResult)
    }

    // Assert
    actualOutputStream.toString shouldBe expectedOutputStream.toString
  }
}
