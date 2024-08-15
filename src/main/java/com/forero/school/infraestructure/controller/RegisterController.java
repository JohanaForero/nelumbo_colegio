package com.forero.school.infraestructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${openapi.aPIDocumentation.base-path}")
public class RegisterController {


//    @Override
//    public ResponseEntity<Void> recordNotes(final Integer subjectId, MultipartFile file) {
//        this.registerUseCase.registerNotes(subjectId, file);
//        return UploadNotesApi.super.recordNotes(subjectId, file);
//    }
//}
}
