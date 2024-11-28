package edu.setokk.astrocluster.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
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



        pdfDocument.close();
        document.close();
    }
}
