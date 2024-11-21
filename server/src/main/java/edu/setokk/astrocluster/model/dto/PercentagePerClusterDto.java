package edu.setokk.astrocluster.model.dto;

/**
 * Record class used to hold information
 * @param clusterLabel label of the cluster (integer)
 * @param percentageInProject percentage of this cluster in the project (double)
 */
public record PercentagePerClusterDto(int clusterLabel, double percentageInProject) {}
