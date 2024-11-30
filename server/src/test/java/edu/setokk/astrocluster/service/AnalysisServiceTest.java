package edu.setokk.astrocluster.service;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AnalysisServiceTest {
    @Inject
    private InterestService interestService;
    @Inject
    private PdfService pdfService;

    @Test
    void generateInterestPdfForAnalysis() {

    }
}