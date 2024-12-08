package com.forero.school.infraestructure.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.forero.school.BaseIT;
import com.forero.school.openapi.model.ErrorObjectDto;
import com.forero.school.openapi.model.RegisteredResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class GetAllRegistrationsBySubjectIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/school/students/";

    private int studentId() {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM student WHERE document_number = ?",
                Integer.class, "109234");
        return id != null ? id : 0;
    }

    private int subjectId() {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM subject WHERE name = ?",
                Integer.class, "ingles");
        return id != null ? id : 0;
    }

    @Test
    void test_getAllRegistrationsBySubject_withRequestValid_shouldStatusOk() throws Exception {
        //Given
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size) VALUES (?, ?)", "ingles", 3);
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size) VALUES (?, ?)", "matematicas", 3);

        this.jdbcTemplate.update("INSERT INTO student (address, city, document_number, first_name, phone, " +
                        "second_name, second_surname, surname) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "VELLA-VISTA", "BOGOTA", "109234", "Valentina", "3214555", "Alvares", "lopex", "Sara");

        this.jdbcTemplate.update("INSERT INTO registered (student_id, subject_id, average, nota1, nota2, nota3) VALUES (?, ?, ?, ?, ?, ?)",
                this.studentId(),
                this.subjectId(),
                new BigDecimal("12.0"),
                new BigDecimal("43.0"),
                new BigDecimal("50.0"),
                new BigDecimal("60.0")
        );
        final int subjectID = this.subjectId();
        final RegisteredResponseDto registered1 = new RegisteredResponseDto();
        registered1.setStudentId(this.studentId());
        registered1.setFirstName("Valentina");
        registered1.setSurname("Sara");
        registered1.setSecondSurname("lopex");
        registered1.setSecondName("Alvares");
        registered1.setDocumentNumber("109234");
        registered1.setAverage(12.0F);
        registered1.setNota1(43.0F);
        registered1.setNota2(50.0F);
        registered1.setNota3(60.0F);
        final List<RegisteredResponseDto> expected = new ArrayList<>();
        expected.add(registered1);
        //When
        final ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + subjectID)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        final String body = response.andReturn().getResponse().getContentAsString();
        final List<RegisteredResponseDto> actual = OBJECT_MAPPER.readValue(body, new TypeReference<List<RegisteredResponseDto>>() {
        });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_getAllRegistrationsBySubject_withSubjectIdInValid_ShouldSubjectNotFound() throws Exception {
        //Given
        final int subjectID = 345;
        final ErrorObjectDto expected = new ErrorObjectDto();
        expected.setMessage("Subject not found");

        //When
        final ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH + subjectID)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        final String body = response.andReturn().getResponse().getContentAsString();
        final ErrorObjectDto actual = OBJECT_MAPPER.readValue(body, ErrorObjectDto.class);
        Assertions.assertEquals(expected, actual);
    }
}
