package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.pdf.PdfMessage;
import edu.setokk.astrocluster.util.Csv;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class PdfServiceTest {
    private PdfService pdfService = new PdfService();

    @Test
    void generatePdfFromCsv() throws IOException {
        Csv csv = new Csv("1ac1c874-74a5-4f5b-91b2-ad17a52b2692", Paths.get(""), ",");

        Csv.Column[] columns = new Csv.Column[] {
                new Csv.Column("Name", true),
                new Csv.Column("Interest in Average LOC", true),
                new Csv.Column("Interest in Hours", true),
                new Csv.Column("Interest in Dollars", true)
        };
        csv.setColumns(columns);

        csv.addColumnValue("Name", "projects/1ac1c874-74a5-4f5b-91b2-ad17a52b2692/src/main/java/edu/gradinfo/dashboard/util/UIUtils.java");
        csv.addColumnValue("Name", "projects/1ac1c874-74a5-4f5b-91b2-ad17a52b2692/src/main/java/edu/gradinfo/dashboard/Dashboard.java");
        csv.addColumnValue("Name", "projects/1ac1c874-74a5-4f5b-91b2-ad17a52b2692/src/main/java/edu/gradinfo/Main.java");
        csv.addColumnValue("Name", "projects/1ac1c874-74a5-4f5b-91b2-ad17a52b2692/src/main/java/edu/gradinfo/service/GitService.java");
        csv.addColumnValue("Name", "projects/1ac1c874-74a5-4f5b-91b2-ad17a52b2692/src/main/java/edu/gradinfo/controller/GitController.java");

        csv.addColumnValue("Interest in Average LOC", "2.000000");
        csv.addColumnValue("Interest in Average LOC", "21.000000");
        csv.addColumnValue("Interest in Average LOC", "Infinity");
        csv.addColumnValue("Interest in Average LOC", "100.000000");
        csv.addColumnValue("Interest in Average LOC", "0.000000");

        csv.addColumnValue("Interest in Hours", "Infinity");
        csv.addColumnValue("Interest in Hours", "20.200040");
        csv.addColumnValue("Interest in Hours", "Infinity");
        csv.addColumnValue("Interest in Hours", "100.000000");
        csv.addColumnValue("Interest in Hours", "0.000000");

        csv.addColumnValue("Interest in Dollars", "Infinity");
        csv.addColumnValue("Interest in Dollars", "20.000000");
        csv.addColumnValue("Interest in Dollars", "0.000000");
        csv.addColumnValue("Interest in Dollars", "57.000000");
        csv.addColumnValue("Interest in Dollars", "0.000000");

        PdfMessage pdfMessage = pdfService.generatePdfFromCsv(csv);
        Path pdfPath = Paths.get(System.getProperty("user.dir") + File.separator + pdfMessage.filename());

        Files.write(pdfPath, pdfMessage.pdfBytes());
        System.out.println("Saved file at: " + pdfPath.toAbsolutePath());
    }
}