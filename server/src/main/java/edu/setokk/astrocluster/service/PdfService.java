package edu.setokk.astrocluster.service;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import edu.setokk.astrocluster.util.Csv;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Service
public class PdfService {
    public OutputStream generatePdfFromCsv(Csv csv) {
        OutputStream os = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(os);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // Title
        document.add(new Paragraph(csv.getTitle())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setFontColor(Color.createColorWithColorSpace(new float[]{255, 255, 255}))
                .setBackgroundColor(Color.createColorWithColorSpace(new float[]{131, 120, 156}))
                .setMarginBottom(20));

        Table table = new Table(UnitValue.createPercentArray(csv.getColumns().get().length)).useAllAvailableWidth();

        // Add table headers
        for (Csv.Column column : csv.getColumns().get()) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(column.columnName()))
                    .setFontSize(15)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.LEFT));
        }

        // Add table cells
        for (int i = 0; i < csv.getRowCount(); i++) {
            for (Csv.Column column : csv.getColumns().get()) {
                table.addCell(new Cell()
                        .add(new Paragraph(csv.getColumnValues(column.columnName()).get(i))
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setBackgroundColor(ColorConstants.WHITE)));
            }
        }
        document.add(table);

        document.close();
        return os;
    }
}
