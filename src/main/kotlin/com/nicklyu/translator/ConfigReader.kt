package com.nicklyu.translator

import java.util.*

object ConfigReader {
    private val properties = ResourceBundle.getBundle("comment-translator-config")

    fun readProperty(name: String): String = properties.getString(name)
}