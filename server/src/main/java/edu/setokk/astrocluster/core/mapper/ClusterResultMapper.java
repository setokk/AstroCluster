package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.ClusterResultEntity;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;

public enum ClusterResultMapper implements IMapper<ClusterResultEntity, ClusterResultDto> {
    INSTANCE;

    @Override
    public ClusterResultDto mapToTarget(ClusterResultEntity clusterResultEntity) {
        return ClusterResultDto.builder()
                .id(clusterResultEntity.getId())
                .filename(StringUtils.splitByAndGetLast(clusterResultEntity.getFilepath(), "[\\\\/]"))
                .filepath(clusterResultEntity.getFilepath())
                .clusterLabel(clusterResultEntity.getClusterLabel()).build();
    }

    @Override
    public ClusterResultEntity mapToInitial(ClusterResultDto clusterResultDto) {
        return new ClusterResultEntity(
                clusterResultDto.getId(),
                clusterResultDto.getFilepath(),
                clusterResultDto.getClusterLabel(),
                null
        );
    }
}
