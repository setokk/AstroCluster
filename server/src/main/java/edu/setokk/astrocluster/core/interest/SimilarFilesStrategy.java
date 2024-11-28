package edu.setokk.astrocluster.core.interest;

import edu.setokk.astrocluster.model.dto.ClusterResultDto;

import java.util.List;

public interface SimilarFilesStrategy {
    void findNeighbouringFiles(SimilarFilesStrategy.Parameters parameters);

    record Parameters(ClusterResultDto currProjectFile,
                      int numSimilarClasses,
                      List<Double> size1Values,
                      List<Double> size2Values,
                      List<ClusterResultDto> projectFiles
                      ) {}
}
