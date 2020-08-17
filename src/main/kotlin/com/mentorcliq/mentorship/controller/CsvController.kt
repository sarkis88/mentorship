package com.mentorcliq.mentorship.controller

import com.mentorcliq.mentorship.controller.dto.MatchedPairResponse
import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.service.MentorService
import com.mentorcliq.mentorship.service.converter.EmployeeConverter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/csv")
@Controller
class CsvController(
        private val employeeConverter: EmployeeConverter,
        private val mentorService: MentorService
) {

    @Operation(summary = "Upload CSV file to get best match for mentoring as JSON")
    @ApiResponse(
            description = "get matched pairs as JSON", responseCode = "200",
            content = [Content(schema = Schema(implementation = MatchedPairResponse::class))])
    @PostMapping(
            path = ["/upload/json"],
            consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun uploadCsvFileAndGetMatchesAsJson(file: MultipartFile): MatchedPairResponse {
        val employees: List<Employee> = employeeConverter.convertCsvToEmployees(file)
        val matches: List<Pair<Employee, Employee>> = mentorService.getBestPairMatches(employees)
        return employeeConverter.toResponseDto(pairs = matches)
    }

    @PostMapping(
            path = ["/upload"],
            consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadCsvFileAndGetMatchesAsHtml(file: MultipartFile, model: Model): String {
        val employees: List<Employee> = employeeConverter.convertCsvToEmployees(file)
        val matches: List<Pair<Employee, Employee>> = mentorService.getBestPairMatches(employees)
        val matchedPairResponse: MatchedPairResponse = employeeConverter.toResponseDto(pairs = matches)
        model.addAttribute("pairs", matchedPairResponse)
        return "matches.html"
    }


}