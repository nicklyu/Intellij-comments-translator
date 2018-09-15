package com.nicklyu.translator.translators.caching

import com.intellij.openapi.components.ProjectComponent

class TranslationCacheManager : ProjectComponent {
    private val cache = mutableMapOf<String, String>()

    fun get(phrase: String): String? = cache[phrase]

    fun put(phrase: String, translation: String): Boolean {
        if (cache.containsKey(phrase))
            return false

        return !cache.put(phrase, translation).isNullOrEmpty()
    }
}
