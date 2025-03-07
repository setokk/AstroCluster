package edu.setokk.astrocluster.controller;

import edu.setokk.astrocluster.core.file.CsvMessage;
import edu.setokk.astrocluster.core.file.FileMessage;
import edu.setokk.astrocluster.core.file.PdfMessage;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;
import edu.setokk.astrocluster.request.InterestPdfAnalysisRequest;
import edu.setokk.astrocluster.response.GetAnalysisResponse;
import edu.setokk.astrocluster.response.GetLatestAnalysesResponse;
import edu.setokk.astrocluster.service.AnalysisService;
import edu.setokk.astrocluster.service.PercentagePerClusterService;
import edu.setokk.astrocluster.util.MediaTypesAC;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;
    private final PercentagePerClusterService percentagePerClusterService;

    @Autowired
    public AnalysisController(AnalysisService analysisService, PercentagePerClusterService percentagePerClusterService) {
        this.analysisService = analysisService;
        this.percentagePerClusterService = percentagePerClusterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAnalysisResponse> getAnalysis(@PathVariable(name = "id") long analysisId) {
        AnalysisDto analysisDto = analysisService.getAnalysis(analysisId);
        List<PercentagePerClusterDto> percentagesPerClusterDto = percentagePerClusterService.findAllByAnalysisId(analysisId);
        return ResponseEntity.ok(
                GetAnalysisResponse.builder()
                        .analysisData(analysisDto)
                        .percentagesPerCluster(percentagesPerClusterDto)
                        .build()
        );
    }

    @GetMapping("/latest-analyses")
    public ResponseEntity<GetLatestAnalysesResponse> getLatestAnalyses() {
        List<AnalysisDto> latestAnalyses = analysisService.getLatestAnalyses();
        return ResponseEntity.ok(
                GetLatestAnalysesResponse.builder()
                        .latestAnalyses(latestAnalyses)
                        .build()
        );
    }

    @PostMapping("/interest/pdf")
    public ResponseEntity<byte[]> generateInterestPdfForAnalysis(@RequestBody @Valid InterestPdfAnalysisRequest requestBody) throws IOException {
        requestBody.validate();
        FileMessage fileMessage = analysisService.generateInterestResultsFileForAnalysis(requestBody, new PdfMessage(null, null));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=\"" + fileMessage.filename() + "\"")
                .body(fileMessage.bytes());
    }

    @PostMapping("/interest/csv")
    public ResponseEntity<byte[]> generateInterestCsvForAnalysis(@RequestBody @Valid InterestPdfAnalysisRequest requestBody) throws IOException {
        requestBody.validate();
        FileMessage fileMessage = analysisService.generateInterestResultsFileForAnalysis(requestBody, new CsvMessage(null, null));
        return ResponseEntity.ok()
                .contentType(MediaTypesAC.TEXT_CSV)
                .header("Content-Disposition", "attachment; filename=\"" + fileMessage.filename() + "\"")
                .body(fileMessage.bytes());
    }

}
