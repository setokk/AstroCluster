package edu.setokk.astrocluster.core.interest;

import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.Csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InterestHelper {
    public static void updateInterestCsvForCurrentProjectFile(Parameters parameters) {
        Csv interestResultsCsv = parameters.interestResultsCsv();
        String projectUUID = parameters.projectUUID();
        ClusterResultDto currProjectFile = parameters.currProjectFile();
        int currIndex = parameters.currIndex();
        boolean isDescriptive = parameters.isDescriptive();
        List<SimilarFilesStrategy.Similarity> neighbouringFilesSorted = parameters.neighbouringFilesSorted();
        Csv metricsCsv = parameters.metricsCsv();
        Map<String, Double> optimalClassMetrics = parameters.optimalClassMetrics();
        Map<String, Double> diffFromOptimalClass = parameters.diffFromOptimalClass();
        String interestInAvgLOCFormatted = String.format("%.5f", parameters.interestInAvgLOC());
        String interestInHoursFormatted = String.format("%.5f", parameters.interestInHours());
        String interestInDollarsFormatted = String.format("%.5f", parameters.interestInDollars());

        if (interestResultsCsv.getColumns().isEmpty()) {
            interestResultsCsv.setColumns(createCsvColumns(isDescriptive, neighbouringFilesSorted));
        }

        if (isDescriptive) {
            // Add first row
            interestResultsCsv.addColumnValue("Metric", "");
            interestResultsCsv.addColumnValue("Pivot File", currProjectFile.getFilepath());
            for (int i = 0; i < neighbouringFilesSorted.size(); i++) {
                int otherIndex = neighbouringFilesSorted.get(i).index();
                interestResultsCsv.addColumnValue("Similar File " + (i + 1),  metricsCsv.getColumnValues("Name").get(otherIndex).replace("projects/" + projectUUID, ""));
            }
            interestResultsCsv.addColumnValue("Optimal Class", "");
            interestResultsCsv.addColumnValue("Diff", "");
            interestResultsCsv.addColumnValue("Interest in Average LOC", interestInAvgLOCFormatted);
            interestResultsCsv.addColumnValue("Interest in Hours", interestInHoursFormatted);
            interestResultsCsv.addColumnValue("Interest in Dollars", interestInDollarsFormatted);

            // Rest of rows
            for (var entry : metricsCsv.getCsvContent().entrySet()) {
                if (entry.getKey().equals("Name") || entry.getKey().equals("SIZE1") || entry.getKey().equals("SIZE2")) {
                    continue;
                }
                interestResultsCsv.addColumnValue("Metric", entry.getKey());
                interestResultsCsv.addColumnValue("Pivot File", entry.getValue().get(currIndex));
                for (int i = 0; i < neighbouringFilesSorted.size(); i++) {
                    int otherIndex = neighbouringFilesSorted.get(i).index();
                    interestResultsCsv.addColumnValue("Similar File " + (i + 1), entry.getValue().get(otherIndex));
                }
                interestResultsCsv.addColumnValue("Optimal Class", String.format("%.5f", optimalClassMetrics.get(entry.getKey())));
                interestResultsCsv.addColumnValue("Diff", String.format("%.5f%%", diffFromOptimalClass.get(entry.getKey())));
                interestResultsCsv.addColumnValue("Interest in Average LOC", "");
                interestResultsCsv.addColumnValue("Interest in Hours", "");
                interestResultsCsv.addColumnValue("Interest in Dollars", "");
            }
            interestResultsCsv.addNewLines(1);
        } else {
            interestResultsCsv.addColumnValue("Name", currProjectFile.getFilepath());
            interestResultsCsv.addColumnValue("Interest in Average LOC", interestInAvgLOCFormatted);
            interestResultsCsv.addColumnValue("Interest in Hours", interestInHoursFormatted);
            interestResultsCsv.addColumnValue("Interest in Dollars", interestInDollarsFormatted);
        }
    }

    public static Csv.Column[] createCsvColumns(boolean isDescriptive, List<SimilarFilesStrategy.Similarity> neighbouringFilesSorted) {
        Csv.Column[] columns;
        if (isDescriptive) {
            columns = new Csv.Column[2 + neighbouringFilesSorted.size() + 5];
            columns[0] = new Csv.Column("Metric", true);
            columns[1] = new Csv.Column("Pivot File", true);
            for (int i = 0; i < neighbouringFilesSorted.size(); i++) {
                columns[2 + i] = new Csv.Column("Similar File " + (i + 1), true);
            }
            columns[2 + neighbouringFilesSorted.size()] = new Csv.Column("Optimal Class", true);
            columns[2 + neighbouringFilesSorted.size() + 1] = new Csv.Column("Diff", true);
            columns[2 + neighbouringFilesSorted.size() + 2] = new Csv.Column("Interest in Average LOC", true);
            columns[2 + neighbouringFilesSorted.size() + 3] = new Csv.Column("Interest in Hours", true);
            columns[2 + neighbouringFilesSorted.size() + 4] = new Csv.Column("Interest in Dollars", true);
        } else {
            columns = new Csv.Column[] {
                    new Csv.Column("Name", true),
                    new Csv.Column("Interest in Average LOC", true),
                    new Csv.Column("Interest in Hours", true),
                    new Csv.Column("Interest in Dollars", true)
            };
        }

        return columns;
    }

    public static Map<String, Double> calculateDiffFromOptimalClass(int currIndex, Map<String, Double> optimalClassMetrics, Csv metricsCsv) {
        Map<String, Double> diffFromOptimalClass = HashMap.newHashMap(metricsCsv.getCsvContent().size() - 1);
        for (Map.Entry<String, List<String>> entry : metricsCsv.getCsvContent().entrySet()) {
            String header = entry.getKey();
            if (header.equalsIgnoreCase("Name")) continue;

            double currValue = Double.parseDouble(entry.getValue().get(currIndex));
            double optimalValue = (!optimalClassMetrics.isEmpty()) ? optimalClassMetrics.get(header) : 0.0;
            double diff = 0.0;
            if (optimalValue == 0) {
                if (currValue != 0)
                    diff = Double.POSITIVE_INFINITY;
            } else {
                diff = Math.abs(currValue - optimalValue) / optimalValue;
            }
            diffFromOptimalClass.put(header, diff);
        }

        return diffFromOptimalClass;
    }

    public static Map<String, Double> calculateOptimalClassMetrics(int currIndex, List<SimilarFilesStrategy.Similarity> neighbouringFilesSorted, Csv metricsCsv) {
        Map<String, Double> optimalClassMetrics = HashMap.newHashMap(metricsCsv.getCsvContent().size() - 1);
        List<SimilarFilesStrategy.Similarity> neighbouringFilesSortedTemp = new ArrayList<>(neighbouringFilesSorted);
        neighbouringFilesSortedTemp.add(new SimilarFilesStrategy.Similarity(currIndex, 100));
        for (SimilarFilesStrategy.Similarity similarity : neighbouringFilesSortedTemp) {
            for (var entry : metricsCsv.getCsvContent().entrySet()) {
                String header = entry.getKey();
                List<String> headerValues = entry.getValue();
                if (header.equalsIgnoreCase("Name")) continue;

                // We assume that each metric is better if the values are lower so that we always get the min.
                double oldValue = optimalClassMetrics.getOrDefault(header, Double.MAX_VALUE);
                double newValue = Double.parseDouble(headerValues.get(similarity.index()));
                optimalClassMetrics.put(header, Math.min(oldValue, newValue));
            }
        }

        return optimalClassMetrics;
    }

    public record Parameters(
            Csv interestResultsCsv,
            String projectUUID,
            ClusterResultDto currProjectFile,
            int currIndex,
            boolean isDescriptive,
            List<SimilarFilesStrategy.Similarity> neighbouringFilesSorted,
            Map<String, Double> optimalClassMetrics,
            Map<String, Double> diffFromOptimalClass,
            double interestInAvgLOC,
            double interestInHours,
            double interestInDollars,
            Csv metricsCsv
    ) {}
}
