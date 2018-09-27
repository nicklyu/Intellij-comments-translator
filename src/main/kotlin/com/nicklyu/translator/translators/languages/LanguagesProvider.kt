package com.nicklyu.translator.translators.languages

interface LanguagesProvider {
    fun getLanguagesList(): MutableMap<String, String>
}