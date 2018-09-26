package com.nicklyu.translator.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.nicklyu.translator.translators.TranslatorType

@State(name = "CommentsTranslatorConfig", storages = arrayOf(Storage("CommentsTranslatorSettingsState.xml")))
class CommentsTranslatorSettingsState : PersistentStateComponent<CommentsTranslatorSettingsState> {

    /**
     * For active [TranslatorType] you must define corresponding api key (see [yandexApiKey])
     * and translator description (see [yandexApiDescription])
     */
    var currentTranslator: TranslatorType? = null


    //Api keys block
    var yandexApiKey: String? = null


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