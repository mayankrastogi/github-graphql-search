package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.connections.UserConnection
import com.mayankrastogi.cs474.hw1.github.interfaces.Starrable

/**
 * A topic aggregates entities that are related to a subject.
 *
 * @param name       The topic's name.
 * @param stargazers A list of users who have starred this starrable.
 * @param id         The unique identifier of this topic.
 */
case class Topic(name: String,
                 override val stargazers: UserConnection,
                 override val id: String)
  extends Starrable
