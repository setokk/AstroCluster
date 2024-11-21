package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;

public enum PercentagePerClusterMapper implements IMapper<PercentagePerClusterJpo, PercentagePerClusterDto> {
    INSTANCE;

    @Override
    public PercentagePerClusterDto mapToTarget(PercentagePerClusterJpo clusterResultJpo) {
        return new PercentagePerClusterDto(
                clusterResultJpo.getId().getClusterLabel(),
                clusterResultJpo.getPercentageInProject()
        );
    }
}
