package com.mayankrastogi.cs474.hw1.github.connections

import com.mayankrastogi.cs474.hw1.github.interfaces.Connection
import com.mayankrastogi.cs474.hw1.github.objects.{Language, PageInfo}

/**
 * A list of languages associated with the parent.
 *
 * @param nodes      A list of languages.
 * @param pageInfo   Information to aid in pagination.
 * @param totalCount Identifies the total count of items in the connection.
 */
case class LanguageConnection(override val nodes: List[Language],
                              override val pageInfo: PageInfo,
                              override val totalCount: Option[Int])
  extends Connection[Language]
