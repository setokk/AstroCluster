package edu.setokk.astrocluster.util;

import java.io.File;

public final class IOUtils {
    public static final String ROOT_DIR = System.getProperty("user.dir") + File.separator;
    public static final String PROJECTS_DIR = ROOT_DIR + "projects" + File.separator;
    public static final String INTEREST_CSV_PATH = PROJECTS_DIR + File.separator + "%s" + File.separator + "%s-%s.csv";
    public static final String METRICS_CSV_PATH = PROJECTS_DIR + File.separator + "%s" + File.separator + "metrics.csv";
}
