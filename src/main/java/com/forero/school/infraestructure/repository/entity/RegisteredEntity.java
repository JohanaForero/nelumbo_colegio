package com.forero.school.infraestructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
//@Getter
//@Entity
//@Table(name = "registered")
public class RegisteredEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @Column(name = "average")
    private BigDecimal average;

    @Column(name = "nota1")
    private int nota1;

    @Column(name = "nota2")
    private int nota2;

    @Column(name = "nota3")
    private int nota3;
}
