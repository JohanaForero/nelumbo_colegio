package com.forero.school.infraestructure.controller;

import com.forero.school.BaseIT;
import com.forero.school.openapi.model.RegisteredResponseDtoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

class GetAllRegistrationsIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/school/allRegistrations";

    private int studentId(final String documentNumber) {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM student WHERE document_number = ?",
                Integer.class, documentNumber);
        return id != null ? id : 0;
    }

    private int subjectId(final String name) {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM subject WHERE name = ?",
                Integer.class, name);
        return id != null ? id : 0;
    }

    private int registeredId(final int studentId) {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM registered WHERE student_id = ?",
                Integer.class, studentId);
        return id != null ? id : 0;
    }

    @Test
    void test_GetAllRegistrations_withRequestValid_shouldReturnListOfRegisteredResponseDtos() throws Exception {
        //Given
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size) VALUES (?, ?)", "español2", 3);
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size) VALUES (?, ?)", "matematicas1", 3);

        this.jdbcTemplate.update("INSERT INTO student (address, city, document_number, first_name, phone, " +
                        "second_name, second_surname, surname)", " VALUES (?, ?, ?, ?, ?, ?, ?, ?)", "VELLA-VISTA", "BOGOTA",
                "109234", "ALEX", "3214555", "Alvares", "lopex", "Sara");
        this.jdbcTemplate.update("INSERT INTO student (address, city, document_number, first_name, phone, " +
                        "second_name, second_surname, surname)", " VALUES (?, ?, ?, ?, ?, ?, ?, ?)", "VELLA-VISTA", "BOGOTA",
                "109235", "ALEX", "3214555", "Alvares", "lopex", "Alex");
        this.jdbcTemplate.update("INSERT INTO registered (student_id, subject_id, average, nota1, nota2, " +
                        "nota3)", " VALUES (?, ?, ?, ?, ?, ?)", this.studentId("Sara"),
                this.subjectId("matematicas1"), 12, 43, 50);
        this.jdbcTemplate.update("INSERT INTO registered (student_id, subject_id, average, nota1, nota2, " +
                        "nota3)", " VALUES (?, ?, ?, ?, ?, ?)", this.studentId("Alex"),
                this.subjectId("español2"), 12, 43, 50);
        final int registeredId_1 = this.registeredId(this.studentId("109234"));
        final RegisteredResponseDtoDto dto1 = new RegisteredResponseDtoDto();
        dto1.setRegisteredId(registeredId_1);
        dto1.setStudentName("Sara");
        dto1.setDocumentNumber("109234");
        dto1.setSubjectName("matematicas1");
        dto1.setAverage(323.3F);
        dto1.setNota1(12F);
        dto1.setNota2(43F);
        dto1.setNota3(50F);

        final int registeredId_2 = this.registeredId(this.studentId("109235"));
        RegisteredResponseDtoDto dto2 = new RegisteredResponseDtoDto();
        dto2.setRegisteredId(registeredId_2);
        dto2.setStudentName("ALEX");
        dto2.setDocumentNumber("109235");
        dto2.setSubjectName("español2");
        dto2.setAverage(23F);
        dto2.setNota1(12F);
        dto2.setNota2(43F);
        dto2.setNota3(50F);

        final List<RegisteredResponseDtoDto> expected = new ArrayList<>();
        expected.add(dto1);
        expected.add(dto2);

        //When
        final ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        final String body = response.andReturn().getResponse().getContentAsString();
        final List actual = OBJECT_MAPPER.readValue(body, List.class);
        Assertions.assertEquals(expected, actual);
    }


}
