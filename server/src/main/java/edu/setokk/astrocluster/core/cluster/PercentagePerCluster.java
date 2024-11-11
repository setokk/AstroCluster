package edu.setokk.astrocluster.core.cluster;

/**
 * Record class used to hold information
 * @param clusterLabel label of the cluster (integer)
 * @param percentageInProject percentage of this cluster in the project (double)
 */
public record PercentagePerCluster(int clusterLabel, double percentageInProject) {}
