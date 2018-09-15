package com.nicklyu.translator.translators

import com.nicklyu.translator.ConfigReader
import com.nicklyu.translator.translators.models.YandexResponse
import com.nicklyu.translator.translators.models.get
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class YandexTranslator : Translator {
    private val commonUrlPart = "https://translate.yandex.net/api/v1.5/tr.json/translate"

    private val key = ConfigReader.readProperty("translator.yandex.key")
    private val client = OkHttpClient()

    override fun translate(phrase: String): String {
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), byteArrayOf())

        val requestUrl =
                commonUrlPart +
                        "?key=$key" +
                        "&text=$phrase" +
                        "&lang=ru" //todo: change language

        val request = Request.Builder()
                .url(requestUrl)
                .post(body)
                .build()
        val response = client.newCall(request).execute().body()?.get<YandexResponse>()

        return response?.text?.joinToString().orEmpty()
    }
}