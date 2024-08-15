package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.infraestructure.mapper.RegisteredMapper;
import com.forero.school.openapi.api.RegisteredApi;
import com.forero.school.openapi.model.GenerateAllRegisteredPdf200ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class GetRegisteredPdfController implements RegisteredApi {
    private final RegisterUseCase registerUseCase;
    private final RegisteredMapper registeredMapper;

    @Override
    public ResponseEntity<GenerateAllRegisteredPdf200ResponseDto> generateAllRegisteredPdf() throws IOException {
        String pdfBytes = this.registerUseCase.generatePdf();
        GenerateAllRegisteredPdf200ResponseDto pdf = new GenerateAllRegisteredPdf200ResponseDto();
        pdf.setPdfBase64(pdfBytes);
        return new ResponseEntity<>(pdf, HttpStatus.OK);
    }

}
