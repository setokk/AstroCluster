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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ClusterService {

    private final ClusterServiceGrpc.ClusterServiceBlockingStub clusterBlockingStub;

    @Autowired
    public ClusterService(ManagedChannel managedChannel) {
        this.clusterBlockingStub = ClusterServiceGrpc.newBlockingStub(managedChannel);
    }

    public PerformClusteringResponse performClustering(PerformClusteringRequest requestBody) throws IOException, InterruptedException {
        String projectPath = IOUtils.PROJECTS_DIR + UUID.randomUUID();
        Process gcProcess = Runtime.getRuntime().exec(new String[]{"git", "clone", requestBody.getGitUrl(), projectPath});
        if (gcProcess.waitFor() != 0)
            throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, "Git clone failed for url: \""+requestBody.getGitUrl()+"\"");

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
        return PerformClusteringResponse.builder()
                .analysisData(analysisDto)
                .addPercentagePerCluster(new PercentagePerCluster(0, 100.0))
                .build();

        // Call clustering model from Python gRPC server
/*        ClusterRequest clusterRequest = ClusterRequest.newBuilder()
                .setLang(requestBody.getLang())
                .setPath(projectPath)
                .addAllExtensions(requestBody.getExtensions())
                .build();
        ClusterResponse clusterResponse = clusterBlockingStub.performClustering(clusterRequest);*/
    }
}