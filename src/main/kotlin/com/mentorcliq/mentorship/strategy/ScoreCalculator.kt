package com.mentorcliq.mentorship.strategy

import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.domain.Preference
import org.springframework.stereotype.Component
import java.math.BigDecimal
import kotlin.math.abs

interface ScoreCalculator {
    fun calculateScore(pair: Pair<Employee, Employee>): BigDecimal
}

@Component
class PreferenceScoreCalculator : ScoreCalculator {

    override fun calculateScore(pair: Pair<Employee, Employee>): BigDecimal {
        return if (!isCompatible(pair))
            BigDecimal.ZERO
        else {
            val age: BigDecimal = AGE_WEIGHT * (if (abs(pair.first.age - pair.second.age) <= AGE_DIFF)
                BigDecimal.ONE else BigDecimal.ZERO)
            val division: BigDecimal = DIVISION_WEIGHT * (if (pair.first.division == pair.second.division)
                BigDecimal.ONE else BigDecimal.ZERO)
            val offset: BigDecimal = OFFSET_WEIGHT * (if (pair.first.offset == pair.second.offset)
                BigDecimal.ONE else BigDecimal.ZERO)
            age + division + offset
        }
    }


    private fun isCompatible(pair: Pair<Employee, Employee>): Boolean {
        val firstLocation: String = pair.first.location
        val secondLocation: String = pair.second.location

        val firstPreference: Preference = pair.first.sameLocationPreference
        val secondPreference: Preference = pair.second.sameLocationPreference

        return if (firstPreference == Preference.NO_PREFERENCE && secondPreference == Preference.NO_PREFERENCE) {
            true
        } else if ((firstPreference == Preference.YES && secondPreference == Preference.NO) ||
                (firstPreference == Preference.NO && secondPreference == Preference.YES)) {
            false
        } else if (firstPreference == Preference.YES && secondPreference == Preference.YES) {
            firstLocation == secondLocation
        } else if (firstPreference == Preference.NO && secondPreference == Preference.NO) {
            firstLocation != secondLocation
        } else if ((firstPreference == Preference.YES && secondPreference == Preference.NO_PREFERENCE) ||
                (firstPreference == Preference.NO_PREFERENCE && secondPreference == Preference.YES)) {
            firstLocation == secondLocation
        } else {
            firstLocation != secondLocation
        }
    }
}