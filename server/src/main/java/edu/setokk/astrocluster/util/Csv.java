package edu.setokk.astrocluster.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
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

    public void save(String oSplitRegex) {
        // Headers
        String splitRegexOut = "";
        StringBuilder out = new StringBuilder();
        if (columns == null || columns.length == 0) {
            return;
        }

        for (Csv.Column column : columns) {
            out.append(splitRegexOut).append(column.columnName);
            splitRegexOut = (oSplitRegex == null) ? this.splitRegex : oSplitRegex;
        }
        out.append("\n");

        // Rest
        for (int i = 0; i < getRowCount(); i++) {
            splitRegexOut = "";
            for (Csv.Column column : columns) {
                out.append(splitRegexOut).append(getColumnValues(column.columnName).get(i));
                splitRegexOut = (oSplitRegex == null) ? this.splitRegex : oSplitRegex;
            }
            out.append("\n");
        }

        try {
            Files.write(filepath, out.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
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

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public record Column(String columnName, boolean isActive) {}
}