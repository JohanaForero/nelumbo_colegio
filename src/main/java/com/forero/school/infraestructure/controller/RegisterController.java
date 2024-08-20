package com.forero.school.infraestructure.controller;

import com.forero.school.application.usecase.RegisterUseCase;
import com.forero.school.openapi.api.NotesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class RegisterController implements NotesApi {
    private final RegisterUseCase registerUseCase;

    @Override
    public ResponseEntity<Void> recordNotes(final Integer subjectId, final List<MultipartFile> files) {
        this.registerUseCase.registerNotes(subjectId, files);
        return ResponseEntity.noContent().build();
    }
}