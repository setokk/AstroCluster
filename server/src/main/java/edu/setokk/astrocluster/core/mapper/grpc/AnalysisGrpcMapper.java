package edu.setokk.astrocluster.core.mapper.grpc;

import edu.setokk.astrocluster.core.mapper.IMapper;
import edu.setokk.astrocluster.grpc.ClusterResponse;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.AnalysisDto.AnalysisDtoBuilder;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum AnalysisGrpcMapper implements IMapper<ClusterResponse, AnalysisDtoBuilder> {
    INSTANCE;

    @Override
    public AnalysisDtoBuilder mapToTarget(ClusterResponse clusterResponse) {
        return AnalysisDto.builder()
                .clusterResults(
                        this.mapClusterResultsToTarget(
                                clusterResponse.getClusterLabelsList(), clusterResponse.getFilePathsList()
                        )
                );
    }

    private List<ClusterResultDto> mapClusterResultsToTarget(List<Long> clusterLabels, List<String> filePaths) {
        return IntStream.range(0, filePaths.size())
                .mapToObj(i -> ClusterResultDto.builder()
                        .filepath(filePaths.get(i))
                        .filename(StringUtils.splitByAndGetLast(filePaths.get(i), "[\\\\/]"))
                        .clusterLabel(Math.toIntExact(clusterLabels.get(i)))
                        .build())
                .collect(Collectors.toList());
    }
}
