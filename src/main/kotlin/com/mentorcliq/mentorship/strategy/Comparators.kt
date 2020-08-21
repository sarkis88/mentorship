package com.mentorcliq.mentorship.strategy

import com.mentorcliq.mentorship.domain.Employee
import org.springframework.stereotype.Component
import java.math.BigDecimal

interface ListPairComparator : Comparator<List<Pair<Employee, Employee>>> {
    override fun compare(list1: List<Pair<Employee, Employee>>, list2: List<Pair<Employee, Employee>>): Int
}

@Component
class ListPairsScoreComparator(private val scoreCalculator: ScoreCalculator) : ListPairComparator {

    override fun compare(list1: List<Pair<Employee, Employee>>, list2: List<Pair<Employee, Employee>>): Int {
        val score1: BigDecimal = list1.asSequence()
                .map { pair -> scoreCalculator.calculateScore(pair) }
                .reduce { accPair, pair -> accPair + pair }
        val score2: BigDecimal = list2.asSequence()
                .map { pair -> scoreCalculator.calculateScore(pair) }
                .reduce { accPair, pair -> accPair + pair }
        return score1.compareTo(score2)
    }

}