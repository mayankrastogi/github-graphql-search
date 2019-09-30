package com.mayankrastogi.cs474.hw1.graphql

import com.mayankrastogi.cs474.hw1.graphql.json.JsonConverter
import com.typesafe.scalalogging.LazyLogging
import scalaj.http.Http

/**
 * An HTTP client that can be used to send queries to any server that supports the GraphQL API spec.
 *
 * @param apiEndpoint      The URL of the GraphQL API server.
 * @param jsonConverter    The [[JsonConverter]] to use for (de)serializing JSON payloads.
 * @param headersPopulator (Optional) A lambda function that returns the headers to be added for a given [[Query]].
 *                         This can be used, for example, to add authentication headers to API calls.
 *                         If `None`, "Content-Type" and "Accept" headers are added by default for interchanging JSON.
 */
class GraphQLClient(val apiEndpoint: String,
                    val jsonConverter: JsonConverter,
                    val headersPopulator: Option[Query[_] => Map[String, String]] = None)
  extends LazyLogging {

  /**
   * Executes the specified `query` on the configured GraphQL API server.
   *
   * @param query The query to execute.
   * @tparam T The type of "data" expected to be returned from the server in response to the `query`.
   * @return A [[QueryResult]] containing the `data` in the expected type `T`.
   */
  def executeQuery[T: Manifest](query: Query[T]): QueryResult[T] = {
    logger.trace(s"query(query: $query")

    // Get the headers to be added to this query; Add default headers if the lambda is not provided
    val populateHeaders = headersPopulator.getOrElse { _: Query[_] =>
      Map(
        "Content-Type" -> "application/json",
        "Accept" -> "application/json"
      )
    }

    // Make POST request to the GraphQL server
    val request = Http(apiEndpoint)
      .headers(populateHeaders(query))
      // Serialize the given query to JSON and POST it to the server
      .postData(jsonConverter.serialize(query))

    logger.debug(s"Making HTTP request: $request")
    val response = request.asString
    logger.debug(s"Got response: $response")

    // Deserialize the JSON response from the server into the expected format
    jsonConverter.deserialize[QueryResult[T]](response.body)
  }
}

/**
 * Companion object for [[GraphQLClient]].
 */
object GraphQLClient {
  /**
   * Creates a new [[GraphQLClient.Builder]] for building a [[GraphQLClient]].
   *
   * @return The builder for [[GraphQLClient]].
   */
  def newBuilder: GraphQLClient.Builder = new Builder

  /**
   * A builder for constructing a [[GraphQLClient]].
   */
  class Builder {
    private var _apiEndpoint: String = _
    private var _jsonConverter: JsonConverter = _
    private var _headersPopulator: Option[Query[_] => Map[String, String]] = None

    /**
     * Sets the URL of the GraphQL API server.
     *
     * @param endpoint The URL of the GraphQL API server.
     * @return A reference to the same [[Builder]] instance to follow the `Builder` pattern.
     */
    def apiEndpoint(endpoint: String): Builder = {
      _apiEndpoint = endpoint
      this
    }

    /**
     * Sets the [[JsonConverter]] to use for (de)serializing JSON payloads.
     *
     * @param converter The [[JsonConverter]] to use for (de)serializing JSON payloads.
     * @return A reference to the same [[Builder]] instance to follow the `Builder` pattern.
     */
    def jsonConverter(converter: JsonConverter): Builder = {
      _jsonConverter = converter
      this
    }

    /**
     * Sets the lambda function that returns the headers to be added for a given [[Query]].
     *
     * This can be used, for example, to add authentication headers to API calls.
     * If `None`, "Content-Type" and "Accept" headers are added by default for interchanging JSON.
     *
     * @param populator A lambda function that returns the headers to be added for a given [[Query]].
     * @return A reference to the same [[Builder]] instance to follow the `Builder` pattern.
     */
    def headersPopulator(populator: Query[_] => Map[String, String]): Builder = {
      _headersPopulator = Some(populator)
      this
    }

    /**
     * Instantiates a new [[GraphQLClient]] based on the configurations specified on the builder.
     *
     * @return The [[GraphQLClient]] configured using the builder.
     */
    def build: GraphQLClient = new GraphQLClient(_apiEndpoint, _jsonConverter, _headersPopulator)
  }

}