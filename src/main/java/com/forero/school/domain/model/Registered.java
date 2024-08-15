package com.forero.school.domain.model;

import com.forero.school.infraestructure.repository.entity.StudentEntity;
import com.forero.school.infraestructure.repository.entity.SubjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Registered {
    private Long id;

    private StudentEntity student;

    private SubjectEntity subject;

    private BigDecimal average;

    private BigDecimal nota1;

    private BigDecimal nota2;

    private BigDecimal nota3;
}
