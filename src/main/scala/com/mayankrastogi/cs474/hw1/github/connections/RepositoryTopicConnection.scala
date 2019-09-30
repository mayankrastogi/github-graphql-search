package com.mayankrastogi.cs474.hw1.github.connections

import com.mayankrastogi.cs474.hw1.github.interfaces.Connection
import com.mayankrastogi.cs474.hw1.github.objects.{PageInfo, RepositoryTopic}

/**
 * The connection type for RepositoryTopic.
 *
 * @param nodes      A list of repository-topics.
 * @param pageInfo   Information to aid in pagination.
 * @param totalCount Identifies the total count of items in the connection.
 */
case class RepositoryTopicConnection(override val nodes: List[RepositoryTopic],
                                     override val pageInfo: PageInfo,
                                     override val totalCount: Option[Int])
  extends Connection[RepositoryTopic]
