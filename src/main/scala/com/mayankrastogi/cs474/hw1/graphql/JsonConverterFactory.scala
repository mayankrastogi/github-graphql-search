package com.mayankrastogi.cs474.hw1.graphql

/**
 * A factory for instantiating and configuring a [[JsonConverter]].
 */
trait JsonConverterFactory {
  /**
   * The factory method that produces a [[JsonConverter]].
   *
   * @return An instance of [[JsonConverter]] configured for use with the [[GraphQLClient]]
   */
  def create: JsonConverter
}
