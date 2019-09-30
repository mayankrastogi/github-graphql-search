package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.connections.UserConnection
import com.mayankrastogi.cs474.hw1.github.interfaces.{Node, SearchResultItem, UniformResourceLocatable}

/**
 * Models a repository on GitHub.
 *
 * A repository contains the content for a project.
 *
 * @param name          The name of the repository.
 * @param nameWithOwner The repository's name with owner.
 * @param description   The description of the repository.
 * @param watchers      A list of users watching the repository.
 * @param stargazers    A list of users who have starred this repository.
 * @param sshUrl        The SSH URL to clone this repository.
 * @param id            The unique identifier of this repository.
 * @param resourcePath  The HTTP path for this repository.
 * @param url           The HTTP URL for this repository.
 */
case class Repository(name: String,
                      nameWithOwner: String,
                      description: String,
                      watchers: UserConnection,
                      stargazers: UserConnection,
                      sshUrl: String,
                      override val id: String,
                      override val resourcePath: String,
                      override val url: String)
  extends SearchResultItem with Node with UniformResourceLocatable
