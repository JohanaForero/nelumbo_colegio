package com.forero.school.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
    private Long id;
    private String firstName;
    private String secondName;
    private String phone;
    private String city;
    private String address;
    private String surname;
    private String secondSurname;
    private String documentNumber;
}
