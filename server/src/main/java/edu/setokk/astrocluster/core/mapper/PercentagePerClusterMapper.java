package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;

public final class PercentagePerClusterMapper implements IMapper<PercentagePerClusterJpo, PercentagePerClusterDto> {
    private static PercentagePerClusterMapper instance;
    private PercentagePerClusterMapper() {}

    public static PercentagePerClusterMapper instance() {
        if (instance == null)
            instance = new PercentagePerClusterMapper();
        return instance;
    }

    @Override
    public PercentagePerClusterDto mapToTarget(PercentagePerClusterJpo clusterResultJpo) {
        return new PercentagePerClusterDto(
                clusterResultJpo.getId().getClusterLabel(),
                clusterResultJpo.getPercentageInProject()
        );
    }
}
