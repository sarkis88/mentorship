package com.mentorcliq.mentorship.controller

import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.domain.Preference
import com.mentorcliq.mentorship.getFileAsByteArray
import com.mentorcliq.mentorship.getFileAsString
import com.mentorcliq.mentorship.service.IMentorService
import com.mentorcliq.mentorship.service.MentorService
import com.mentorcliq.mentorship.service.converter.EmployeeConverter
import com.mentorcliq.mentorship.strategy.ListPairsScoreComparator
import com.mentorcliq.mentorship.strategy.PreferenceScoreCalculator
import com.mentorcliq.mentorship.strategy.ScoreCalculator
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class CsvControllerTest {

    private lateinit var mockMvc: MockMvc

    private lateinit var scoreCalculator: ScoreCalculator
    private lateinit var employeeConverter: EmployeeConverter
    private lateinit var mentorService: IMentorService
    private lateinit var comparator: ListPairsScoreComparator

    @BeforeEach
    fun setUp() {
        scoreCalculator = spy(PreferenceScoreCalculator())
        comparator = spy(ListPairsScoreComparator(scoreCalculator))
        employeeConverter = spy(EmployeeConverter(scoreCalculator))
        mentorService = spy(MentorService(comparator))

        mockMvc = MockMvcBuilders
                .standaloneSetup(CsvController(employeeConverter, mentorService))
                .build()
    }

    @Test
    fun `uploadCsvFile successful with JSON response`() {
        mockMvc.perform(multipart("/csv/upload/json").file("file", getFileAsByteArray("file/employee.csv")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(getFileAsString("json/response.json")))
                .andDo(log())

        verify(employeeConverter).convertToEmployee("Gabrielle Clarkson", "tamas@me_example.com",
                "Accounting", "25", "2", "Moscow", "Yes"
        )
        verify(employeeConverter).convertToEmployee("Zoe Peters", "gozer@icloud_example.com",
                "Finance", "30", "3", "Moscow", "No Preference"
        )
        verify(employeeConverter).convertToEmployee("Jacob Murray", "lstein@me_example.com",
                "Accounting", "22", "2", "New York", "No")
        verify(employeeConverter).convertToEmployee("Nicholas Vance", "saridder@outlook_example.com",
                "Engineering", "27", "-3", "Athens", "No Preference")
        verify(mentorService).getBestPairMatches(
                employees = listOf(
                        Employee("Gabrielle Clarkson", "tamas@me_example.com",
                                "Accounting", 25, 2, "Moscow", Preference.YES),
                        Employee("Zoe Peters", "gozer@icloud_example.com",
                                "Finance", 30, 3, "Moscow", Preference.NO_PREFERENCE),
                        Employee("Jacob Murray", "lstein@me_example.com",
                                "Accounting", 22, 2, "New York", Preference.NO),
                        Employee("Nicholas Vance", "saridder@outlook_example.com",
                                "Engineering", 27, -3, "Athens", Preference.NO_PREFERENCE)
                )
        )
    }
}