package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.ClusterResultJpo;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;

public enum ClusterResultMapper implements IMapper<ClusterResultJpo, ClusterResultDto> {
    INSTANCE;

    @Override
    public ClusterResultDto mapToTarget(ClusterResultJpo clusterResultJpo) {
        return ClusterResultDto.builder()
                .id(clusterResultJpo.getId())
                .filename(StringUtils.splitByAndGetLast(clusterResultJpo.getFilepath(), "[\\\\/]"))
                .filepath(clusterResultJpo.getFilepath())
                .clusterLabel(clusterResultJpo.getClusterLabel()).build();
    }

    @Override
    public ClusterResultJpo mapToInitial(ClusterResultDto clusterResultDto) {
        return new ClusterResultJpo(
                clusterResultDto.getId(),
                clusterResultDto.getFilepath(),
                clusterResultDto.getClusterLabel(),
                null
        );
    }
}
