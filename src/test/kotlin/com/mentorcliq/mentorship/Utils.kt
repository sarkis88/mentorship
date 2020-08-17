package com.mentorcliq.mentorship

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

private val objectMapper: ObjectMapper = ObjectMapper().apply {
    registerModule(KotlinModule())
    registerModule(JavaTimeModule())
}

fun getObjectMapper(): ObjectMapper = objectMapper

fun getFileAsString(path: String): String =
        File(MentorshipApplication::class.java.classLoader.getResource(path)!!.toURI()).readText()

fun getFileAsByteArray(path: String): ByteArray =
        File(MentorshipApplication::class.java.classLoader.getResource(path)!!.toURI()).readBytes()

inline fun <reified T> getObjectFromPath(path: String): T = getObjectFromJsonString(getFileAsString(path))

inline fun <reified T> getObjectFromJsonString(jsonString: String): T = getObjectMapper().readValue(jsonString)