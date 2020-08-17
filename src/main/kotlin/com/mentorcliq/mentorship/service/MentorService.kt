package com.mentorcliq.mentorship.service

import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.strategy.MENTOR_COMPARATOR
import org.springframework.stereotype.Service

@Service
class MentorService {

    fun getBestPairMatches(
            employees: List<Employee>,
            comparator: Comparator<Pair<Employee, Employee>> = MENTOR_COMPARATOR
    ): List<Pair<Employee, Employee>> {
        val sortedPairs: List<Pair<Employee, Employee>> = getSortedAvailablePairs(employees, comparator)
        val bestMatchedPairs: MutableList<Pair<Employee, Employee>> = mutableListOf()
        sortedPairs.forEach { pair ->
            if (!bestMatchedPairs.containsAnyFromThePair(pair)) {
                bestMatchedPairs.add(pair)
            }
        }
        return bestMatchedPairs
    }

    private fun getSortedAvailablePairs(
            employees: List<Employee>,
            comparator: Comparator<Pair<Employee, Employee>> = MENTOR_COMPARATOR
    ): List<Pair<Employee, Employee>> {
        val pairs: MutableList<Pair<Employee, Employee>> = mutableListOf()
        for (i in 0 until employees.size - 1) {
            for (j in 0 until employees.size - i - 1) {
                pairs.add(employees[i + j + 1] to employees[i])
            }
        }
        pairs.sortWith(comparator)
        return pairs
    }
}

private fun List<Pair<Employee, Employee>>.containsAnyFromThePair(pair: Pair<Employee, Employee>): Boolean {
    return this.any { thisPair ->
        thisPair.first == pair.first || thisPair.first == pair.second ||
                thisPair.second == pair.first || thisPair.second == pair.second
    }
}


