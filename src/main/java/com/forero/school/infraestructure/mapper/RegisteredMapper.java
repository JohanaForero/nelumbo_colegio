package com.forero.school.infraestructure.mapper;

import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.repository.entity.RegisteredEntity;
import com.forero.school.openapi.model.RegisteredResponseDtoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface RegisteredMapper {

    @Mapping(target = "average", source = "average")
    @Mapping(target = "student.documentNumber", source = "student.documentNumber")
    @Mapping(target = "subject.name", source = "subject.name")
    @Mapping(target = "student.firstName", source = "student.firstName")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nota2", source = "nota2")
    @Mapping(target = "nota1", source = "nota1")
    @Mapping(target = "nota3", source = "nota3")
    @Mapping(target = "student.city", ignore = true)
    @Mapping(target = "student.phone", ignore = true)
    @Mapping(target = "student.surname", ignore = true)
    @Mapping(target = "student.secondSurname", ignore = true)
    @Mapping(target = "subject.id", ignore = true)
    @Mapping(target = "subject.notesSize", ignore = true)
    Registered toModel(RegisteredEntity registeredEntity);

    @Mapping(target = "nota3", source = "nota3")
    @Mapping(target = "nota2", source = "nota2")
    @Mapping(target = "nota1", source = "nota1")
    @Mapping(target = "average", source = "average")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "documentNumber", source = "student.documentNumber")
    @Mapping(target = "studentName", source = "student.firstName")
    @Mapping(target = "registeredId", source = "id")
    RegisteredResponseDtoDto toDtoToModel(Registered registered);

    List<RegisteredResponseDtoDto> toDto(List<Registered> recordsDomain);
}
