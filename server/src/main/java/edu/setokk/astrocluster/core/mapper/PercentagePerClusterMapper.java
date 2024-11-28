package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.PercentagePerClusterEntity;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum PercentagePerClusterMapper implements IMapper<PercentagePerClusterEntity, PercentagePerClusterDto> {
    INSTANCE;

    @Override
    public PercentagePerClusterDto mapToTarget(PercentagePerClusterEntity clusterResultEntity) {
        return new PercentagePerClusterDto(
                clusterResultEntity.getId().getClusterLabel(),
                clusterResultEntity.getPercentageInProject()
        );
    }

    public List<PercentagePerClusterDto> mapPercentagePerClusters(Set<PercentagePerClusterEntity> clusterResultsEntity) {
        return clusterResultsEntity.stream()
                .map(this::mapToTarget)
                .collect(Collectors.toList());
    }
}
