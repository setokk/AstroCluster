package edu.setokk.astrocluster.cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ClusterController {
    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    public ResponseEntity<?> performClustering(@RequestParam String gitUrl) throws IOException, InterruptedException {
        clusterService.performClustering(gitUrl);

        return ResponseEntity.ok(null);
    }
}
