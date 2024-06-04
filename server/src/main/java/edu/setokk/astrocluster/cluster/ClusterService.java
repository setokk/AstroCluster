package edu.setokk.astrocluster.cluster;

import edu.setokk.astrocluster.error.exception.BusinessLogicException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ClusterService {
    @GrpcClient("cluster-grpc-service")
    private

    public void performClustering(String gitUrl) throws IOException, InterruptedException {
        // Create a unique dir using UUID
        String projectDir = "./" + UUID.randomUUID();

        // Clone the git repo in that folder
        Process gcProcess = Runtime.getRuntime().exec(new String[]{"git", "clone", gitUrl, projectDir});
        if (gcProcess.waitFor() != 0)
            throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, "Git clone failed for url: \""+gitUrl+"\"");

        // Call clustering model from Python gRPC server


        // and receive cluster labels from model

        // Process the labels accordingly to produce result
        
    }
}
