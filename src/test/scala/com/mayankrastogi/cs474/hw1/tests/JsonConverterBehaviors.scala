package com.mayankrastogi.cs474.hw1.tests

import com.mayankrastogi.cs474.hw1.github.connections.RepositoryConnection
import com.mayankrastogi.cs474.hw1.github.interfaces.SearchResult
import com.mayankrastogi.cs474.hw1.github.objects.{Repository, RepositorySearchResult}
import com.mayankrastogi.cs474.hw1.graphql.json.JsonConverterFactory
import com.mayankrastogi.cs474.hw1.graphql.{Query, QueryResult}
import org.scalatest.{FunSuite, Matchers}

/**
 * Defines test cases to test JSON Converters extending the [[com.mayankrastogi.cs474.hw1.graphql.json.JsonConverter]]
 * trait.
 */
trait JsonConverterBehaviors {
  this: FunSuite with Matchers =>

  def jsonConverter(converterFactory: JsonConverterFactory, converterName: String) {
    val converter = converterFactory.create

    test(s"$converterName: Empty Scala objects should serialize to empty JSON objects.") {
      val obj = Query(null, null)
      val json = converter.serialize(obj)

      json shouldBe "{}"
    }

    test(s"$converterName: Empty JSON objects should deserialize to empty (all nullable fields set to null) Scala objects.") {
      val json = "{}"
      val obj = converter.deserialize[QueryResult[SearchResult]](json)

      obj shouldBe QueryResult[SearchResult](null)
    }

    test(s"$converterName: Nested Scala objects should get serialized properly to JSON, with null fields omitted.") {
      val obj =
        QueryResult(
          RepositorySearchResult(
            RepositoryConnection(
              List(Repository("test name", null, null, null, null, null, null, null, null, null, null)),
              null,
              null
            )
          )
        )
      val expectedJson = """{"data":{"search":{"nodes":[{"name":"test name"}]}}}"""

      val actualJson = converter.serialize(obj)

      actualJson shouldBe expectedJson
    }

    test(s"$converterName: Nested JSON should get deserialized properly to Scala objects with missing fields set to null (Option values set to None).") {
      val json = """{"data":{"search":{"nodes":[{"name":"test name"}]}}}"""
      val expectedObj =
        QueryResult(
          RepositorySearchResult(
            RepositoryConnection(
              List(Repository("test name", null, null, null, null, null, null, null, null, null, null)),
              null,
              None
            )
          )
        )

      val actualObj = converter.deserialize[QueryResult[RepositorySearchResult]](json)

      actualObj shouldBe expectedObj
    }
  }
}
