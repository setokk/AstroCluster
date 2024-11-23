package edu.setokk.astrocluster.core.mapper.grpc;

import edu.setokk.astrocluster.core.mapper.IMapper;
import edu.setokk.astrocluster.grpc.ClusterResponse;
import edu.setokk.astrocluster.model.dto.AnalysisDto;

public enum AnalysisGrpcMapper implements IMapper<ClusterResponse, AnalysisDto> {
    INSTANCE;

    @Override
    public AnalysisDto mapToTarget(ClusterResponse clusterResponse) {
        return AnalysisDto.builder()
                .clusterResults(this.mapClusterResultsToTarget(analysisJpo.getClusterResults())).build();
    }
}
