package com.mayankrastogi.cs474.hw1.tests

import com.mayankrastogi.cs474.hw1.graphql.json.factories.JacksonJsonConverterFactory
import org.scalatest.{FunSuite, Matchers}

/**
 * Tests the implementations of [[com.mayankrastogi.cs474.hw1.graphql.json.JsonConverter]].
 */
class JsonConverterTest extends FunSuite with JsonConverterBehaviors with Matchers {

  // Run the tests for all the JsonConverters

  // GSON fails 2 out of the 4 test cases
  //testsFor(jsonConverter(GsonJsonConverterFactory, "GsonJsonConverter"))

  // Lift-Web JSON fails all the 4 test cases
  //testsFor(jsonConverter(LiftJsonConverterFactory, "LiftJsonConverter"))

  // Jackson passes all the 4 test cases
  testsFor(jsonConverter(JacksonJsonConverterFactory, "JacksonJsonConverter"))
}
