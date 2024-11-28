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
import java.util.Optional;
import java.util.Set;

@Getter
public final class Csv {
    private String title;
    private Path filepath;
    private Header[] headers;
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
        this.headers = Arrays.stream(fileLines.getFirst().split(splitRegex))
                .map(columnName -> new Header(columnName, true))
                .toArray(Header[]::new);

        for (String line : fileLines) {
            String[] values = line.split(splitRegex);
            for (int i = 0; i < values.length; i++) {
                addColumnValue(headers[i], values[i]);
            }
        }
    }

    public void load(Set<String> includedColumns) throws IOException {
        List<String> fileLines = Files.readAllLines(filepath);
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

    public Optional<Header[]> getHeaders() {
        return Optional.ofNullable(headers);
    }

    public record Header(String columnName, boolean isActive) {}
}