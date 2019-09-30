package com.mayankrastogi.cs474.hw1.graphql.json

/**
 * A factory for instantiating and configuring a [[JsonConverter]].
 */
trait JsonConverterFactory {
  /**
   * The factory method that produces a [[JsonConverter]].
   *
   * @return An instance of [[JsonConverter]] configured for use with the
   *         [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]]
   */
  def create: JsonConverter
}
