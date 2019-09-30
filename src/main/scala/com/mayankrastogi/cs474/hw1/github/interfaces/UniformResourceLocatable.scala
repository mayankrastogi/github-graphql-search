package com.mayankrastogi.cs474.hw1.github.interfaces

/**
 * Represents a type that can be retrieved by a URL.
 */
trait UniformResourceLocatable {
  /**
   * The HTML path to this resource.
   */
  val resourcePath: String
  /**
   * The URL to this resource.
   */
  val url: String
}
