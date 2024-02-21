package edu.setokk.astrocluster.controller;

import edu.setokk.astrocluster.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterController {
    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    public ResponseEntity<?> perform(@RequestParam String gitUrl) {
        clusterService.perform(gitUrl);

        return ResponseEntity.ok(null);
    }
}
