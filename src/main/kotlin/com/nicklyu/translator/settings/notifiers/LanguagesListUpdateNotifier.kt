package com.nicklyu.translator.settings.notifiers

import com.intellij.util.messages.Topic

interface LanguagesListUpdateNotifier {

    companion object {
        val LANGUAGES_LIST_UPDATED: Topic<LanguagesListUpdateNotifier> = Topic.create("LANGUAGES_LIST_UPDATED", LanguagesListUpdateNotifier::class.java)
    }

    fun listUpdated()
}