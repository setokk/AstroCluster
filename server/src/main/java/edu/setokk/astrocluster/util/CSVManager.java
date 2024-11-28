package edu.setokk.astrocluster.util;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public final class CSVManager {
    private Header[] headers;
    private final Map<String, List<String>> csvContent;
    private final String splitRegex;

    public CSVManager(String splitRegex) {
        this.csvContent = HashMap.newHashMap(0);
        this.splitRegex = splitRegex;
    }

    public void load(Path path, Set<String> includedColumns) throws IOException {
        List<String> fileLines = Files.readAllLines(path);
        this.headers = Arrays.stream(fileLines.getFirst().split(splitRegex))
                .map(columnName -> new Header(columnName, includedColumns.contains(columnName)))
                .toArray(Header[]::new);

        for (String line : fileLines) {
            String[] values = line.split(splitRegex);
            for (int i = 0; i < values.length; i++) {
                addColumnValue(headers[i], values[i]);
            }
        }
    }

    public List<String> getColumnValues(String column) {
        return csvContent.computeIfAbsent(column, k -> new ArrayList<>());
    }

    public void addColumnValue(Header header, String value) {
       if (!header.isActive) return;
       List<String> values = getColumnValues(header.columnName);
       values.add(value);
    }

    public record Header(String columnName, boolean isActive) {}
}