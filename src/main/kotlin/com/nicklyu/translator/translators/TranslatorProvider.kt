package com.nicklyu.translator.translators

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project

class TranslatorProvider(private val project: Project) : ProjectComponent {
    val translator: Translator by lazy {
        //todo: check settings
        YandexTranslator(project)
    }
}