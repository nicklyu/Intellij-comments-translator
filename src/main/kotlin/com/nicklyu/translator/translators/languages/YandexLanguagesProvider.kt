package com.nicklyu.translator.translators.languages

import com.nicklyu.translator.settings.CommentsTranslatorSettingsState
import com.nicklyu.translator.translators.models.YandexLanguageResponce
import com.nicklyu.translator.translators.models.get
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object YandexLanguagesProvider : LanguagesProvider {
    private val settings = CommentsTranslatorSettingsState.instance
    private val client = OkHttpClient()

    private const val commonUrlPart = "https://translate.yandex.net/api/v1.5/tr.json/getLangs"


    override fun getLanguagesList(): MutableMap<String, String> {
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), byteArrayOf())
        val requestUrl = "$commonUrlPart?key=${settings.yandexApiKey}&ui=en"
        val request = Request.Builder()
                .url(requestUrl)
                .post(body)
                .build()
        val responce = client.newCall(request).execute().body()?.get<YandexLanguageResponce>()

        return responce?.langs ?: CommentsTranslatorSettingsState.instance.yandexTargetLanguages
    }
}