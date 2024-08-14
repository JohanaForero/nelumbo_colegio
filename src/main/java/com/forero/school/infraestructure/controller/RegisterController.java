package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.openapi.api.UploadNotesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class RegisterController implements UploadNotesApi {
    private RegisterUseCase registerUseCase;

    @Override
    public ResponseEntity<Void> recordNotes(final Integer subjectId, MultipartFile file) {
        this.registerUseCase.registerNotes(subjectId, file);
        return UploadNotesApi.super.recordNotes(subjectId, file);
    }
}
