package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.enums.SimilarFilesCriteria;
import edu.setokk.astrocluster.core.interest.SimilarFilesClusterStrategy;
import edu.setokk.astrocluster.core.interest.SimilarFilesNormalStrategy;
import edu.setokk.astrocluster.core.interest.SimilarFilesStrategy;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.request.InterestPdfAnalysisRequest;
import edu.setokk.astrocluster.util.Csv;
import edu.setokk.astrocluster.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Service
public class InterestService {
    public Csv calculateInterestCsv(AnalysisDto analysisDto, InterestPdfAnalysisRequest requestBody) throws IOException {
        final String title;
        final String filepath;
        final String descriptiveLabel = requestBody.getIsDescriptive() ? "Descriptive " : "";

        // Figure out which strategy to use
        final SimilarFilesCriteria similarFilesCriteria = SimilarFilesCriteria.get(requestBody.getSimilarFilesCriteria()).get();
        final SimilarFilesStrategy similarFilesStrategy = switch (similarFilesCriteria) {
            case SimilarFilesCriteria.NORMAL ->  {
                title = String.format("%sNormal Interest Results for analysis uuid %s", descriptiveLabel, analysisDto.getProjectUUID());
                filepath = String.format(IOUtils.INTEREST_CSV_PATH, analysisDto.getProjectUUID(), "interestResultsNormal", descriptiveLabel.trim());
                yield  new SimilarFilesNormalStrategy();
            }
            case SimilarFilesCriteria.CLUSTER -> {
                title = String.format("%sCluster Interest Results for analysis uuid %s", descriptiveLabel, analysisDto.getProjectUUID());
                filepath = String.format(IOUtils.INTEREST_CSV_PATH, analysisDto.getProjectUUID(), "interestResultsCluster", descriptiveLabel.trim());
                yield new SimilarFilesClusterStrategy();
            }
        };

        // Output csv
        final Csv interestResultsCsv = new Csv(title, Paths.get(filepath), ",");
        if (Files.exists(interestResultsCsv.getFilepath())) {
            interestResultsCsv.load();
        }

        // Calculate metrics by calling metrics calculator
        Path metricsOutputPath = Paths.get(String.format(IOUtils.METRICS_CSV_PATH, analysisDto.getProjectUUID()));
        if (Files.notExists(metricsOutputPath)) {
            // TODO: Call metrics calculator
        }

        final Csv metricsCsv = new Csv(null, metricsOutputPath, ";");
        metricsCsv.load(Set.of());

        List<ClusterResultDto> projectFiles = analysisDto.getClusterResults();
        for (ClusterResultDto currProjectFile : projectFiles) {
            var parameters = new SimilarFilesStrategy.Parameters(currProjectFile, projectFiles, requestBody.getNumSimilarClasses(), metricsCsv);
            List<ClusterResultDto> neighbouringFiles = similarFilesStrategy.findNeighbouringFiles(parameters);
        }

        return interestResultsCsv;
    }
}
