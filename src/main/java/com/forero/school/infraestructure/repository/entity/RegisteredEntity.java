package com.forero.school.infraestructure.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "registered")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RegisteredEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;
    @NotNull
    @Column(name = "average")
    private BigDecimal average;
    @NotNull
    @Column(name = "nota1")
    private BigDecimal nota1;
    @NotNull
    @Column(name = "nota2")
    private BigDecimal nota2;
    @NotNull
    @Column(name = "nota3")
    private BigDecimal nota3;
}
