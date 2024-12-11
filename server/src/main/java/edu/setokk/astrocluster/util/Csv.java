package edu.setokk.astrocluster.util;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public Csv(String splitRegex) {
        this.csvContent = HashMap.newHashMap(0);
        this.splitRegex = splitRegex;
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

        for (int i = 1; i < fileLines.size(); i++) {
            String line = fileLines.get(i);
            String[] values = line.split(splitRegex);
            for (int j = 0; j < values.length; j++) {
                addColumnValue(columns[j], values[j]);
            }
        }
    }

    public void load(Set<String> includedColumns) throws IOException {
        List<String> fileLines = Files.readAllLines(filepath);
        this.columns = Arrays.stream(fileLines.getFirst().split(splitRegex))
                .map(columnName -> new Column(columnName, includedColumns.contains(columnName)))
                .toArray(Column[]::new);

        for (int i = 1; i < fileLines.size(); i++) {
            String line = fileLines.get(i);
            String[] values = line.split(splitRegex);
            for (int j = 0; j < values.length; j++) {
                addColumnValue(columns[j], values[j]);
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

    public int getColumnCount() {
        return csvContent.size();
    }

    public int getRowCount() {
        if (getColumnCount() == 0) return 0;
        return getColumnValues(columns[0].columnName).size();
    }

    @Override
    public String toString() {
        // Headers
        StringBuilder out = new StringBuilder();
        if (columns == null || columns.length == 0) {
            return "";
        }

        String splitRegexOut = "";
        for (Csv.Column column : columns) {
            out.append(splitRegexOut).append(column.columnName);
            splitRegexOut = this.splitRegex;
        }
        out.append("\n");

        // Rest
        for (int i = 0; i < getRowCount(); i++) {
            splitRegexOut = "";
            for (Csv.Column column : columns) {
                out.append(splitRegexOut).append(getColumnValues(column.columnName).get(i));
                splitRegexOut = this.splitRegex;
            }
            out.append("\n");
        }

        return out.toString();
    }

    public record Column(String columnName, boolean isActive) {}
}