package com.mayankrastogi.cs474.hw1.graphql.json.converters

import java.lang.reflect.{ParameterizedType, Type}

import com.google.gson.Gson
import com.mayankrastogi.cs474.hw1.graphql.json.JsonConverter

/**
 * An adapter for using the `Gson` library for performing JSON (de)serialization in the
 * [[com.mayankrastogi.cs474.hw1.graphql.GraphQLClient]].
 *
 * @param gson A pre-configured instance of [[Gson]].
 */
class GsonJsonConverter(val gson: Gson) extends JsonConverter {

  /**
   * Converts a Scala object to it's JSON representation.
   *
   * @param obj The scala object to convert.
   * @return A string containing the JSON representation of the `obj`.
   */
  override def serialize(obj: Any): String = gson.toJson(obj)

  /**
   * Converts a JSON representation to Scala objects.
   *
   * @param json The JSON string.
   * @tparam T The class that models the passed JSON.
   * @return An object of the class that models the passed JSON string.
   */
  override def deserialize[T: Manifest](json: String): T = gson.fromJson(json, typeFromManifest(manifest[T]))

  /**
   * Private helper to extract Runtime Type information from the Manifest so that we can pass it to Gson for it to know
   * which kind of object to map the json data to.
   *
   * Borrowed from Kipton Barros's answer on StackOverflow:
   * https://stackoverflow.com/a/14166997/4463881
   *
   * @param m The Manifest for the generic class.
   * @return The [[Type]] of the generic class from the manifest.
   */
  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) {
      m.runtimeClass
    }
    else new ParameterizedType {
      def getRawType: Class[_] = m.runtimeClass

      def getActualTypeArguments: Array[Type] = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType: Null = null
    }
  }
}
