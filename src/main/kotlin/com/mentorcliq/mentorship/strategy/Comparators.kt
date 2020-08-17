package com.mentorcliq.mentorship.strategy

import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.domain.getPairScore
import java.math.BigDecimal

const val AGE_DIFF: Int = 5

val DIVISION_WEIGHT: BigDecimal = "0.30".toBigDecimal()
val AGE_WEIGHT: BigDecimal = "0.30".toBigDecimal()
val OFFSET_WEIGHT: BigDecimal = "0.40".toBigDecimal()

val MENTOR_COMPARATOR: Comparator<Pair<Employee, Employee>> = Comparator { pair1, pair2 ->
    val score1: BigDecimal = pair1.getPairScore()
    val score2: BigDecimal = pair2.getPairScore()
    score2.compareTo(score1)
}