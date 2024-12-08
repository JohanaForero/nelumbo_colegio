package com.forero.school.infraestructure.mapper;

import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.infraestructure.repository.entity.StudentEntity;
import com.forero.school.openapi.model.StudentDtoDto;
import com.forero.school.openapi.model.SubjectResponseDtoDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SubjectMapper {
    @Mapping(target = "subjectName", source = "name")
    @Mapping(target = "subjectId", source = "subjectId")
    @Mapping(target = "students", source = "result")
    SubjectResponseDtoDto toDto(GeneralAggregate<StudentEntity> generalAggregate);

    //    @Mapping(target = "studentName", source = "firstName")
    @Mapping(target = "studentId", source = "id")
    StudentDtoDto toDto(StudentEntity studentEntity);

    @IterableMapping(elementTargetType = SubjectResponseDtoDto.class)
    List<SubjectResponseDtoDto> toModel(List<GeneralAggregate<StudentEntity>> resultDomain);
}
