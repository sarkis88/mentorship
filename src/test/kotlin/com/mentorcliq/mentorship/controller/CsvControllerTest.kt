package com.mentorcliq.mentorship.controller

import com.mentorcliq.mentorship.domain.Employee
import com.mentorcliq.mentorship.getFileAsByteArray
import com.mentorcliq.mentorship.getFileAsString
import com.mentorcliq.mentorship.service.MentorService
import com.mentorcliq.mentorship.service.converter.EmployeeConverter
import com.mentorcliq.mentorship.strategy.MENTOR_COMPARATOR
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class CsvControllerTest {

    private lateinit var mockMvc: MockMvc

    @Spy
    private lateinit var employeeConverter: EmployeeConverter
    @Spy
    private lateinit var mentorService: MentorService

    @BeforeEach
    fun setUp() {
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
                "Accounting", "25","2"
        )
        verify(employeeConverter).convertToEmployee("Zoe Peters","gozer@icloud_example.com",
                "Finance","30","3"
        )
        verify(employeeConverter).convertToEmployee("Jacob Murray", "lstein@me_example.com",
                "Accounting","22", "2")
        verify(employeeConverter).convertToEmployee("Nicholas Vance", "saridder@outlook_example.com",
                "Engineering","27","-3")
        verify(mentorService).getBestPairMatches(
                employees = listOf(
                        Employee("Gabrielle Clarkson", "tamas@me_example.com",
                        "Accounting", 25,2),
                        Employee("Zoe Peters","gozer@icloud_example.com",
                                "Finance",30,3),
                        Employee("Jacob Murray", "lstein@me_example.com",
                                "Accounting",22, 2),
                        Employee("Nicholas Vance", "saridder@outlook_example.com",
                                "Engineering",27,-3)
                ),
                comparator = MENTOR_COMPARATOR
        )
    }
}