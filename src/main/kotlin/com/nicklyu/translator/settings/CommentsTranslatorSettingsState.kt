package com.nicklyu.translator.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.nicklyu.translator.translators.TranslatorType
import com.nicklyu.translator.translators.languages.YandexLanguagesProvider
import kotlinx.coroutines.experimental.launch

@State(name = "CommentsTranslatorConfig", storages = arrayOf(Storage("CommentsTranslatorSettingsState.xml")))
class CommentsTranslatorSettingsState : PersistentStateComponent<CommentsTranslatorSettingsState> {

    /**
     * For active [TranslatorType] you must define corresponding api key (see [yandexApiKey])
     * and translator description (see [yandexApiDescription])
     * and languages list (see [yandexTargetLanguages]) + target language (see [yandexChosenLanguage])
     */
    var currentTranslator: TranslatorType? = null


    //Api keys block
    var yandexApiKey: String? = null

    //Api languages block
    var yandexTargetLanguages = mutableMapOf(
            "en" to "English"
    )

    //Target languages block
    var yandexChosenLanguage = "en"


    //Translators description
    @Transient
    val yandexApiDescription = "Powered by Yandex.Translate"

    fun currentApiKey() = when (currentTranslator) {
        TranslatorType.YANDEX -> yandexApiKey
        else -> "Empty"
    }

    fun currentApiDescription() = when (currentTranslator) {
        TranslatorType.YANDEX -> yandexApiDescription
        else -> "None"
    }

    fun currentChosenLanguage() = when (currentTranslator) {
        TranslatorType.YANDEX -> yandexChosenLanguage
        else -> "en"
    }

    fun currentLanguageList() = when (currentTranslator) {
        TranslatorType.YANDEX -> yandexTargetLanguages
        else -> mutableMapOf()
    }


    fun updateLanguageList() {
        val languagesProvider = when (currentTranslator) {
            TranslatorType.YANDEX -> YandexLanguagesProvider
            else -> throw IllegalStateException("Can't access unknown translator languages")
        }

        launch {
            val languages = languagesProvider.getLanguagesList()
            yandexTargetLanguages = languages
        }
    }

    override fun getState(): CommentsTranslatorSettingsState {
        return this
    }

    override fun loadState(state: CommentsTranslatorSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    override fun noStateLoaded() {
        currentTranslator = TranslatorType.YANDEX
    }

    companion object {
        val instance: CommentsTranslatorSettingsState
            get() = ServiceManager.getService(CommentsTranslatorSettingsState::class.java)
    }

}