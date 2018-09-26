package com.nicklyu.translator.translators


enum class TranslatorType(val translatorName: String) {
    YANDEX("Yandex Translator");

    companion object {
        fun type(translatorName: String): TranslatorType {
            values().forEach { type ->
                if (type.translatorName == translatorName)
                    return type
            }
            throw IllegalArgumentException("Unknown type name")
        }
    }
}