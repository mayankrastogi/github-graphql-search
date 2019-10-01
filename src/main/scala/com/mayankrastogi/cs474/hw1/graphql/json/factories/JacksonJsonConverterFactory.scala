package com.mayankrastogi.cs474.hw1.graphql.json.factories

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.mayankrastogi.cs474.hw1.graphql.json.converters.JacksonJsonConverter
import com.mayankrastogi.cs474.hw1.graphql.json.{JsonConverter, JsonConverterFactory}

/**
 * A factory for producing instances of [[JacksonJsonConverter]] for use with
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 */
object JacksonJsonConverterFactory extends JsonConverterFactory {
  /**
   * The factory method that produces a [[JacksonJsonConverter]].
   *
   * @return An instance of [[JacksonJsonConverter]] configured for use with the
   *         [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]]
   */
  override def create: JsonConverter = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.setSerializationInclusion(Include.NON_NULL)
    new JacksonJsonConverter(mapper)
  }
}
