package edu.setokk.astrocluster.core.interest;

import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.Csv;

import java.util.List;

public interface SimilarFilesStrategy {
    List<Similarity> findNeighbouringFiles(SimilarFilesStrategy.Parameters parameters);

    record Parameters(
            int currIndex,
            ClusterResultDto currProjectFile,
            List<ClusterResultDto> projectFiles,
            short numSimilarClasses,
            Csv metricsCsv
    ) {}

    record Similarity(int projectFileId, double similarityPercentage) {}
}
