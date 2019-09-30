package com.mayankrastogi.cs474.hw1.github.objects

/**
 * Information about pagination in a connection.
 *
 * @param endCursor       When paginating forwards, the cursor to continue.
 * @param hasNextPage     When paginating forwards, are there more items?
 * @param hasPreviousPage When paginating backwards, are there more items?
 * @param startCursor     When paginating backwards, the cursor to continue.
 */
case class PageInfo(endCursor: String,
                    hasNextPage: Boolean,
                    hasPreviousPage: Boolean,
                    startCursor: String)