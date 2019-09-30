package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.connections.RepositoryConnection
import com.mayankrastogi.cs474.hw1.github.interfaces.SearchResult

/**
 * The search results obtained upon querying with search type `Repository`.
 *
 * @param search The [[RepositoryConnection]] for getting the repositories returned by the search query.
 */
case class RepositorySearchResult(override val search: RepositoryConnection) extends SearchResult
