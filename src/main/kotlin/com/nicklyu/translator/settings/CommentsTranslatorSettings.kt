package com.nicklyu.translator.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.ProjectManager
import com.nicklyu.translator.settings.ui.CommentsTranslatorSettingsForm
import com.nicklyu.translator.translators.TranslatorType
import com.nicklyu.translator.translators.caching.TranslationCacheManager
import javax.swing.JComponent

class CommentsTranslatorSettings : Configurable {
    @Volatile
    private var settingsForm: CommentsTranslatorSettingsForm? = null

    @Synchronized
    private fun initializeSettings() {
        if (settingsForm == null)
            settingsForm = CommentsTranslatorSettingsForm()
    }


    override fun getDisplayName(): String = "Comments translator"

    override fun isModified(): Boolean {
        val state = CommentsTranslatorSettingsState.instance

        @Suppress("UnnecessaryVariable")
        val isModified =
                state.currentTranslator != settingsForm!!.translator()
                        || state.currentApiKey() != settingsForm!!.apiKey()
                        || state.currentChosenLanguage() != settingsForm!!.targetLanguage()


        return isModified
    }

    override fun apply() {
        val state = CommentsTranslatorSettingsState.instance

        //todo: separate cache for each language
        if (state.currentChosenLanguage() != settingsForm!!.targetLanguage()) {
            ProjectManager.getInstance().openProjects.forEach { project ->
                project.getComponent(TranslationCacheManager::class.java).clear()
            }
        }
        //type
        state.currentTranslator = settingsForm!!.translator()

        //api key and language
        when (state.currentTranslator) {
            TranslatorType.YANDEX -> {
                state.yandexApiKey = settingsForm!!.apiKey()
                state.yandexChosenLanguage = settingsForm!!.targetLanguage()
            }
        }
    }

    override fun createComponent(): JComponent? {
        initializeSettings()

        //todo: think about finding better place to update language list
        CommentsTranslatorSettingsState.instance.updateLanguageList()
        return settingsForm!!.content
    }
}