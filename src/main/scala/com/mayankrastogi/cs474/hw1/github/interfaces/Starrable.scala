package com.mayankrastogi.cs474.hw1.github.interfaces

import com.mayankrastogi.cs474.hw1.github.connections.UserConnection

/**
 * Things that can be starred.
 */
trait Starrable extends Node {
  /**
   * A list of users who have starred this starrable.
   */
  val stargazers: UserConnection
}
