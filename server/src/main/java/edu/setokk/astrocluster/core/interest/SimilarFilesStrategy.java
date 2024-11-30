package edu.setokk.astrocluster.core.interest;

import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.Csv;

import java.util.List;
import java.util.Map;

public interface SimilarFilesStrategy {
    List<Similarity> findNeighbouringFiles(SimilarFilesStrategy.Parameters parameters);

    record Parameters(
            ClusterResultDto currProjectFile,
            List<ClusterResultDto> projectFiles,
            short numSimilarClasses,
            Csv metricsCsv,
            Map<String, Integer> projectFilesToMetricsCsvIndexBridge
    ) {}

    record Similarity(int index, double similarityPercentage) {}
}
