package com.forero.school.infraestructure.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.forero.school.BaseIT;
import com.forero.school.openapi.model.StudentDtoDto;
import com.forero.school.openapi.model.SubjectResponseDtoDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class GetAllSubjectsWithStudentsIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/school/subjects_students";

    private int studentId(final String documentNumber) {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM student WHERE document_number = ?", Integer.class, documentNumber);
        return id != null ? id : 0;
    }

    private int subjectId(final String name) {
        final Integer id = this.jdbcTemplate.queryForObject("SELECT id FROM subject WHERE name = ?", Integer.class, name);
        return id != null ? id : 0;
    }

    @Test
    void test_getAllSubjectsWithStudents_withRequestValid_shouldReturnListOfRegisteredResponseDtos() throws Exception {
        //Given
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size) VALUES (?, ?)", "ingles", 3);
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size) VALUES (?, ?)", "matematicas", 3);

        this.jdbcTemplate.update("INSERT INTO student (address, city, document_number, first_name, phone, " +
                        "second_name, second_surname, surname) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "VELLA-VISTA", "BOGOTA", "109234", "Valentina", "3214555", "Alvares", "lopex", "Sara");

        this.jdbcTemplate.update("INSERT INTO registered (student_id, subject_id, average, nota1, nota2, nota3) VALUES (?, ?, ?, ?, ?, ?)",
                this.studentId("109234"),
                this.subjectId("ingles"),
                new BigDecimal("12.0"),
                new BigDecimal("43.0"),
                new BigDecimal("50.0"),
                new BigDecimal("60.0")
        );

        this.jdbcTemplate.update("INSERT INTO student (address, city, document_number, first_name, phone, " +
                        "second_name, second_surname, surname) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "VELLA-VISTA", "BOGOTA", "109236", "laura", "3214555", "Alvares", "lopex", "Marly");

        this.jdbcTemplate.update("INSERT INTO registered (student_id, subject_id, average, nota1, nota2, nota3) VALUES (?, ?, ?, ?, ?, ?)",
                this.studentId("109236"),
                this.subjectId("matematicas"),
                new BigDecimal("12.0"),
                new BigDecimal("43.0"),
                new BigDecimal("50.0"),
                new BigDecimal("60.0")
        );

        final StudentDtoDto studentDto1 = new StudentDtoDto();
        studentDto1.setStudentId(this.studentId("109234"));
        studentDto1.setFirstName("Valentina");
        studentDto1.secondName("Alvares");
        studentDto1.setSurname("Sara");
        studentDto1.setSecondSurname("lopex");
        studentDto1.setDocumentNumber("109234");
        final List<StudentDtoDto> student1 = new ArrayList<>();
        student1.add(studentDto1);
        final SubjectResponseDtoDto dto1 = new SubjectResponseDtoDto();
        dto1.setSubjectId(this.subjectId("ingles"));
        dto1.setStudents(student1);
        dto1.setSubjectName("ingles");

        final StudentDtoDto studentDto2 = new StudentDtoDto();
        studentDto2.setStudentId(this.studentId("109236"));
        studentDto2.setFirstName("laura");
        studentDto2.secondName("Alvares");
        studentDto2.setSecondSurname("lopex");
        studentDto2.setSurname("Marly");
        studentDto2.setDocumentNumber("109236");
        final List<StudentDtoDto> student2 = new ArrayList<>();
        student2.add(studentDto2);
        final SubjectResponseDtoDto dto2 = new SubjectResponseDtoDto();
        dto2.setSubjectId(this.subjectId("matematicas"));
        dto2.setStudents(student2);
        dto2.setSubjectName("matematicas");

        final List<SubjectResponseDtoDto> expected = new ArrayList<>();
        expected.add(dto1);
        expected.add(dto2);

        //When
        final ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        final String body = response.andReturn().getResponse().getContentAsString();
        final List<SubjectResponseDtoDto> actual = OBJECT_MAPPER.readValue(body, new TypeReference<List<SubjectResponseDtoDto>>() {
        });
        Assertions.assertEquals(expected, actual);
    }
}
