package edu.setokk.astrocluster.core.analysis;

import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AnalysisHelper {
    public static Set<PercentagePerClusterJpo> calculatePercentagesPerCluster(List<ClusterResultDto> clusterResults, long analysisId) {
        Map<Integer, Integer> clabelCount = new HashMap<>();
        for (var cr : clusterResults) {
            clabelCount.put(cr.getClusterLabel(), clabelCount.getOrDefault(cr.getClusterLabel(), 0) + 1);
        }

        AnalysisJpo analysisJpo = new AnalysisJpo(analysisId);
        Set<PercentagePerClusterJpo> percentagesPerCluster = new HashSet<>();
        for (var entry : clabelCount.entrySet()) {
            var percentagePerClusterId = new PercentagePerClusterJpo.PercentagePerClusterId(analysisId, entry.getKey());
            double percentage = (double) entry.getValue() / clusterResults.size();

            percentagesPerCluster.add(new PercentagePerClusterJpo(percentagePerClusterId, percentage * 100, analysisJpo));
        }
        return percentagesPerCluster;
    }
}
