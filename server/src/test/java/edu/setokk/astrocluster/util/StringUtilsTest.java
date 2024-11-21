package edu.setokk.astrocluster.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {

    @Test
    public void splitByAndGetLastTest1() {
        String last = StringUtils.splitByAndGetLast("https://github.com/setokk/AstroCluster.git", "\\/");
        assertEquals(last, "AstroCluster.git");
    }

    @Test
    public void splitByAndGetLastTest2() {
        String last = StringUtils.splitByAndGetLast("https://github.com/setokk/AstroCluster", "\\/");
        assertEquals(last, "AstroCluster");
    }

    @Test
    public void splitByAndGetFirstTest1() {
        String first = StringUtils.splitByAndGetFirst("AstroCluster.git", "\\.");
        assertEquals(first, "AstroCluster");
    }

    @Test
    public void splitByAndGetFirstTest2() {
        String first = StringUtils.splitByAndGetFirst("AstroCluster", "\\.");
        assertEquals(first, "AstroCluster");
    }
}
