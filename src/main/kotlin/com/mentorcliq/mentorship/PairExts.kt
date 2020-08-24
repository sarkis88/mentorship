package com.mentorcliq.mentorship

import com.mentorcliq.mentorship.domain.Employee

fun List<Pair<Employee, Employee>>.containsAnyFromThePair(pair: Pair<Employee, Employee>): Boolean {
    return this.any { thisPair ->
        thisPair.first == pair.first || thisPair.first == pair.second ||
                thisPair.second == pair.first || thisPair.second == pair.second
    }
}

fun List<MutableList<Pair<Employee, Employee>>>.addPair(pair: Pair<Employee, Employee>) {
    for (i in this.indices) {
        val pairList: MutableList<Pair<Employee, Employee>> = this[i]
        if (!pairList.containsAnyFromThePair(pair)) {
            pairList.add(pair)
        }
    }
}