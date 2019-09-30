package com.mayankrastogi.cs474.hw1.graphql.json.factories

import com.mayankrastogi.cs474.hw1.graphql.json.converters.LiftJsonConverter
import com.mayankrastogi.cs474.hw1.graphql.json.{JsonConverter, JsonConverterFactory}
import net.liftweb.json.DefaultFormats

/**
 * A factory for producing instances of [[LiftJsonConverter]] for use with
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 */
object LiftJsonConverterFactory extends JsonConverterFactory {
  /**
   * The factory method that produces a [[LiftJsonConverter]].
   *
   * @return An instance of [[LiftJsonConverter]] configured for use with the
   *         [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]]
   */
  override def create: JsonConverter = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    new LiftJsonConverter()
  }
}
