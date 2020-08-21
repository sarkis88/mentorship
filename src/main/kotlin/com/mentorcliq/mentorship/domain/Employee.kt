package com.mentorcliq.mentorship.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class Employee(
        var name: String,
        var email: String,
        var division: String,
        var age: Int,
        var offset: Int,
        var location: String,
        var sameLocationPreference: Preference
)

enum class Preference(@JsonValue val text: String) {

    YES("Yes"),
    NO("No"),
    NO_PREFERENCE("No Preference");

    companion object {
        @JsonCreator
        @JvmStatic
        fun forValue(string: String): Preference {
            return values().first { it.text == string }
        }
    }
}
