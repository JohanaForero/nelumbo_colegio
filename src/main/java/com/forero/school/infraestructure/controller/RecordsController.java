package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.mapper.RegisteredMapper;
import com.forero.school.openapi.api.RegistrationsApi;
import com.forero.school.openapi.model.RegisteredResponseDtoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class RecordsController implements RegistrationsApi {
    private final RegisterUseCase registerUseCase;
    private final RegisteredMapper registeredMapper;

    @Override
    public ResponseEntity<List<RegisteredResponseDtoDto>> getAllRegistrations() {
        final List<Registered> recordsDomain = this.registerUseCase.getAllRecords();
        final List<RegisteredResponseDtoDto> recordsDto = this.registeredMapper.toDto(recordsDomain);
        return new ResponseEntity<>(recordsDto, HttpStatus.OK);
    }
}
