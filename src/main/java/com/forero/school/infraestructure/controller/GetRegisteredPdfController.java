package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.openapi.api.RegisteredApi;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class GetRegisteredPdfController implements RegisteredApi {
    private final RegisterUseCase registerUseCase;

    @Override
    public ResponseEntity<Resource> generateAllRegisteredPdf() {
        byte[] pdfBytes = this.registerUseCase.generarPdf();
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("mi_informe.pdf").build());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}
