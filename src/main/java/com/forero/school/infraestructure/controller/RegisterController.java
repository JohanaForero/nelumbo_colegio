package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.openapi.api.UploadNotesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class RegisterController implements UploadNotesApi {
    private final RegisterUseCase registerUseCase;

    @Override
    public ResponseEntity<Void> recordNotes(final Integer subjectId, final MultipartFile file) {
        try {
            this.registerUseCase.registerNotes(subjectId, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

}