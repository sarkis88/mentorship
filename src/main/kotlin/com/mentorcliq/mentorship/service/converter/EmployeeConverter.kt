package com.mentorcliq.mentorship.service.converter

import com.mentorcliq.mentorship.controller.dto.MatchedPairResponse
import com.mentorcliq.mentorship.controller.dto.PairInfoDto
import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.domain.Preference
import com.mentorcliq.mentorship.strategy.ScoreCalculator
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal

private const val NAME_FIELD = "Name"
private const val EMAIL_FIELD = "Email"
private const val DIVISION_FIELD = "Division"
private const val AGE_FIELD = "Age"
private const val OFFSET_FIELD = "Timezone"

private fun isFirstRow(row: List<String>): Boolean =
        row[0] == NAME_FIELD && row[1] == EMAIL_FIELD && row[2] == DIVISION_FIELD &&
                row[3] == AGE_FIELD && row[4] == OFFSET_FIELD

@Component
class EmployeeConverter(private val scoreCalculator: ScoreCalculator) {

    fun convertToEmployee(name: String, email: String, division: String, age: String, offset: String,
                          location: String, sameLocationPreference: String): Employee =
            Employee(name = name, email = email, division = division, age = age.toInt(), offset = offset.toInt(),
                    location = location, sameLocationPreference = Preference.forValue(sameLocationPreference))

    fun convertCsvToEmployees(file: MultipartFile): List<Employee> {
        val employees: MutableList<Employee> = mutableListOf()
        file.inputStream.bufferedReader().lines().forEach { line ->
            val fields: List<String> = line.split(",")
            if (isFirstRow(fields)) return@forEach
            employees.add(convertToEmployee(
                    name = fields[0], email = fields[1], division = fields[2],
                    age = fields[3], offset = fields[4], location = fields[5], sameLocationPreference = fields[6]
            ))
        }
        return employees
    }

    fun toResponseDto(pairs: List<Pair<Employee, Employee>>): MatchedPairResponse {
        val pairDtos: List<PairInfoDto> = pairs
                .map { pair ->
                    PairInfoDto(first = pair.first, second = pair.second, score = scoreCalculator.calculateScore(pair))
                }
        val averageScore: BigDecimal = pairDtos.asSequence()
                .map { it.score }
                .reduce { acc, next -> acc + next } / pairDtos.size.toBigDecimal()
        return MatchedPairResponse(pairs = pairDtos, averageScore = averageScore)
    }
}
