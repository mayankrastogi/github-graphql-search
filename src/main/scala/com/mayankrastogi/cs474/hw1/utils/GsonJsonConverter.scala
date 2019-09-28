package com.mayankrastogi.cs474.hw1.utils

import com.google.gson.Gson
import com.mayankrastogi.cs474.hw1.graphql.JsonConverter

/**
 * An adapter for using the `Gson` library for performing JSON (de)serialization in the
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 *
 * @param gson A pre-configured instance of [[Gson]].
 */
class GsonJsonConverter(val gson: Gson) extends JsonConverter {

  override def serialize(obj: Any): String = gson.toJson(obj)

  override def deserialize[T](json: String, classOfT: Class[T]): T = gson.fromJson(json, classOfT)
}
