package com.mentorcliq.mentorship.controller.dto

import com.mentorcliq.mentorship.domain.Employee
import java.math.BigDecimal

data class MatchedPairResponse(
        var pairs: List<PairInfoDto>,
        var averageScore: BigDecimal
)

data class PairInfoDto(
        var first: Employee,
        var second: Employee,
        var score: BigDecimal
)
