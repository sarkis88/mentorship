package com.mentorcliq.mentorship.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.mentorcliq.mentorship.strategy.AGE_DIFF
import com.mentorcliq.mentorship.strategy.AGE_WEIGHT
import com.mentorcliq.mentorship.strategy.DIVISION_WEIGHT
import com.mentorcliq.mentorship.strategy.OFFSET_WEIGHT
import java.math.BigDecimal
import kotlin.math.abs

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
