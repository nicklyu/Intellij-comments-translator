package com.nicklyu.translator.translators

class TranslatorProvider {
    val translator: Translator by lazy {
        //todo: check settings
        YandexTranslator()
    }
}