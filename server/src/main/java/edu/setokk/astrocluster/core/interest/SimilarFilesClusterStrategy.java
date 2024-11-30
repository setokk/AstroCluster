package edu.setokk.astrocluster.core.interest;

import edu.setokk.astrocluster.model.dto.ClusterResultDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SimilarFilesClusterStrategy implements SimilarFilesStrategy {
    @Override
    public List<Similarity> findNeighbouringFiles(Parameters parameters) {
        ClusterResultDto currProjectFile = parameters.currProjectFile();
        int currIndex = parameters.projectFilesToMetricsCsvIndexBridge().get(currProjectFile.getFilepath());
        List<ClusterResultDto> projectFiles = parameters.projectFiles();
        Map<String, List<String>> metricsCsvContent = parameters.metricsCsv().getCsvContent();

        return IntStream.range(0, projectFiles.size())
                .filter(i -> !currProjectFile.equals(projectFiles.get(i)))
                .filter(i -> currProjectFile.getClusterLabel() == projectFiles.get(i).getClusterLabel())
                .mapToObj(i -> {
                    int otherIndex = parameters.projectFilesToMetricsCsvIndexBridge().get(projectFiles.get(i).getFilepath());
                    double similarity = 0.0;
                    for (var entry : metricsCsvContent.entrySet()) {
                        String header = entry.getKey();
                        if (header.equalsIgnoreCase("Name")) continue;

                        double currMetricValue = Double.parseDouble(entry.getValue().get(currIndex));
                        double otherMetricValue = Double.parseDouble(entry.getValue().get(otherIndex));
                        if (currMetricValue == 0 && otherMetricValue == 0) {
                            similarity += 1.0; // Perfect match if both are zero
                        } else if (currMetricValue != 0) {
                            similarity += 1 - Math.abs((otherMetricValue - currMetricValue) / currMetricValue);
                        }
                    }
                    double avgSimilarity = (similarity / (metricsCsvContent.size() - 1)) * 100;
                    return new Similarity(otherIndex, avgSimilarity);
                })
                .sorted((a, b) -> Double.compare(b.similarityPercentage(), a.similarityPercentage()))
                .limit(parameters.numSimilarClasses())
                .collect(Collectors.toList());
    }
}
