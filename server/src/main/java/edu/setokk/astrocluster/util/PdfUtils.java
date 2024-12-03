package edu.setokk.astrocluster.util;

import java.util.HashMap;
import java.util.Map;

public final class PdfUtils {
    public static Map<String, Integer> getOptimalMaxCellWidthsForCsv(Csv csv) {
        Map<String, Integer> maxCellWidths = HashMap.newHashMap(csv.getColumnCount());
        for (var entry : csv.getCsvContent().entrySet()) {
            int maxCellWidth = csv.getColumnValues(entry.getKey()).stream().map(String::length).max(Integer::compareTo).get();
            maxCellWidths.put(entry.getKey(), maxCellWidth);
        }
        return maxCellWidths;
    }
}
