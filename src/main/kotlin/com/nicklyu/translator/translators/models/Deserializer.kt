package com.nicklyu.translator.translators.models

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.ResponseBody


val mapper = jacksonObjectMapper()

inline fun <reified T> ResponseBody.get(): T = mapper.readValue(this.string(), T::class.java)


