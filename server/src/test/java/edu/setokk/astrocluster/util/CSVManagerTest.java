package edu.setokk.astrocluster.util;

import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class CSVManagerTest {

    @Test
    void loadInput() throws IOException {
        Path csvPath = Paths.get(System.getProperty("user.dir") + "/src/test/java/edu/setokk/astrocluster/util/analysis.csv");
        Csv csv = new Csv("A title", Paths.get("A filename"), ",");
        csv.load(Collections.setOf("SIZE1", "SIZE2"));
    }
}