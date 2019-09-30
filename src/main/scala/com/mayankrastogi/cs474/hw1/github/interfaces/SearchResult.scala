package com.mayankrastogi.cs474.hw1.github.interfaces

import com.mayankrastogi.cs474.hw1.github.connections.SearchResultItemConnection

/**
 * The search results obtained upon querying with search type `Repository`.
 */
trait SearchResult {
  /**
   * The [[SearchResultItemConnection]] for getting the repositories returned by the search query.
   */
  val search: SearchResultItemConnection
}
