package com.mentorcliq.mentorship.domain

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
        var offset: Int
)

fun Pair<Employee, Employee>.getPairScore(): BigDecimal {
    val age: BigDecimal = AGE_WEIGHT * (if (abs(this.first.age - this.second.age) <= AGE_DIFF) BigDecimal.ONE else BigDecimal.ZERO)
    val division: BigDecimal = DIVISION_WEIGHT * (if (this.first.division == this.second.division) BigDecimal.ONE else BigDecimal.ZERO)
    val offset: BigDecimal = OFFSET_WEIGHT * (if (this.first.offset == this.second.offset) BigDecimal.ONE else BigDecimal.ZERO)
    return age + division + offset
}
