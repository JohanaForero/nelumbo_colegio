package com.forero.school.RegisterController;

import com.forero.school.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class RecordNotesIntegrationTest extends BaseIT {
    private static final String BASE_PATH = "/school/";

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

    @Test
    void test_RegisterController_withRequestValid_shouldStatusOk() throws Exception {
        //Given
        this.jdbcTemplate.update("INSERT INTO subject (name, notes_size)", " VALUES (?, ?)", "matematicas", 3);
        this.jdbcTemplate.update("INSERT INTO student (address, city, document_number, first_name, phone, " +
                        "second_name, second_surname, surname)", " VALUES (?, ?, ?, ?, ?, ?, ?, ?)", "VELLA-VISTA", "BOGOTA",
                "109234", "ALEX", "3214555", "Alvares", "lopex", "seba");
        final int studentId = this.studentId("109234");
        final int subjectId = this.subjectId("matematicas");
        this.jdbcTemplate.update("INSERT INTO student (student_id, subject_id, average, nota1, nota2, " +
                "nota3)", " VALUES (?, ?, ?, ?, ?, ?)", studentId, subjectId, 12, 43, 50);

        //When
        final ResultActions response = this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_PATH + studentId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }


}
