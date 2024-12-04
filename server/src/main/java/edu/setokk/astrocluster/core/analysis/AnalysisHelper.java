package edu.setokk.astrocluster.core.analysis;

import edu.setokk.astrocluster.model.AnalysisEntity;
import edu.setokk.astrocluster.model.PercentagePerClusterEntity;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AnalysisHelper {
    public static Set<PercentagePerClusterEntity> calculatePercentagesPerCluster(List<ClusterResultDto> clusterResults, AnalysisEntity analysisEntity) {
        Map<Integer, Integer> clabelCount = new HashMap<>();
        for (var cr : clusterResults) {
            clabelCount.put(cr.getClusterLabel(), clabelCount.getOrDefault(cr.getClusterLabel(), 0) + 1);
        }

        Set<PercentagePerClusterEntity> percentagesPerCluster = new HashSet<>();
        for (var entry : clabelCount.entrySet()) {
            var percentagePerClusterId = new PercentagePerClusterEntity.PercentagePerClusterId(analysisEntity.getId(), entry.getKey());
            double percentage = (double) entry.getValue() / clusterResults.size();

            percentagesPerCluster.add(new PercentagePerClusterEntity(percentagePerClusterId, percentage * 100, analysisEntity));
        }
        return percentagesPerCluster;
    }
}
