package com.mayankrastogi.cs474.hw1.graphql.json.converters

import com.mayankrastogi.cs474.hw1.graphql.json.JsonConverter
import net.liftweb.json.{Formats, Serialization, parse}

/**
 * An adapter for using the `LiftWeb`'s JSON Module for performing JSON (de)serialization in the
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 *
 * @param formats The implicit [[Formats]] to use for performing (de)serialization using LiftWeb.
 */
class LiftJsonConverter(implicit val formats: Formats) extends JsonConverter {

  /**
   * Converts a Scala object to it's JSON representation.
   *
   * @param obj The scala object to convert.
   * @return A string containing the JSON representation of the `obj`.
   */
  override def serialize(obj: Any): String = Serialization.write(obj)

  /**
   * Converts a JSON representation to Scala objects.
   *
   * @param json The JSON string.
   * @tparam T The class that models the passed JSON.
   * @return An object of the class that models the passed JSON string.
   */
  override def deserialize[T: Manifest](json: String): T = parse(json).extract[T]

}
