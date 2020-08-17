package com.mentorcliq.mentorship.service.converter

import com.mentorcliq.mentorship.controller.dto.MatchedPairResponse
import com.mentorcliq.mentorship.controller.dto.PairInfoDto
import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.domain.getPairScore
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
class EmployeeConverter {
    fun convertToEmployee(name: String, email: String, division: String, age: String, offset: String) =
            Employee(name = name, email = email, division = division, age = age.toInt(), offset = offset.toInt())

    fun convertCsvToEmployees(file: MultipartFile): List<Employee> {
        val employees: MutableList<Employee> = mutableListOf()
        file.inputStream.bufferedReader().lines().forEach { line ->
            val fields: List<String> = line.split(",")
            if (isFirstRow(fields)) return@forEach
            employees.add(convertToEmployee(
                    name = fields[0], email = fields[1], division = fields[2],
                    age = fields[3], offset = fields[4]
            ))
        }
        return employees
    }

    fun toResponseDto(pairs: List<Pair<Employee, Employee>>): MatchedPairResponse {
        val pairDtos: MutableList<PairInfoDto> = mutableListOf()
        val averageScore: BigDecimal = pairs.asSequence()
                .mapTo(pairDtos) { pair ->
                    PairInfoDto(first = pair.first, second = pair.second, score = pair.getPairScore())
                }
                .map { it.score }
                .reduce { acc, dto -> acc + dto } / pairDtos.size.toBigDecimal()
        return MatchedPairResponse(pairs = pairDtos, averageScore = averageScore)
    }
}
