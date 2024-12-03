package edu.setokk.astrocluster.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import edu.setokk.astrocluster.core.pdf.PdfMessage;
import edu.setokk.astrocluster.util.Csv;
import edu.setokk.astrocluster.util.PdfUtils;
import edu.setokk.astrocluster.util.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {
    public PdfMessage generatePdfFromCsv(Csv csv) {
        Map<String, Integer> maxCellWidths = PdfUtils.getOptimalMaxCellWidthsForCsv(csv);
        PageSize pageSize = new PageSize(800.0F, maxCellWidths.values().stream().reduce(Integer::sum).get());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(os);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(pageSize);
        Document document = new Document(pdfDocument);

        // Title
        document.add(new Paragraph(csv.getTitle())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setMarginBottom(3));

        // Add table headers
        Table table = new Table(csv.getColumnCount());
        for (Csv.Column column : csv.getColumns().get()) {
            table.addHeaderCell(new Cell()
                    .add(new Paragraph(column.columnName())
                            .setFontSize(15)
                            .setTextAlignment(TextAlignment.LEFT))
                            .setMinWidth(maxCellWidths.get(column.columnName()))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setMinWidth(maxCellWidths.get(column.columnName())));
        }

        // Add table cells
        for (int i = 0; i < csv.getRowCount(); i++) {
            for (Csv.Column column : csv.getColumns().get()) {
                String currValue = csv.getColumnValues(column.columnName()).get(i);
                table.addCell(new Cell()
                        .add(new Paragraph(currValue)
                                .setFontSize(12)
                                .setTextAlignment(TextAlignment.LEFT)
                                .setMinWidth(maxCellWidths.get(column.columnName())))
                        .setBackgroundColor(ColorConstants.WHITE)
                        .setMinWidth(maxCellWidths.get(column.columnName())));
            }
        }
        document.add(table);
        document.close();

        String filename = StringUtils.splitByAndGetLast(csv.getFilepath().toString(), "[\\\\/]").replace(".csv", ".pdf");
        return new PdfMessage(os.toByteArray(), filename);
    }
}
