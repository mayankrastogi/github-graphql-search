package com.mayankrastogi.cs474.hw1.github.objects

import com.mayankrastogi.cs474.hw1.github.interfaces.Node

/**
 * Represents a given language found in repositories.
 *
 * @param name  The name of the current language.
 * @param color The color defined for the current language.
 * @param id    The unique identifier of this language.
 */
case class Language(name: String,
                    color: String,
                    override val id: String)
  extends Node
