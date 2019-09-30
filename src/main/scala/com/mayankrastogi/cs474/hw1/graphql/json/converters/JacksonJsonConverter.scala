package com.mayankrastogi.cs474.hw1.graphql.json.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.mayankrastogi.cs474.hw1.graphql.json.JsonConverter

/**
 * An adapter for using the `Jackson` Scala Module for performing JSON (de)serialization in the
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 *
 * @param mapper Jackson's [[ObjectMapper]] which will be used to perform (de)serialization.
 */
class JacksonJsonConverter(val mapper: ObjectMapper with ScalaObjectMapper) extends JsonConverter {

  /**
   * Converts a Scala object to it's JSON representation.
   *
   * @param obj The scala object to convert.
   * @return A string containing the JSON representation of the `obj`.
   */
  override def serialize(obj: Any): String = mapper.writeValueAsString(obj)

  /**
   * Converts a JSON representation to Scala objects.
   *
   * @param json The JSON string.
   * @tparam T The class that models the passed JSON.
   * @return An object of the class that models the passed JSON string.
   */
  override def deserialize[T: Manifest](json: String): T = mapper.readValue(json)
}
