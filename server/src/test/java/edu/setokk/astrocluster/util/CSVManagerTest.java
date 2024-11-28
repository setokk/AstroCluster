package edu.setokk.astrocluster.util;

import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CSVManagerTest {

    @Test
    void loadInput() throws IOException {
        Path csvPath = Paths.get(System.getProperty("user.dir") + "/src/test/java/edu/setokk/astrocluster/util/analysis.csv");
        CSVManager csvManager = new CSVManager(",");
        csvManager.load(csvPath, Collections.setOf("SIZE1", "SIZE2"));
    }
}