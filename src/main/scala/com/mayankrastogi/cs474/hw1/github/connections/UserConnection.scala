package com.mayankrastogi.cs474.hw1.github.connections

import com.mayankrastogi.cs474.hw1.github.objects.{PageInfo, User}

/**
 * A list of users that matched against a search query.
 *
 * @param nodes      A list of users.
 * @param pageInfo   Information to aid in pagination.
 * @param totalCount Identifies the total count of items in the connection.
 */
case class UserConnection(override val nodes: List[User],
                          override val pageInfo: PageInfo,
                          override val totalCount: Option[Int])
  extends SearchResultItemConnection

