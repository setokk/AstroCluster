package edu.setokk.astrocluster.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVManager {
    private String[] headers;
    private Map<String, List<String>> csvContent;

    public CSVManager() {}

    public void loadData(Path path, String splitRegex) throws IOException {
        List<String> fileLines = Files.readAllLines(path);
        this.headers = fileLines.getFirst().split(splitRegex);
        this.csvContent = HashMap.newHashMap(0);
    }

    public void saveData(Path path, String splitRegex) {

    }

    public List<String> getColumn(String column) {
        return csvContent.getOrDefault(column, new ArrayList<>());
    }
}