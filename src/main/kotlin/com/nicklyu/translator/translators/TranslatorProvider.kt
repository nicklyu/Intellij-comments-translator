package com.nicklyu.translator.translators

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import com.nicklyu.translator.settings.CommentsTranslatorSettingsState

class TranslatorProvider(private val project: Project) : ProjectComponent {
    val translator: Translator
        get() = when (CommentsTranslatorSettingsState.instance.currentTranslator) {
            TranslatorType.YANDEX -> YandexTranslator(project)
            else -> throw IllegalStateException("Unknown translator")
        }
}