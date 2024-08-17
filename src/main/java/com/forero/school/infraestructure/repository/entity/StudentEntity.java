package com.forero.school.infraestructure.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "student",
        indexes = {
                @Index(name = "document_number_index", columnList = "document_number", unique = true)
        }
)
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "first_name")
    private String firstName;
    @NotNull
    @Column(name = "second_name")
    private String secondName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;
    @NotNull
    @Column(name = "surname")
    private String surname;
    @NotNull
    @Column(name = "second_surname")
    private String secondSurname;

    @NotNull
    @Column(name = "document_number", unique = true)
    private String documentNumber;
}
