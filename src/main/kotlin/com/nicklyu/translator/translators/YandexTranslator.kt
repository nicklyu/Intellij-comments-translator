package com.nicklyu.translator.translators

import com.intellij.openapi.project.Project
import com.nicklyu.translator.settings.CommentsTranslatorSettingsState
import com.nicklyu.translator.translators.caching.TranslationCacheManager
import com.nicklyu.translator.translators.exceptions.InvalidApiKeyException
import com.nicklyu.translator.translators.models.YandexResponse
import com.nicklyu.translator.translators.models.get
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory

class YandexTranslator(project: Project) : Translator {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val settings = CommentsTranslatorSettingsState.instance

    private val commonUrlPart = "https://translate.yandex.net/api/v1.5/tr.json/translate"
    private val cacheManager: TranslationCacheManager = project.getComponent(TranslationCacheManager::class.java)


    private val key = settings.yandexApiKey
    private val client = OkHttpClient()

    override fun translate(phrase: String): String {
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), byteArrayOf())

        val requestUrl =
                commonUrlPart +
                        "?key=$key" +
                        "&text=$phrase" +
                        "&lang=${settings.currentChosenLanguage()}"

        val request = Request.Builder()
                .url(requestUrl)
                .post(body)
                .build()
        val response = client.newCall(request).execute().body()?.get<YandexResponse>()
        logger.debug("Translation status ${response?.code}. [${response?.text.orEmpty()}]")
        if (response?.code == 401)
            throw InvalidApiKeyException(response.message.orEmpty())
        val translation = response?.text?.joinToString().orEmpty()
        if (translation.isNotBlank())
            cacheManager.put(phrase, translation)


        return translation
    }

}