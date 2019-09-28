package com.mayankrastogi.cs474.hw1.graphql

/**
 * Models a response from a GraphQL API server.
 *
 * @param data The response object (of type `T`) obtained from the server.
 * @tparam T The type of the response object expected to be sent by the server.
 */
case class QueryResult[T](data: T)
