package com.mentorcliq.mentorship.service

import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.strategy.ScoreCalculator
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

interface IMentorService {
    fun getBestPairMatches(employees: List<Employee>): List<Pair<Employee, Employee>>
}

@Service
class MentorService(private val scoreCalculator: ScoreCalculator) : IMentorService {

    private val comparator: Comparator<List<Pair<Employee, Employee>>> =
            Comparator { list1, list2 ->
                val score2: BigDecimal = list2.asSequence()
                        .map { scoreCalculator.calculateScore(it) }
                        .reduce { acc, pair -> acc + pair }
                val score1: BigDecimal = list1.asSequence()
                        .map { scoreCalculator.calculateScore(it) }
                        .reduce { acc, pair -> acc + pair }
                score2.compareTo(score1)
            }

    override fun getBestPairMatches(employees: List<Employee>): List<Pair<Employee, Employee>> {
        val pairs: List<List<Pair<Employee, Employee>>> = getAllPairs(employees)
        return Collections.min(pairs, comparator)
    }

    private fun getAllPairs(employees: List<Employee>): List<List<Pair<Employee, Employee>>> {
        val pairs: MutableList<MutableList<Pair<Employee, Employee>>> = mutableListOf()
        for (i in 0 until employees.size - 1) {
            pairs.add(mutableListOf())
        }
        for (i in 0 until employees.size - 1) {
            for (j in 0 until employees.size - i - 1) {
                if (i == 0) {
                    pairs[j].add(employees[i + j + 1] to employees[i])
                } else {
                    pairs.addPair(employees[i + j + 1] to employees[i])
                }
            }
        }
        return pairs
    }
}

private fun List<Pair<Employee, Employee>>.containsAnyFromThePair(pair: Pair<Employee, Employee>): Boolean {
    return this.any { thisPair ->
        thisPair.first == pair.first || thisPair.first == pair.second ||
                thisPair.second == pair.first || thisPair.second == pair.second
    }
}

private fun List<MutableList<Pair<Employee, Employee>>>.addPair(pair: Pair<Employee, Employee>) {
    for (i in this.indices) {
        val pairList: MutableList<Pair<Employee, Employee>> = this[i]
        if (!pairList.containsAnyFromThePair(pair)) {
            pairList.add(pair)
        }
    }
}


