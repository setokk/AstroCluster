package edu.setokk.astrocluster.util;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Getter
public final class Csv {
    private String title;
    private Path filepath;
    @Setter private Column[] columns;
    private final Map<String, List<String>> csvContent;
    private final String splitRegex;
    private final Map<String, String> metadata;

    public Csv(String splitRegex) {
        this.csvContent = HashMap.newHashMap(0);
        this.splitRegex = splitRegex;
        this.metadata = HashMap.newHashMap(0);
    }

    public Csv(String title, Path filepath, String splitRegex) {
        this(splitRegex);
        this.title = title;
        this.filepath = filepath;
    }

    public void load() throws IOException {
        List<String> fileLines = Files.readAllLines(filepath);
        this.columns = Arrays.stream(fileLines.getFirst().split(splitRegex))
                .map(columnName -> new Column(columnName, true))
                .toArray(Column[]::new);

        for (String line : fileLines) {
            String[] values = line.split(splitRegex);
            for (int i = 0; i < values.length; i++) {
                addColumnValue(columns[i], values[i]);
            }
        }
    }

    public void load(Set<String> includedColumns) throws IOException {
        List<String> fileLines = Files.readAllLines(filepath);
        this.columns = Arrays.stream(fileLines.getFirst().split(splitRegex))
                .map(columnName -> new Column(columnName, includedColumns.contains(columnName)))
                .toArray(Column[]::new);

        for (String line : fileLines) {
            String[] values = line.split(splitRegex);
            for (int i = 0; i < values.length; i++) {
                addColumnValue(columns[i], values[i]);
            }
        }
    }

    public List<String> getColumnValues(String column) {
        return csvContent.computeIfAbsent(column, k -> new ArrayList<>());
    }

    public void addColumnValue(Column column, String value) {
        if (!column.isActive) return;
        List<String> values = getColumnValues(column.columnName);
        values.add(value);
    }

    public void addColumnValue(String columnName, String value) {
        List<String> values = getColumnValues(columnName);
        values.add(value);
    }

    public void removeColumns(String... column) {
        for (var c : column) {
            csvContent.remove(c);
        }
    }

    public Optional<Column[]> getColumns() {
        return Optional.ofNullable(columns);
    }

    public void addNewLines(int numNewLines) {
        for (int i = 0; i < numNewLines; i++) {
            for (var entry : csvContent.entrySet()) {
                getColumnValues(entry.getKey()).add("");
            }
        }
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public record Column(String columnName, boolean isActive) {}
}