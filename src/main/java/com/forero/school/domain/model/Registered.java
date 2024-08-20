package com.forero.school.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Registered {
    private Long id;

    private Student student;

    private Subject subject;

    private BigDecimal average;

    private BigDecimal nota1;

    private BigDecimal nota2;

    private BigDecimal nota3;
}
