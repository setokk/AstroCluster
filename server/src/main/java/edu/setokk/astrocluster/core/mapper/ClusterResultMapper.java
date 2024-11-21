package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.ClusterResultJpo;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;

public final class ClusterResultMapper implements IMapper<ClusterResultJpo, ClusterResultDto> {
    private static ClusterResultMapper instance;
    private ClusterResultMapper() {}

    public static ClusterResultMapper instance() {
        if (instance == null)
            instance = new ClusterResultMapper();
        return instance;
    }

    @Override
    public ClusterResultDto mapToTarget(ClusterResultJpo clusterResultJpo) {
        return ClusterResultDto.builder()
                .id(clusterResultJpo.getId())
                .filename(StringUtils.splitByAndGetLast(clusterResultJpo.getFilepath(), "[\\\\/]"))
                .filepath(clusterResultJpo.getFilepath())
                .clusterLabel(clusterResultJpo.getClusterLabel()).build();
    }
}
