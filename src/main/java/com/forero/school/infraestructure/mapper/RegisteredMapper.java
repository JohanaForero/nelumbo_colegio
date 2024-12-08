package com.forero.school.infraestructure.mapper;

import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.repository.entity.RegisteredEntity;
import com.forero.school.openapi.model.RegisteredResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface RegisteredMapper {
    @Mapping(source = "student.documentNumber", target = "documentNumber")
    @Mapping(source = "student.firstName", target = "firstName")
    @Mapping(source = "student.secondName", target = "secondName")
    @Mapping(source = "student.surname", target = "surname")
    @Mapping(target = "nota3", source = "nota3")
    @Mapping(target = "nota2", source = "nota2")
    @Mapping(target = "nota1", source = "nota1")
    @Mapping(source = "student.secondSurname", target = "secondSurname")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "average", target = "average")
    RegisteredResponseDto toDtoToModel(Registered registered);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nota3", source = "nota3")
    @Mapping(target = "nota2", source = "nota2")
    @Mapping(target = "nota1", source = "nota1")
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "student.phone", ignore = true)
    @Mapping(target = "student.city", ignore = true)
    @Mapping(target = "student.address", ignore = true)
    @Mapping(target = "average", source = "average")
    @Mapping(target = "student.documentNumber", source = "student.documentNumber")
    @Mapping(target = "student.secondName", source = "student.secondName")
    @Mapping(target = "student.firstName", source = "student.firstName")
    @Mapping(target = "student.surname", source = "student.surname")
    @Mapping(target = "student.secondSurname", source = "student.secondSurname")
    @Mapping(target = "student.id", source = "student.id")
    Registered toEntityToModel(RegisteredEntity registeredEntity);

    List<RegisteredResponseDto> toDto(List<Registered> recordsDomain);
}
