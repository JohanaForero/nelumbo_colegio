package com.forero.school.infraestructure.adapter;

import com.forero.school.application.exception.RepositoryException;
import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.GeneralAggregate;
import com.forero.school.domain.exception.CodeException;
import com.forero.school.domain.model.Registered;
import com.forero.school.infraestructure.mapper.RegisteredMapper;
import com.forero.school.infraestructure.repository.RegisteredRepository;
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
    public List<GeneralAggregate<StudentEntity>> getAllSubjectAndStudents() {
        final List<GeneralAggregate<StudentEntity>> result = new ArrayList<>();
        final List<SubjectEntity> subjects = this.subjectRepository.findAll();
        this.validateSubjectEntityList(subjects);
        for (final SubjectEntity subject : subjects) {
            final GeneralAggregate<StudentEntity> generalAggregate = new GeneralAggregate<>();
            final List<StudentEntity> students = this.getStudentsBySubjectId(subject.getId().intValue());
            generalAggregate.setResult(students);
            generalAggregate.setSubjectId(subject.getId().intValue());
            generalAggregate.setName(subject.getName());
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
    public List<Registered> getAllRegistered(final int subjectId) {
        final List<RegisteredEntity> registeredEntityList =
                this.registeredRepository.findAllBySubjectIdOrderByAverageDesc((long) subjectId);
        this.validateRegisteredEntityList(registeredEntityList);
        return registeredEntityList
                .stream()
                .map(this.registeredMapper::toEntityToModel)
                .toList();
    }

    @Override
    @Transactional
    public void uploadGrades(final MultipartFile[] files, final int idSubject) {
        final MultipartFile file = files[ConstantsRepository.MINI_VALOR];
        try (final InputStream is = file.getInputStream();
             final Workbook workbook = new XSSFWorkbook(is)) {

            final Sheet sheet = workbook.getSheetAt(ConstantsRepository.MINI_VALOR);
            final Iterator<Row> iterator = sheet.iterator();

            if (!iterator.hasNext()) {
                throw new RepositoryException(CodeException.INVALID_PARAMETERS, null,
                        ConstantsRepository.FILE);
            }

            iterator.next();

            final Set<String> studentIdentifications = new HashSet<>();
            final Map<String, RegisteredEntity> existingRegistrations = new HashMap<>();

            final List<RegisteredEntity> registrations = this.registeredRepository.findAllBySubjectId(idSubject);
            if (registrations.isEmpty()) {
                throw new RepositoryException(CodeException.EMPTY_LIST, null, ConstantsRepository.RECORDS);
            }
            for (final RegisteredEntity registration : registrations) {
                final String key = registration.getStudent().getDocumentNumber()
                        + ConstantsRepository.CONCATENATE + idSubject;
                existingRegistrations.put(key, registration);
            }

            while (iterator.hasNext()) {
                final Row row = iterator.next();
                final String studentIdentification = this.getStudentIdentification(
                        row.getCell(ConstantsRepository.MINI_VALOR));

                if (studentIdentifications.contains(studentIdentification)) {
                    throw new RepositoryException(CodeException.DUPLICATE_STUDENT_IN_EXCEL, null,
                            studentIdentification);
                }

                studentIdentifications.add(studentIdentification);

                final BigDecimal noteOne = this.getBigDecimalFromCell(row.getCell(
                        ConstantsRepository.NOTE_ONE_CELL_INDEX));
                final BigDecimal noteTwo = this.getBigDecimalFromCell(row.getCell(
                        ConstantsRepository.NOTE_TWO_CELL_INDEX));
                final BigDecimal noteThree = this.getBigDecimalFromCell(row.getCell(
                        ConstantsRepository.NOTE_THREE_CELL_INDEX));
                this.validateNotes(noteOne, noteTwo, noteThree);

                final RegisteredEntity existingRegistration = existingRegistrations.get(
                        studentIdentification + ConstantsRepository.CONCATENATE + idSubject);

                if (existingRegistration != null) {
                    final BigDecimal average = this.calculateAverage(noteOne, noteTwo, noteThree);
                    existingRegistration.setNota1(noteOne);
                    existingRegistration.setNota2(noteTwo);
                    existingRegistration.setNota3(noteThree);
                    existingRegistration.setAverage(average);
                    this.registeredRepository.save(existingRegistration);
                } else {
                    throw new RepositoryException(CodeException.INVALID_PARAMETERS, null,
                            ConstantsRepository.REGISTERED);
                }
            }
        } catch (final IOException e) {
            throw new RepositoryException(CodeException.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    private String getStudentIdentification(final Cell cell) {
        if (cell == null) return ConstantsRepository.DEFAULT_VALUE;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> ConstantsRepository.DEFAULT_VALUE;
        };
    }

    private void validateNotes(final BigDecimal noteOne, final BigDecimal noteTwo, final BigDecimal noteThree) {
        this.validateSingleNote(noteOne);
        this.validateSingleNote(noteTwo);
        this.validateSingleNote(noteThree);
    }

    private void validateSingleNote(final BigDecimal note) {
        if (note == null) {
            throw new RepositoryException(CodeException.INVALID_PARAMETERS, null, ConstantsRepository.NOTE);
        }

        if (note.compareTo(BigDecimal.ONE) < ConstantsRepository.MINI_VALOR ||
                note.compareTo(ConstantsRepository.MAX_NOTE) > ConstantsRepository.MINI_VALOR) {
            throw new RepositoryException(CodeException.INVALID_NOTE, null);
        }
    }

    private BigDecimal calculateAverage(final BigDecimal noteOne, final BigDecimal noteTwo, final BigDecimal noteThree) {
        final BigDecimal summationNotes = noteOne.add(noteTwo).add(noteThree);
        final BigDecimal totalNotes = ConstantsRepository.TOTAL_NOTES;
        return summationNotes.divide(totalNotes, ConstantsRepository.DECIMAL_PRECISION, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getBigDecimalFromCell(final Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            throw new RepositoryException(CodeException.INVALID_PARAMETERS, null, ConstantsRepository.NOTE);
        }

        final BigDecimal note = BigDecimal.valueOf(cell.getNumericCellValue());
        if (note.compareTo(BigDecimal.ONE) < ConstantsRepository.MINI_VALOR || note.compareTo(
                ConstantsRepository.MAX_NOTE) > ConstantsRepository.MINI_VALOR) {
            throw new RepositoryException(CodeException.INVALID_NOTE, null);
        }

        return note;
    }

    @Override
    public byte[] generatePdf(final int subjectId) {
        final List<RegisteredEntity> registeredEntityList =
                this.registeredRepository.findAllBySubjectIdOrderByAverageDesc((long) subjectId);
        this.validateRegisteredEntityList(registeredEntityList);
        try (final PDDocument document = new PDDocument();
             final ByteArrayOutputStream bass = new ByteArrayOutputStream()) {
            this.createPdfContent(document, registeredEntityList);
            document.save(bass);
            return bass.toByteArray();
        } catch (final IOException e) {
            throw new RepositoryException(CodeException.PDF_GENERATION_ERROR, null);
        }
    }

    private void validateRegisteredEntityList(final List<RegisteredEntity> registeredEntityList) {
        if (registeredEntityList.isEmpty()) {
            throw new RepositoryException(CodeException.EMPTY_LIST, null, ConstantsRepository.RECORDS);
        }
    }

    private void validateSubjectEntityList(final List<SubjectEntity> subjectEntities) {
        if (subjectEntities.isEmpty()) {
            throw new RepositoryException(CodeException.EMPTY_LIST, null, ConstantsRepository.SUBJECTS);
        }
    }

    private void createPdfContent(final PDDocument document, final List<RegisteredEntity> registeredEntityList)
            throws IOException {
        final PDPage page = new PDPage();
        document.addPage(page);

        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            this.createTitle(contentStream);
            this.createHeader(contentStream);
            this.createBody(document, contentStream, registeredEntityList);
        }
    }

    private void createTitle(final PDPageContentStream contentStream) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, ConstantsRepository.TITLE_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(ConstantsRepository.TITLE_X_OFFSET, ConstantsRepository.TITLE_Y_OFFSET);
        contentStream.showText(ConstantsRepository.TITLE);
        contentStream.endText();
    }

    private void createHeader(final PDPageContentStream contentStream) throws IOException {
        final float marginLeft = ConstantsRepository.HEADER_MARGIN_LEFT;
        final float marginTop = ConstantsRepository.HEADER_MARGIN_TOP;
        final float tableWidth = ConstantsRepository.TABLE_WIDTH;
        final String[] headers = ConstantsRepository.HEADERS;

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, ConstantsRepository.HEADER_FONT_SIZE);
        contentStream.beginText();
        contentStream.newLineAtOffset(marginLeft, marginTop);

        for (final String header : headers) {
            contentStream.showText(header);
            contentStream.newLineAtOffset(tableWidth / headers.length, ConstantsRepository.MINI_VALOR);
        }

        contentStream.endText();
    }

    private void createBody(final PDDocument document, PDPageContentStream contentStream,
                            final List<RegisteredEntity> registeredEntityList) throws IOException {
        float marginTop = ConstantsRepository.MARGIN_TOP;
        final float rowHeight = ConstantsRepository.ROW_HEIGHT;

        contentStream.setFont(PDType1Font.HELVETICA, ConstantsRepository.FRONT_SIZE);

        for (final RegisteredEntity registered : registeredEntityList) {
            if (marginTop < ConstantsRepository.MARGIN_TOP_MIN) {
                contentStream.close();
                contentStream = this.createNewPage(document);
                marginTop = ConstantsRepository.MARGIN_TOP;
            }

            this.drawRow(contentStream, registered, marginTop);
            marginTop -= rowHeight;
        }
    }


    private PDPageContentStream createNewPage(final PDDocument document) throws IOException {
        final PDPage page = new PDPage();
        document.addPage(page);
        final PDPageContentStream newContentStream = new PDPageContentStream(document, page);

        this.createTitle(newContentStream);
        this.createHeader(newContentStream);

        return newContentStream;
    }

    private void drawRow(final PDPageContentStream contentStream, final RegisteredEntity registered,
                         final float marginTop) throws IOException {
        final float marginLeft = ConstantsRepository.HEADER_MARGIN_LEFT;
        final float cellWidth = ConstantsRepository.TABLE_WIDTH / ConstantsRepository.HEADERS.length;
        final float increasedCellWidthForDocument = (cellWidth * ConstantsRepository.DOCUMENT_CELL_WIDTH_ADJUSTMENT_FACTOR);
        final float spaceBetweenCells = ConstantsRepository.SPACE_CELLS;

        contentStream.beginText();
        contentStream.newLineAtOffset(marginLeft, marginTop);

        contentStream.showText(String.valueOf(registered.getStudent().getId()));
        contentStream.newLineAtOffset(cellWidth + spaceBetweenCells, ConstantsRepository.MINI_VALOR);

        contentStream.showText(String.valueOf(registered.getStudent().getFirstName()));
        contentStream.newLineAtOffset(cellWidth + spaceBetweenCells, ConstantsRepository.MINI_VALOR);

        contentStream.showText(String.valueOf(registered.getStudent().getSecondName()));
        contentStream.newLineAtOffset(cellWidth + spaceBetweenCells, ConstantsRepository.MINI_VALOR);

        contentStream.showText(registered.getStudent().getDocumentNumber());
        contentStream.newLineAtOffset(increasedCellWidthForDocument + spaceBetweenCells,
                ConstantsRepository.MINI_VALOR);

        contentStream.showText(String.valueOf(registered.getNota2()));
        contentStream.newLineAtOffset(cellWidth + spaceBetweenCells, ConstantsRepository.MINI_VALOR);

        contentStream.showText(String.valueOf(registered.getNota3()));
        contentStream.newLineAtOffset(cellWidth + spaceBetweenCells, ConstantsRepository.MINI_VALOR);

        contentStream.showText(String.valueOf(registered.getNota1()));
        contentStream.newLineAtOffset(cellWidth + spaceBetweenCells, ConstantsRepository.MINI_VALOR);


        contentStream.showText(String.valueOf(registered.getAverage()));
        contentStream.newLine();

        contentStream.endText();
    }
}
