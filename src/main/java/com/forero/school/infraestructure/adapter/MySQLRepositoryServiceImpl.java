package com.forero.school.infraestructure.adapter;

import com.forero.school.application.exception.RepositoryException;
import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.exception.CodeException;
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

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MySQLRepositoryServiceImpl implements RepositoryService {
    private static SubjectRepository subjectRepository;

    @Override
    public void saveNotes(@NonNull final Integer subjectId, @NonNull final MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Obtiene la primera hoja

            // Iterar sobre las filas
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Salta la primera fila si es el encabezado
                    continue;
                }

                // Lee los datos de la fila
                String studentName = row.getCell(0).getStringCellValue();
                String studentIdentification = row.getCell(1).getStringCellValue();
                int nota1 = (int) row.getCell(2).getNumericCellValue();
                int nota2 = (int) row.getCell(3).getNumericCellValue();
                int nota3 = (int) row.getCell(4).getNumericCellValue();

                // Busca el estudiante por identificación
                Optional<StudentEntity> studentOpt = studentRepository.findByIdentification(studentIdentification);
                if (!studentOpt.isPresent()) {
                    throw new RuntimeException("Student with identification " + studentIdentification + " not found");
                }

                StudentEntity student = studentOpt.get();

                // Verifica que el ID de la materia sea válido
                if (!subjectRepository.existsById(subjectId)) {
                    throw new RuntimeException("Subject with ID " + subjectId + " not found");
                }

                // Calcula el promedio
                BigDecimal average = BigDecimal.valueOf((nota1 + nota2 + nota3) / 3.0);

                // Crea y guarda la entidad RegisteredEntity
                RegisteredEntity registeredEntity = new RegisteredEntity();
                registeredEntity.setStudent(student);
                registeredEntity.setSubject(new SubjectEntity(subjectId)); // Se asume que existe un constructor que recibe el ID
                registeredEntity.setNota1(nota1);
                registeredEntity.setNota2(nota2);
                registeredEntity.setNota3(nota3);
                registeredEntity.setAverage(average);

                registeredRepository.save(registeredEntity);
            }
        }
    }

    @Override
    public void validateIfSubjectExists(int subjectId) {
        final long idSubject = subjectId;
        final boolean existSubjectId = this.subjectRepository.existsById(idSubject);
        if (!existSubjectId) {
            throw new RepositoryException(CodeException.SUBJECT_NOT_FOUND);
        }
    }
}

