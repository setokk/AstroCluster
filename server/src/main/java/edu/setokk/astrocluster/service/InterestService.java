package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.enums.SimilarFilesCriteria;
import edu.setokk.astrocluster.core.interest.SimilarFilesClusterStrategy;
import edu.setokk.astrocluster.core.interest.SimilarFilesNormalStrategy;
import edu.setokk.astrocluster.core.interest.SimilarFilesStrategy;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.request.InterestPdfAnalysisRequest;
import edu.setokk.astrocluster.util.CmdUtils;
import edu.setokk.astrocluster.util.Csv;
import edu.setokk.astrocluster.util.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InterestService {
    public Csv calculateInterestCsv(AnalysisDto analysisDto, InterestPdfAnalysisRequest requestBody) throws IOException {
        List<ClusterResultDto> projectFiles = analysisDto.getClusterResults();
        short numSimilarClasses = (short) projectFiles.stream()
                .collect(Collectors.groupingBy(
                        ClusterResultDto::getClusterLabel,
                        Collectors.counting()
                ))
                .values().stream()
                .mapToInt(Long::intValue)
                .min().orElse(1);
        final String csvTitle;
        final String csvFilepath;
        final String descriptiveLabel = requestBody.getIsDescriptive() ? "Descriptive " : "";

        // Figure out which strategy to use
        final SimilarFilesCriteria similarFilesCriteria = SimilarFilesCriteria.get(requestBody.getSimilarFilesCriteria()).get();
        final SimilarFilesStrategy similarFilesStrategy = switch (similarFilesCriteria) {
            case SimilarFilesCriteria.NORMAL ->  {
                csvTitle = String.format("%sNormal Interest Results for analysis uuid %s", descriptiveLabel, analysisDto.getProjectUUID());
                csvFilepath = String.format(IOUtils.INTEREST_CSV_FILE, analysisDto.getProjectUUID(), "interestResultsNormal", descriptiveLabel.trim());
                yield new SimilarFilesNormalStrategy();
            }
            case SimilarFilesCriteria.CLUSTER -> {
                csvTitle = String.format("%sCluster Interest Results for analysis uuid %s", descriptiveLabel, analysisDto.getProjectUUID());
                csvFilepath = String.format(IOUtils.INTEREST_CSV_FILE, analysisDto.getProjectUUID(), "interestResultsCluster", descriptiveLabel.trim());
                yield new SimilarFilesClusterStrategy();
            }
        };

        // Interest results csv (if already calculated, return)
        final Csv interestResultsCsv = new Csv(csvTitle, Paths.get(csvFilepath), ",");
        if (Files.exists(interestResultsCsv.getFilepath())) {
            interestResultsCsv.load();
            return interestResultsCsv;
        }
        // Calculate metrics by calling metrics calculator
        final Path metricsCsvPath = Paths.get(String.format(IOUtils.METRICS_CSV_FILE, analysisDto.getProjectUUID()));
        final Csv metricsCsv = new Csv(null, metricsCsvPath, ";");
        if (Files.notExists(metricsCsvPath)) {
            String projectPath = IOUtils.PROJECTS_DIR + analysisDto.getProjectUUID();
            int exitCode = CmdUtils.executeCmd("java", "-Xms64m", "-jar", IOUtils.METRICS_CALCULATOR_JAR, projectPath, metricsCsvPath.toAbsolutePath().toString());
            if (exitCode != CmdUtils.EXIT_CODE_SUCCESS) {
                throw new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, "Metrics calculation module is unavailable. Please try again later.");
            }
        }
        Set<String> includedColumns = Set.of("Name", "WMC", "DIT", "CBO", "LCOM", "SIZE1", "SIZE2");
        metricsCsv.load(includedColumns);

        // Get actual indexes (map metrics csv indexes to cluster results)
        List<String> metricsCsvFilepaths = metricsCsv.getColumnValues("Name");
        Map<String, Integer> clusterResultsIndexBridge = HashMap.newHashMap(metricsCsvFilepaths.size());
        for (int i = 0; i < metricsCsvFilepaths.size(); i++) {
            clusterResultsIndexBridge.put(metricsCsvFilepaths.get(i), i);
        }

        // Calculate interest
        for (ClusterResultDto currProjectFile : projectFiles) {
            int currIndex = clusterResultsIndexBridge.get(currProjectFile.getFilepath());
            var parameters = new SimilarFilesStrategy.Parameters(currIndex, currProjectFile, projectFiles, numSimilarClasses, metricsCsv);
            List<SimilarFilesStrategy.Similarity> neighbouringFiles = similarFilesStrategy.findNeighbouringFiles(parameters);
        }

        return interestResultsCsv;
    }
}
