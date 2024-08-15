package com.forero.school.infraestructure.mapper;

import com.forero.school.domain.agregate.DataResultAgregate;
import com.forero.school.domain.model.Student;
import com.forero.school.infraestructure.repository.entity.StudentEntity;
import com.forero.school.openapi.model.StudentDtoDto;
import com.forero.school.openapi.model.SubjectResponseDtoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SubjectMapper {

    StudentEntity toEntity(Student student);

    @Mapping(source = "firstName", target = "studentName")
    @Mapping(source = "id", target = "studentId")
    StudentDtoDto toDto(Student student);

    @Mapping(target = "subjectName", source = "name")
    @Mapping(target = "subjectId", source = "subjectId")
    @Mapping(target = "students", source = "result")
    SubjectResponseDtoDto toDto(DataResultAgregate dataResultAgregate);

    @Mapping(target = "subjectName", qualifiedByName = "name")
    @Mapping(target = "subjectId", qualifiedByName = "subjectId")
    @Mapping(target = "students", qualifiedByName = "result")
    List<SubjectResponseDtoDto> toModel(List<DataResultAgregate> resultDomain);
}
