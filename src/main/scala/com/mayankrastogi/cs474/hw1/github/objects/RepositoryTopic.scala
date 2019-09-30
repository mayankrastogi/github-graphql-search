package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.interfaces.{Node, UniformResourceLocatable}

/**
 * A repository-topic connects a repository to a topic.
 *
 * @param topic        The topic.
 * @param id           The unique identifier of this repository-topic.
 * @param resourcePath The HTTP path for this repository-topic.
 * @param url          The HTTP URL for this repository-topic.
 */
case class RepositoryTopic(topic: Topic,
                           override val id: String,
                           override val resourcePath: String,
                           override val url: String)
  extends Node with UniformResourceLocatable
