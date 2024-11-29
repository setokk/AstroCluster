package edu.setokk.astrocluster.core.interest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SimilarFilesNormalStrategy implements SimilarFilesStrategy {
    @Override
    public List<Similarity> findNeighbouringFiles(Parameters parameters) {
        int currIndex = parameters.currIndex();
        List<String> size1Values = parameters.metricsCsv().getColumnValues("SIZE1");
        List<String> size2Values = parameters.metricsCsv().getColumnValues("SIZE2");
        double currSize1 = Double.parseDouble(size1Values.get(currIndex));
        double currSize2 = Double.parseDouble(size2Values.get(currIndex));

        return IntStream.range(0, size1Values.size())
                .filter(i -> currIndex != i)
                .mapToObj(i -> {
                    double similarity1 = (currSize1 != 0) ? 1 - Math.abs((Double.parseDouble(size1Values.get(i)) - currSize1) / currSize1) : 0;
                    double similarity2 = (currSize2 != 0) ? 1 - Math.abs((Double.parseDouble(size2Values.get(i)) - currSize2) / currSize2) : 0;
                    return new Similarity(i, (similarity1 * 100 + similarity2 * 100) / 2);
                })
                .sorted((a, b) -> Double.compare(b.similarityPercentage(), a.similarityPercentage())) // DESC order
                .limit(parameters.numSimilarClasses())
                .collect(Collectors.toList());
    }
}
