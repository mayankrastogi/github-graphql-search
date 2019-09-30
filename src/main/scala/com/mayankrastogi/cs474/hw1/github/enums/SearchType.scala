package com.mayankrastogi.cs474.hw1.github.enums

/**
 * The types of search items that can be searched.
 */
object SearchType extends Enumeration {
  type SearchType = Value

  /**
   * Returns results matching repositories.
   */
  val Repository = Value("REPOSITORY")
  /**
   * Returns results matching users and organizations on GitHub.
   */
  val User = Value("USER")
}
