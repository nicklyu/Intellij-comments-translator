package com.nicklyu.translator.translators.models

data class YandexLanguageResponse(val code: String?, val message: String?, val dirs: MutableList<String>, val langs: MutableMap<String, String>)