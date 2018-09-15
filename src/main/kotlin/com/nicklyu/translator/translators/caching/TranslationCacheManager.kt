package com.nicklyu.translator.translators.caching

import com.intellij.openapi.components.ProjectComponent
import org.slf4j.LoggerFactory

class TranslationCacheManager : ProjectComponent {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val cache = mutableMapOf<String, String>()

    fun get(phrase: String): String? {
        val cachedValue = cache[phrase]
        logger.trace("Phrase $phrase requested from cache. Result: $cachedValue")
        return cachedValue
    }

    fun put(phrase: String, translation: String): Boolean {
        if (cache.containsKey(phrase))
            return false

        logger.debug("Value $translation cached for phrase $phrase")
        return !cache.put(phrase, translation).isNullOrEmpty()
    }
}
