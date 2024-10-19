package edu.setokk.astrocluster.cluster;

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
    public ResponseEntity<?> performClustering(@RequestBody ClusterRequestBody requestBody) throws IOException, InterruptedException {
        requestBody.validate();
        clusterService.performClustering(requestBody);
        return ResponseEntity.ok(null);
    }
}
