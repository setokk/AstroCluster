package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.mapper.grpc.AnalysisGrpcMapper;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.grpc.ClusterRequest;
import edu.setokk.astrocluster.grpc.ClusterResponse;
import edu.setokk.astrocluster.grpc.ClusterServiceGrpc;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.AnalysisDto.AnalysisDtoBuilder;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.request.PerformClusteringRequest;
import edu.setokk.astrocluster.util.CmdUtils;
import edu.setokk.astrocluster.util.IOUtils;
import edu.setokk.astrocluster.util.StringUtils;
import io.grpc.ManagedChannel;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class ClusterService {

    private final AnalysisService analysisService;
    private final AuthService authService;
    private final EmailService emailService;
    private final ClusterServiceGrpc.ClusterServiceBlockingStub clusterBlockingStub;

    @Autowired
    public ClusterService(ManagedChannel managedChannel,
                          AnalysisService analysisService,
                          AuthService authService,
                          EmailService emailService) {
        this.clusterBlockingStub = ClusterServiceGrpc.newBlockingStub(managedChannel);
        this.analysisService = analysisService;
        this.authService = authService;
        this.emailService = emailService;
    }

    @Async
    public void performClusteringAsync(PerformClusteringRequest requestBody) throws MessagingException {
        performClustering(requestBody);
    }

    public AnalysisDto performClustering(PerformClusteringRequest requestBody) throws MessagingException {
        // Clone git project
        UUID uuid = UUID.randomUUID();
        int exitCode = CmdUtils.executeCmd("git", "clone", requestBody.getGitUrl(), IOUtils.PROJECTS_DIR + uuid);
        if (exitCode != CmdUtils.EXIT_CODE_SUCCESS)
            throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, "Git clone failed for url: \"" + requestBody.getGitUrl() + "\"");

        // Call clustering model from Python gRPC server
        ClusterRequest clusterRequest = ClusterRequest.newBuilder()
                .setLang(requestBody.getLang())
                .setPath(uuid.toString())
                .addAllExtensions(requestBody.getExtensions())
                .build();
        ClusterResponse clusterResponse = clusterBlockingStub.performClustering(clusterRequest);

        // Save analysis data in DB
        AnalysisDtoBuilder analysisBuilder = AnalysisGrpcMapper.INSTANCE.mapToTarget(clusterResponse);
        analysisBuilder.gitUrl(requestBody.getGitUrl())
                .projectUUID(uuid.toString())
                .projectLang(requestBody.getLang())
                .createdDate(ZonedDateTime.now(ZoneId.of("UTC+2")))
                .gitProjectName(StringUtils.splitByAndGetFirst(
                        StringUtils.splitByAndGetLast(requestBody.getGitUrl(), "\\/"), "\\."
                ));
        AnalysisDto analysisDto = analysisService.saveAnalysis(analysisBuilder.build());

        // Send completion email
        UserDto user = authService.getAuthenticatedUser();
        String userEmail = user.isPublicUser() ? requestBody.getEmail() : user.getEmail();
        emailService.sendMail(
                userEmail,
                "[AstroCluster]: Analysis Completed (UUID: " + uuid + ")",
                "<h2>An analysis you requested with UUID: " + uuid + ", has just completed!</h2>" +
                     "<p>You can find it <a href=\"http://localhost:4200/api/analysis/" + analysisDto.getId() + "\">here</a>.</p>" +
                     "<p><b>Please note that analyses which are performed with no account are publicly available.</b></p>." +
                     "<p>If you wish to perform private analyses, please create an account <a href=\"http://localhost:4200/register\">here</a>."
        );
        return analysisDto;
    }
}