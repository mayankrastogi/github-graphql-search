package com.mayankrastogi.cs474.hw1.utils

import com.google.gson.Gson
import com.mayankrastogi.cs474.hw1.graphql.{JsonConverter, JsonConverterFactory}

/**
 * A factory for producing instances of [[GsonJsonConverterFactory]] for use with
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 */
object GsonJsonConverterFactory extends JsonConverterFactory {
  override def create: JsonConverter = new GsonJsonConverter(new Gson())
}
