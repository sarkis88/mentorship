package com.mentorcliq.mentorship.service

import com.mentorcliq.mentorship.addPair
import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.strategy.ListPairComparator
import org.springframework.stereotype.Service
import java.util.*

interface IMentorService {
    fun getBestPairMatches(employees: List<Employee>): List<Pair<Employee, Employee>>
}

@Service
class MentorService(private val listPairsScoreComparator: ListPairComparator) : IMentorService {

    override fun getBestPairMatches(employees: List<Employee>): List<Pair<Employee, Employee>> {
        val pairs: List<List<Pair<Employee, Employee>>> = getAllPairs(employees)
        return Collections.max(pairs, listPairsScoreComparator)
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


