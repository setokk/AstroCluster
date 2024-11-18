package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.cluster.PercentagePerCluster;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.grpc.ClusterRequest;
import edu.setokk.astrocluster.grpc.ClusterResponse;
import edu.setokk.astrocluster.grpc.ClusterServiceGrpc;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.request.PerformClusteringRequest;
import edu.setokk.astrocluster.response.PerformClusteringResponse;
import edu.setokk.astrocluster.util.IOUtils;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ClusterService {

    private AnalysisService analysisService;
    private final ClusterServiceGrpc.ClusterServiceBlockingStub clusterBlockingStub;

    @Autowired
    public ClusterService(ManagedChannel managedChannel, AnalysisService analysisService) {
        this.clusterBlockingStub = ClusterServiceGrpc.newBlockingStub(managedChannel);
        this.analysisService = analysisService;
    }

    @Async
    public void performClustering(PerformClusteringRequest requestBody) throws IOException, InterruptedException {
        String projectPath = IOUtils.PROJECTS_DIR + UUID.randomUUID();
        Process gcProcess = Runtime.getRuntime().exec(new String[]{"git", "clone", requestBody.getGitUrl(), projectPath});
        if (gcProcess.waitFor() != 0)
            throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, "Git clone failed for url: \""+requestBody.getGitUrl()+"\"");

        // Call clustering model from Python gRPC server
        ClusterRequest clusterRequest = ClusterRequest.newBuilder()
                .setLang(requestBody.getLang())
                .setPath(projectPath)
                .addAllExtensions(requestBody.getExtensions())
                .build();
        ClusterResponse clusterResponse = clusterBlockingStub.performClustering(clusterRequest);
    }
}