package com.mayankrastogi.cs474.hw1.github.interfaces

import com.mayankrastogi.cs474.hw1.github.objects.PageInfo

/**
 * A connection in the graph.
 *
 * @tparam T The type of objects that form the nodes in this connection.
 */
trait Connection[+T] {
  /**
   * A list of nodes.
   */
  val nodes: List[T]
  /**
   * Information to aid in pagination.
   */
  val pageInfo: PageInfo
  /**
   * Identifies the total count of objects in the connection.
   */
  val totalCount: Option[Int]
}
