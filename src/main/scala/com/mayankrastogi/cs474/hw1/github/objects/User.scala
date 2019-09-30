package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.connections.{RepositoryConnection, UserConnection}
import com.mayankrastogi.cs474.hw1.github.interfaces.{Node, SearchResultItem, UniformResourceLocatable}

/**
 * Models a User on GitHub.
 *
 * A user is an individual's account on GitHub that owns repositories and can make new content.
 *
 * @param name                      The user's public profile name.
 * @param login                     The username used to login.
 * @param location                  The user's public profile location.
 * @param bio                       The user's public profile bio.
 * @param followers                 A list of users the given user is followed by.
 * @param following                 A list of users the given user is following.
 * @param repositories              A list of repositories that the user owns.
 * @param repositoriesContributedTo A list of repositories that the user recently contributed to.
 * @param id                        The unique identifier of this user.
 * @param resourcePath              The HTTP path for this user.
 * @param url                       The HTTP URL for this user.
 */
case class User(name: String,
                login: String,
                location: String,
                bio: String,
                followers: UserConnection,
                following: UserConnection,
                repositories: RepositoryConnection,
                repositoriesContributedTo: RepositoryConnection,
                override val id: String,
                override val resourcePath: String,
                override val url: String)
  extends SearchResultItem with Node with UniformResourceLocatable
