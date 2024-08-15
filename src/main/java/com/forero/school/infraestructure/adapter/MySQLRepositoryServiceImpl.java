package com.forero.school.infraestructure.adapter;

import com.forero.school.application.exception.RepositoryException;
import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.DataResultAgregate;
import com.forero.school.domain.exception.CodeException;
import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.mapper.RegisteredMapper;
import com.forero.school.infraestructure.repository.RegisteredRepository;
import com.forero.school.infraestructure.repository.StudentRepository;
import com.forero.school.infraestructure.repository.SubjectRepository;
import com.forero.school.infraestructure.repository.entity.RegisteredEntity;
import com.forero.school.infraestructure.repository.entity.StudentEntity;
import com.forero.school.infraestructure.repository.entity.SubjectEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MySQLRepositoryServiceImpl implements RepositoryService {
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final RegisteredRepository registeredRepository;
    private final RegisteredMapper registeredMapper;

    @Override
    public void saveNotes(@NonNull final Integer subjectId, @NonNull final MultipartFile file) {
        final SubjectEntity subject = subjectRepository.findById(Long.valueOf(subjectId)).orElse(null);
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String studentIdentification = row.getCell(1).getStringCellValue();
                int nota1 = (int) row.getCell(2).getNumericCellValue();
                int nota2 = (int) row.getCell(3).getNumericCellValue();
                int nota3 = (int) row.getCell(4).getNumericCellValue();

                final StudentEntity student = studentRepository.findByDocumentNumber(studentIdentification)
                        .orElse(null);
                if (student != null && subject != null) {
                    BigDecimal average = BigDecimal.valueOf((nota1 + nota2 + nota3) / 3.0);

                    RegisteredEntity registeredEntity = new RegisteredEntity();
//                    registeredEntity.setStudent(student);
//                    registeredEntity.setSubject(subject);
//                    registeredEntity.setNota1((() nota1));
//                    registeredEntity.setNota2(nota2);
//                    registeredEntity.setNota3(nota3);
//                    registeredEntity.setAverage(average);

                    registeredRepository.save(registeredEntity);
                }
            }
        } catch (IOException ioException) {
            throw new RepositoryException(CodeException.INVALID_PARAMETERS, null, subject.getName());
        }
    }

    @Override
    public void validateIfSubjectExists(int subjectId) {
        final boolean existSubjectId = this.subjectRepository.existsById((long) subjectId);
        if (!existSubjectId) {
            throw new RepositoryException(CodeException.SUBJECT_NOT_FOUND, null);
        }
    }

    @Override
    public List<DataResultAgregate> getAllSubjectAndStudents() {
        List<DataResultAgregate> result = new ArrayList<>();
        List<SubjectEntity> subject = this.subjectRepository.findAll();
        for (final SubjectEntity subject1 : subject) {
            final DataResultAgregate dataResultAgregate = new DataResultAgregate();
            List<StudentEntity> students = this.getStudentsBySubjectId(subject1.getId().intValue());
            dataResultAgregate.setResult(students);
            dataResultAgregate.setSubjectId(subject1.getId().intValue());
            dataResultAgregate.setName(subject1.getName());
            result.add(dataResultAgregate);

        }
        return result;
    }

    public List<StudentEntity> getStudentsBySubjectId(int subjectId) {
        return registeredRepository.findBySubjectId(subjectId)
                .stream()
                .map(RegisteredEntity::getStudent)
                .toList();
    }

    @Override
    public List<Registered> getAllRegistered() {
        List<RegisteredEntity> registeredEntityList = this.registeredRepository.findAll();
        return registeredEntityList
                .stream()
                .map(this.registeredMapper::toModel)
                .toList();
    }
}

