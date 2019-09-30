package com.mayankrastogi.cs474.hw1.graphql.json.factories

import com.google.gson.Gson
import com.mayankrastogi.cs474.hw1.graphql.json.converters.GsonJsonConverter
import com.mayankrastogi.cs474.hw1.graphql.json.{JsonConverter, JsonConverterFactory}

/**
 * A factory for producing instances of [[GsonJsonConverter]] for use with
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 */
object GsonJsonConverterFactory extends JsonConverterFactory {

  /**
   * The factory method that produces a [[GsonJsonConverter]].
   *
   * @return An instance of [[GsonJsonConverter]] configured for use with the
   *         [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]]
   */
  override def create: JsonConverter = new GsonJsonConverter(new Gson())
}
