package edu.setokk.astrocluster.controller;

import edu.setokk.astrocluster.core.cluster.PercentagePerCluster;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.request.PerformClusteringRequest;
import edu.setokk.astrocluster.response.PerformClusteringResponse;
import edu.setokk.astrocluster.service.ClusterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/cluster")
public class ClusterController {
    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @PostMapping("/perform-clustering")
    public ResponseEntity<?> performClustering(@RequestBody @Valid PerformClusteringRequest requestBody) throws IOException, InterruptedException {
        requestBody.validate();
        /*clusterService.performClustering(requestBody);*/
        AnalysisDto analysisDto = AnalysisDto.builder()
                .id(1L)
                .gitProjectName("AstroCluster!")
                .gitUrl("https://www.github.com/setokk/AstroCluster.git")
                .addClusterResult(ClusterResultDto.builder()
                        .id(1L)
                        .filename("Something.java")
                        .filepath("src/java/main/Something.java")
                        .clusterLabel(0).build())
                .build();

        PerformClusteringResponse responseBody = PerformClusteringResponse.builder()
                .analysisData(analysisDto)
                .addPercentagePerCluster(new PercentagePerCluster(0, 100.0))
                .build();
        return ResponseEntity.ok(responseBody);
    }
}
