package edu.setokk.astrocluster.cluster;

import edu.setokk.astrocluster.cluster.proto.ClusterRequest;
import edu.setokk.astrocluster.cluster.proto.ClusterResponse;
import edu.setokk.astrocluster.cluster.proto.ClusterServiceGrpc;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.util.IOUtils;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ClusterService {

    private final ClusterServiceGrpc.ClusterServiceBlockingStub clusterBlockingStub;

    @Autowired
    public ClusterService(ManagedChannel managedChannel) {
        this.clusterBlockingStub = ClusterServiceGrpc.newBlockingStub(managedChannel);
    }

    public void performClustering(ClusterRequestBody requestBody) throws IOException, InterruptedException {
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