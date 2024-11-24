package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum PercentagePerClusterMapper implements IMapper<PercentagePerClusterJpo, PercentagePerClusterDto> {
    INSTANCE;

    @Override
    public PercentagePerClusterDto mapToTarget(PercentagePerClusterJpo clusterResultJpo) {
        return new PercentagePerClusterDto(
                clusterResultJpo.getId().getClusterLabel(),
                clusterResultJpo.getPercentageInProject()
        );
    }

    public List<PercentagePerClusterDto> mapPercentagePerClusters(Set<PercentagePerClusterJpo> clusterResultsJpo) {
        return clusterResultsJpo.stream()
                .map(this::mapToTarget)
                .collect(Collectors.toList());
    }
}
