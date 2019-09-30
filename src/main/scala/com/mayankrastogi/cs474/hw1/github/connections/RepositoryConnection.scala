package com.mayankrastogi.cs474.hw1.github.connections

import com.mayankrastogi.cs474.hw1.github.objects.{PageInfo, Repository}

/**
 * A list of repositories that matched against a search query.
 *
 * @param nodes      A list of repositories.
 * @param pageInfo   Information to aid in pagination.
 * @param totalCount Identifies the total count of items in the connection.
 */
case class RepositoryConnection(override val nodes: List[Repository],
                                override val pageInfo: PageInfo,
                                override val totalCount: Option[Int])
  extends SearchResultItemConnection
