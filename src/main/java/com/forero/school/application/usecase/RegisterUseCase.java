package com.forero.school.application.usecase;

import com.forero.school.application.service.RepositoryService;
import com.forero.school.domain.agregate.DataResultAgregate;
import com.forero.school.domain.model.Registered;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public record RegisterUseCase(RepositoryService repositoryService) {
    public void registerNotes(final Integer subjectId, final MultipartFile file) throws IOException {
        this.repositoryService.validateIfSubjectExists(subjectId);
        this.repositoryService.uploadGrades(file, Long.valueOf(subjectId));
    }

    public List<DataResultAgregate> getAllSubjects() {
        final List<DataResultAgregate> subjects = this.repositoryService.getAllSubjectAndStudents();
        return subjects;
    }

    public List<Registered> getAllRecords() {
        return this.repositoryService.getAllRegistered();
    }

    public byte[] generarPdf() {
        return this.repositoryService.generatePdf();
    }

    public String generatePdf() throws IOException {
        List<Registered> registeredList = this.repositoryService.getAllRegistered();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PDDocument document = new PDDocument()) {
            // Definir la primera página
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType1Font font = PDType1Font.HELVETICA;
                contentStream.setFont(font, 12);

                // Definir el ancho de la tabla y el margen
                float tableWidth = page.getTrimBox().getWidth() - 40;
                float margin = 20;
                float yPosition = page.getTrimBox().getHeight() - margin;

                // Encabezados
                String[] headers = {"ID", "Estudiante", "Documento", "Materia", "Promedio", "Nota 1", "Nota 2", "Nota 3"};
                contentStream.beginText();
                contentStream.setLeading(15f);
                contentStream.newLineAtOffset(margin, yPosition);

                for (String header : headers) {
                    contentStream.showText(header);
                    contentStream.newLineAtOffset(100, 0); // Ajusta el espaciado horizontal entre columnas
                }

                contentStream.endText();
                yPosition -= 20; // Espacio después del encabezado

                // Línea divisoria entre encabezados y datos
                contentStream.drawLine(margin, yPosition, tableWidth + margin, yPosition);
                yPosition -= 10; // Espacio después de la línea divisoria

                // Datos
                for (Registered registered : registeredList) {
                    contentStream.beginText();
                    contentStream.setLeading(15f);
                    contentStream.newLineAtOffset(margin, yPosition);

                    contentStream.showText(String.valueOf(registered.getId()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(registered.getStudent().getFirstName() + " " + registered.getStudent().getSecondName());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(registered.getStudent().getDocumentNumber());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(registered.getSubject().getName());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(registered.getAverage().toString());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(registered.getNota1().toString() + ", " + registered.getNota2().toString() + ", " + registered.getNota3().toString());
                    contentStream.newLine();

                    contentStream.endText();
                    yPosition -= 15; // Ajusta el espaciado vertical entre filas

                    // Verifica si se necesita una nueva página
                    if (yPosition < margin) {
                        contentStream.close();

                        // Añadir nueva página
                        page = new PDPage();
                        document.addPage(page);

                        // Crear nuevo contentStream para la nueva página
                        try (PDPageContentStream newContentStream = new PDPageContentStream(document, page)) {
                            newContentStream.setFont(font, 12);
                            yPosition = page.getTrimBox().getHeight() - margin;
                            newContentStream.beginText();
                            newContentStream.setLeading(15f);
                            newContentStream.newLineAtOffset(margin, yPosition);

                            // Reimprimir encabezados en la nueva página
                            for (String header : headers) {
                                newContentStream.showText(header);
                                newContentStream.newLineAtOffset(100, 0); // Ajusta el espaciado horizontal entre columnas
                            }

                            newContentStream.endText();
                            yPosition -= 20; // Espacio después del encabezado

                            // Línea divisoria entre encabezados y datos
                            newContentStream.drawLine(margin, yPosition, tableWidth + margin, yPosition);
                            yPosition -= 10; // Espacio después de la línea divisoria

                            // Añadir datos
                            for (Registered r : registeredList) {
                                newContentStream.beginText();
                                newContentStream.setLeading(15f);
                                newContentStream.newLineAtOffset(margin, yPosition);

                                newContentStream.showText(String.valueOf(r.getId()));
                                newContentStream.newLineAtOffset(100, 0);
                                newContentStream.showText(r.getStudent().getFirstName() + " " + r.getStudent().getSecondName());
                                newContentStream.newLineAtOffset(100, 0);
                                newContentStream.showText(r.getStudent().getDocumentNumber());
                                newContentStream.newLineAtOffset(100, 0);
                                newContentStream.showText(r.getSubject().getName());
                                newContentStream.newLineAtOffset(100, 0);
                                newContentStream.showText(r.getAverage().toString());
                                newContentStream.newLineAtOffset(100, 0);
                                newContentStream.showText(r.getNota1().toString() + ", " + r.getNota2().toString() + ", " + r.getNota3().toString());
                                newContentStream.newLine();

                                newContentStream.endText();
                                yPosition -= 15; // Ajusta el espaciado vertical entre filas

                                if (yPosition < margin) {
                                    break; // Rompe el bucle para salir de la página
                                }
                            }
                        }
                    }
                }
            }

            document.save(outputStream);
        }

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}