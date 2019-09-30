package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.connections.UserConnection
import com.mayankrastogi.cs474.hw1.github.interfaces.SearchResult

/**
 * The search results obtained upon querying with search type `User`.
 *
 * @param search The [[UserConnection]] for getting the repositories returned by the search query.
 */
case class UserSearchResult(override val search: UserConnection) extends SearchResult
