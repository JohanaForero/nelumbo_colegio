package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.infraestructure.mapper.SubjectMapper;
import com.forero.school.openapi.api.SubjectsWithStudentsApi;
import com.forero.school.openapi.model.SubjectResponseDtoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class SubjectController implements SubjectsWithStudentsApi {
    private final RegisterUseCase registerUseCase;
    private final SubjectMapper subjectMapper;

    @Override
    public ResponseEntity<List<SubjectResponseDtoDto>> getAllSubjectsWithStudents() {
        final List<GeneralAggregate> resultDomain = this.registerUseCase.getAllSubjects();
        final List<SubjectResponseDtoDto> resultDto = this.subjectMapper.toModel(resultDomain);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }
}
