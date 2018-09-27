package com.nicklyu.translator.translators.models

data class YandexResponse(val code: Int, val lang: String?, val text: List<String>?, val message: String?)
