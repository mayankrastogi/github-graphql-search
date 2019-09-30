package com.mayankrastogi.cs474.hw1.graphql

/**
 * Represents the payload for making a GraphQL query.
 *
 * The JSON representation of this class forms the payload for sending GraphQL queries to a GraphQL API server.
 *
 * @param query     The GraphQL query.
 * @param variables A mapping of variable names to their values, for all variables used in the `query`.
 * @tparam T The type of object expected to be returned by the server in response to this `query`.
 */
case class Query[+T: Manifest](query: String, variables: Map[String, Any])