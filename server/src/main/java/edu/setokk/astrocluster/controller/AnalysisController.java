package edu.setokk.astrocluster.controller;

import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.response.GetAnalysisResponse;
import edu.setokk.astrocluster.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public ResponseEntity<?> getAnalysis(@PathVariable long analysisId) {
        AnalysisDto analysisDto = analysisService.getAnalysis(analysisId);

        return ResponseEntity.ok(
                GetAnalysisResponse.builder()
                        .analysisData(analysisDto)
                        .percentagesPerCluster()
                        .build()
        );
    }
}
