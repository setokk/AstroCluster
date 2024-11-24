package edu.setokk.astrocluster.controller;

import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.request.PerformClusteringRequest;
import edu.setokk.astrocluster.response.PerformClusteringResponse;
import edu.setokk.astrocluster.service.ClusterService;
import edu.setokk.astrocluster.service.EmailService;
import edu.setokk.astrocluster.validation.PerformClusteringValidator;
import jakarta.mail.MessagingException;
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
    private final PerformClusteringValidator performClusteringValidator;

    @Autowired
    public ClusterController(ClusterService clusterService, PerformClusteringValidator performClusteringValidator) {
        this.clusterService = clusterService;
        this.performClusteringValidator = performClusteringValidator;
    }

    @PostMapping("/perform-clustering")
    public ResponseEntity<PerformClusteringResponse> performClustering(@RequestBody @Valid PerformClusteringRequest requestBody)
            throws IOException, InterruptedException, MessagingException {
        requestBody.validate();
        performClusteringValidator.advancedValidate(requestBody);

        var responseBuilder = PerformClusteringResponse.builder();
        if (requestBody.isAsync()) {
            clusterService.performClusteringAsync(requestBody);
            responseBuilder.isAsync(true).ack((short) 1);
        } else {
            AnalysisDto analysisDto = clusterService.performClustering(requestBody);
            responseBuilder.isAsync(false).analysisId(analysisDto.getId());
        }

        return ResponseEntity.ok(responseBuilder.build());
    }
}
