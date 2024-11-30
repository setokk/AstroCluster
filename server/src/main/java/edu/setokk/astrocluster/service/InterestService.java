package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.enums.SimilarFilesCriteria;
import edu.setokk.astrocluster.core.interest.InterestHelper;
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
        // Prepare variables from requestBody
        final List<ClusterResultDto> projectFiles = analysisDto.getClusterResults();
        short numSimilarClasses = (short) projectFiles.stream()
                .collect(Collectors.groupingBy(
                        ClusterResultDto::getClusterLabel,
                        Collectors.counting()
                ))
                .values().stream()
                .mapToInt(Long::intValue)
                .min().orElse(0);
        numSimilarClasses = (numSimilarClasses == 1) ? 2 : numSimilarClasses;
        String csvTitle;
        String csvFilepath;
        String descriptiveLabel = requestBody.getIsDescriptive() ? "Descriptive " : "";

        // Figure out which strategy to use
        SimilarFilesCriteria similarFilesCriteria = SimilarFilesCriteria.get(requestBody.getSimilarFilesCriteria()).get();
        SimilarFilesStrategy similarFilesStrategy = switch (similarFilesCriteria) {
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
        Path metricsCsvPath = Paths.get(String.format(IOUtils.METRICS_CSV_FILE, analysisDto.getProjectUUID()));
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

        // Get actual indexes (map cluster results to the position they are in metrics csv)
        List<String> metricsCsvFilepaths = metricsCsv.getColumnValues("Name");
        Map<String, Integer> projectFilesToMetricsCsvIndexBridge = HashMap.newHashMap(metricsCsvFilepaths.size());
        for (int i = 0; i < metricsCsvFilepaths.size(); i++) {
            projectFilesToMetricsCsvIndexBridge.put(metricsCsvFilepaths.get(i), i);
        }

        // Calculate interest
        for (ClusterResultDto currProjectFile : projectFiles) {
            // Get top "numSimilarClasses" similar classes
            int currIndex = projectFilesToMetricsCsvIndexBridge.get(currProjectFile.getFilepath());
            var parameters = new SimilarFilesStrategy.Parameters(currProjectFile, projectFiles, numSimilarClasses, metricsCsv, projectFilesToMetricsCsvIndexBridge);
            List<SimilarFilesStrategy.Similarity> neighbouringFilesSorted = similarFilesStrategy.findNeighbouringFiles(parameters);

            metricsCsv.removeColumns("SIZE1", "SIZE2"); // Not needed anymore

            Map<String, Double> optimalClassMetrics = InterestHelper.calculateOptimalClassMetrics(currIndex, neighbouringFilesSorted, metricsCsv);
            Map<String, Double> diffFromOptimalClass = InterestHelper.calculateDiffFromOptimalClass(currIndex, optimalClassMetrics, metricsCsv);

            // Calculate average of differences
            double averageDiff = 0.0;
            for (Map.Entry<String, Double> entry : diffFromOptimalClass.entrySet()) {
                averageDiff += entry.getValue();
            }
            averageDiff = (!diffFromOptimalClass.isEmpty()) ? averageDiff / diffFromOptimalClass.size() : 0.0;
            double interestInAvgLOC = averageDiff * requestBody.getAvgPerGenerationLOC();
            double interestInHours = interestInAvgLOC / requestBody.getPerHourLOC();
            double interestInDollars = interestInHours * requestBody.getPerHourSalary();
            InterestHelper.updateInterestCsvForCurrentProjectFile(
                    interestResultsCsv, currIndex, currProjectFile,
                    requestBody.getIsDescriptive(), neighbouringFilesSorted,
                    optimalClassMetrics, diffFromOptimalClass,
                    interestInAvgLOC, interestInHours, interestInDollars, metricsCsv
            );
        }

        if (Files.notExists(interestResultsCsv.getFilepath()))
            interestResultsCsv.save(null);

        return interestResultsCsv;
    }
}
