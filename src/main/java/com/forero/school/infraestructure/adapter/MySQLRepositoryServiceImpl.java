package com.forero.school.infraestructure.adapter;

import com.forero.school.application.exception.RepositoryException;
import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.GeneralAggregate;
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
    public void validateIfSubjectExists(final int subjectId) {
        final boolean existSubjectId = this.subjectRepository.existsById((long) subjectId);
        if (!existSubjectId) {
            throw new RepositoryException(CodeException.SUBJECT_NOT_FOUND, null);
        }
    }

    @Override
    public List<GeneralAggregate> getAllSubjectAndStudents() {
        final List<GeneralAggregate> result = new ArrayList<>();
        final List<SubjectEntity> subject = this.subjectRepository.findAll();
        for (final SubjectEntity subject1 : subject) {
            final GeneralAggregate generalAggregate = new GeneralAggregate();
            final List<StudentEntity> students = this.getStudentsBySubjectId(subject1.getId().intValue());
            generalAggregate.setResult(students);
            generalAggregate.setSubjectId(subject1.getId().intValue());
            generalAggregate.setName(subject1.getName());
            result.add(generalAggregate);

        }
        return result;
    }

    public List<StudentEntity> getStudentsBySubjectId(final int subjectId) {
        return this.registeredRepository.findBySubjectId(subjectId)
                .stream()
                .map(RegisteredEntity::getStudent)
                .toList();
    }

    @Override
    public List<Registered> getAllRegistered() {
        final List<RegisteredEntity> registeredEntityList = this.registeredRepository.findAll();
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
    public void uploadGrades(final MultipartFile file, final int idSubject) {
        try (final InputStream is = file.getInputStream();
             final Workbook workbook = new XSSFWorkbook(is)) {

            final Sheet sheet = workbook.getSheetAt(0);
            final Iterator<Row> iterator = sheet.iterator();

            if (iterator.hasNext()) {
                iterator.next();
            }

            final Set<String> studentIdentifications = new HashSet<>();
            final Map<String, RegisteredEntity> existingRegistrations = new HashMap<>();

            final List<RegisteredEntity> registrations = this.registeredRepository.findAllBySubjectId(idSubject);
            for (final RegisteredEntity registration : registrations) {
                existingRegistrations.put(
                        registration.getStudent().getDocumentNumber() + "_" + idSubject, registration);
            }

            while (iterator.hasNext()) {
                final Row row = iterator.next();
                String studentIdentification = "";
                final Cell cell = row.getCell(0);

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

                final BigDecimal noteOne = this.getBigDecimalFromCell(row.getCell(1));
                final BigDecimal noteTwo = this.getBigDecimalFromCell(row.getCell(2));
                final BigDecimal noteThree = this.getBigDecimalFromCell(row.getCell(3));
                final String finalStudentIdentification = studentIdentification;
                final StudentEntity studentEntity = this.getSubject(finalStudentIdentification);

                final SubjectEntity subjectEntity = this.getSubject(idSubject);
                final BigDecimal average = this.calculateAverage(noteOne, noteTwo, noteThree);
                final RegisteredEntity existingRegistration =
                        existingRegistrations.get(studentIdentification + "_" + idSubject);

                if (existingRegistration != null) {
                    existingRegistration.setNota1(noteOne);
                    existingRegistration.setNota2(noteTwo);
                    existingRegistration.setNota3(noteThree);
                    existingRegistration.setAverage(average);
                    this.registeredRepository.save(existingRegistration);
                } else {
                    final RegisteredEntity registeredEntity = new RegisteredEntity();
                    registeredEntity.setStudent(studentEntity);
                    registeredEntity.setSubject(subjectEntity);
                    registeredEntity.setAverage(average);
                    registeredEntity.setNota1(noteOne);
                    registeredEntity.setNota2(noteTwo);
                    registeredEntity.setNota3(noteThree);
                    this.registeredRepository.save(registeredEntity);
                }
            }
        } catch (final IOException e) {
            throw new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null);
        }
    }


    private SubjectEntity getSubject(final int subjectId) {
        final SubjectEntity subjectEntity = this.subjectRepository.findById((long) subjectId).orElse(null);
        if (subjectEntity == null) {
            throw new RepositoryException(CodeException.SUBJECT_NOT_FOUND, null, String.valueOf(subjectId));
        }
        return subjectEntity;
    }

    private StudentEntity getSubject(final String studentIdentification) {
        return this.studentRepository.findByDocumentNumber(studentIdentification)
                .orElseThrow(() -> new RepositoryException(CodeException.STUDENT_NOT_FOUND, null, studentIdentification));
    }

    private BigDecimal calculateAverage(final BigDecimal noteOne, final BigDecimal noteTwo, final BigDecimal noteThree) {
        final BigDecimal summationNotes = noteOne.add(noteTwo).add(noteThree);
        final BigDecimal totalNotes = BigDecimal.valueOf(3);
        return summationNotes.divide(totalNotes, 2, RoundingMode.HALF_EVEN);
    }


    public BigDecimal getBigDecimalFromCell(final Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return null;
        }
        return BigDecimal.valueOf(cell.getNumericCellValue());
    }

    @Override
    public byte[] generatePdf() {
        final List<RegisteredEntity> registeredEntityList = this.registeredRepository.findAll();
        if (registeredEntityList.isEmpty()) {
            throw new RepositoryException(CodeException.EMPTY_LIST, null, "records");
        }
        final List<Registered> registeredList = registeredEntityList
                .stream()
                .map(this.registeredMapper::toModel)
                .toList();

        try (final PDDocument document = new PDDocument(); final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            try {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(220, 750);
                contentStream.showText("Lista de Registros");
                contentStream.endText();

                final float marginLeft = 50;
                float marginTop = 700;
                final float rowHeight = 20;
                final float tableWidth = 500;

                final String[] headers = {"Registros", "Promedio", "Documento", "Nombre", "Nota 1", "Nota 2", "Nota 3",
                        "Materia"};

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, marginTop);

                for (final String header : headers) {
                    contentStream.showText(header);
                    contentStream.newLineAtOffset(tableWidth / headers.length, 0);
                }

                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                marginTop -= rowHeight;

                for (final Registered registered : registeredList) {
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

        } catch (final IOException e) {
            throw new RepositoryException(CodeException.PDF_GENERATION_ERROR, null);
        }
    }
}
