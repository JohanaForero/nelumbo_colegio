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
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MySQLRepositoryServiceImpl implements RepositoryService {
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final RegisteredRepository registeredRepository;
    private final RegisteredMapper registeredMapper;

    @Override
    public void validateIfSubjectExists(int subjectId) {
        final boolean existSubjectId = this.subjectRepository.existsById((long) subjectId);
        if (!existSubjectId) {
            throw new RepositoryException(CodeException.SUBJECT_NOT_FOUND, null);
        }
    }

    @Override
    public List<DataResultAgregate> getAllSubjectAndStudents() {
        final List<DataResultAgregate> result = new ArrayList<>();
        final List<SubjectEntity> subject = this.subjectRepository.findAll();
        for (final SubjectEntity subject1 : subject) {
            final DataResultAgregate dataResultAgregate = new DataResultAgregate();
            final List<StudentEntity> students = this.getStudentsBySubjectId(subject1.getId().intValue());
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
        if (registeredEntityList.isEmpty()) {
            throw new RepositoryException(CodeException.EMPTY_LIST, null, "records");
        }
        return registeredEntityList
                .stream()
                .map(this.registeredMapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public void uploadGrades(@NonNull final MultipartFile file, @NonNull final Long idSubject) throws IOException {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            if (iterator.hasNext()) {
                iterator.next(); // Skip header row
            }

            Set<String> studentIdentifications = new HashSet<>();
            Map<String, RegisteredEntity> existingRegistrations = new HashMap<>();

            // Load existing registrations into a map
            List<RegisteredEntity> registrations = registeredRepository.findAllBySubjectId(Math.toIntExact(idSubject));
            for (RegisteredEntity registration : registrations) {
                existingRegistrations.put(
                        registration.getStudent().getDocumentNumber() + "_" + idSubject, registration);
            }

            while (iterator.hasNext()) {
                Row row = iterator.next();
                String studentIdentification = "";
                Cell cell = row.getCell(0);

                if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            studentIdentification = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            studentIdentification = String.valueOf((long) cell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                }

                if (studentIdentifications.contains(studentIdentification)) {
                    throw new RepositoryException(CodeException.DUPLICATE_STUDENT_IN_EXCEL, null, studentIdentification);
                }

                studentIdentifications.add(studentIdentification);

                BigDecimal nota1 = getBigDecimalFromCell(row.getCell(1));
                BigDecimal nota2 = getBigDecimalFromCell(row.getCell(2));
                BigDecimal nota3 = getBigDecimalFromCell(row.getCell(3));
                String finalStudentIdentification = studentIdentification;
                StudentEntity studentEntity = this.studentRepository.findByDocumentNumber(studentIdentification)
                        .orElseThrow(() -> new RepositoryException(CodeException.STUDENT_NOT_FOUND, null, finalStudentIdentification));

                SubjectEntity subjectEntity = this.subjectRepository.findById(idSubject).orElse(null);
                if (subjectEntity == null) {
                    throw new RepositoryException(CodeException.SUBJECT_NOT_FOUND, null, idSubject.toString());
                }


                BigDecimal sumOfNotes = nota1.add(nota2).add(nota3);
                BigDecimal totalNotes = BigDecimal.valueOf(3);
                BigDecimal average = sumOfNotes.divide(totalNotes, 2, RoundingMode.HALF_EVEN);
                RegisteredEntity existingRegistration =
                        existingRegistrations.get(studentIdentification + "_" + idSubject);

                if (existingRegistration != null) {
                    existingRegistration.setNota1(nota1);
                    existingRegistration.setNota2(nota2);
                    existingRegistration.setNota3(nota3);
                    existingRegistration.setAverage(average);
                    registeredRepository.save(existingRegistration);
                } else {
                    RegisteredEntity registeredEntity = new RegisteredEntity();
                    registeredEntity.setStudent(studentEntity);
                    registeredEntity.setSubject(subjectEntity);
                    registeredEntity.setAverage(average);
                    registeredEntity.setNota1(nota1);
                    registeredEntity.setNota2(nota2);
                    registeredEntity.setNota3(nota3);
                    registeredRepository.save(registeredEntity);
                }
            }
        }
    }


    public BigDecimal getBigDecimalFromCell(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return null;
        }
        return BigDecimal.valueOf(cell.getNumericCellValue());
    }

    @Override
    public byte[] generatePdf() {
        List<RegisteredEntity> registeredEntityList = this.registeredRepository.findAll();
        if (registeredEntityList.isEmpty()) {
            throw new RepositoryException(CodeException.EMPTY_LIST, null, "records");
        }
        List<Registered> registeredList = registeredEntityList
                .stream()
                .map(this.registeredMapper::toModel)
                .toList();

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            try {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(220, 750);
                contentStream.showText("Lista de Registros");
                contentStream.endText();

                float marginLeft = 50;
                float marginTop = 700;
                float rowHeight = 20;
                float tableWidth = 500;
                float cellMargin = 5;

                String[] headers = {"Registros", "Promedio", "Documento", "Nombre", "Nota 1", "Nota 2", "Nota 3",
                        "Materia"};

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, marginTop);

                for (String header : headers) {
                    contentStream.showText(header);
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);
                }

                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                marginTop -= rowHeight;

                for (Registered registered : registeredList) {
                    if (marginTop < 50) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        marginTop = 700;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(marginLeft, marginTop);

                    contentStream.showText(String.valueOf(registered.getId()));
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(String.valueOf(registered.getAverage()));
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(registered.getStudent().getDocumentNumber());
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(registered.getStudent().getFirstName());
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(String.valueOf(registered.getNota1()));
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(String.valueOf(registered.getNota2()));
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(String.valueOf(registered.getNota3()));
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);

                    contentStream.showText(registered.getSubject().getName());

                    contentStream.endText();
                    marginTop -= rowHeight;
                }

            } finally {
                contentStream.close();
            }

            document.save(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RepositoryException(CodeException.PDF_GENERATION_ERROR, null);
        }
    }
}
