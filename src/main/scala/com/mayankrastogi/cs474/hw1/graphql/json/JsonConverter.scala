package com.mayankrastogi.cs474.hw1.graphql.json

/**
 * Defines an interface used by the [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]] to convert Scala objects to
 * JSON and vice-versa.
 *
 * This allows users to use their favorite JSON (de)serializing library with
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]] for performing the JSON conversion.
 */
trait JsonConverter {
  /**
   * Converts a Scala object to it's JSON representation.
   *
   * @param obj The scala object to convert.
   * @return A string containing the JSON representation of the `obj`.
   */
  def serialize(obj: Any): String

  /**
   * Converts a JSON representation to Scala objects.
   *
   * @param json The JSON string.
   * @tparam T The class that models the passed JSON.
   * @return An object of the class that models the passed JSON string.
   */
  def deserialize[T: Manifest](json: String): T
}
