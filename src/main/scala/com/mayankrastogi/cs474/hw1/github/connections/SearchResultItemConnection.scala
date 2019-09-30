package com.mayankrastogi.cs474.hw1.github.connections

import com.mayankrastogi.cs474.hw1.github.interfaces.{Connection, SearchResultItem}

/**
 * A list of results that matched against a search query.
 */
abstract class SearchResultItemConnection extends Connection[SearchResultItem]
