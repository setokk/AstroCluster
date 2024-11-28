package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.AnalysisEntity;
import edu.setokk.astrocluster.model.ClusterResultEntity;
import edu.setokk.astrocluster.model.UserEntity;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum AnalysisMapper implements IMapper<AnalysisEntity, AnalysisDto> {
    INSTANCE;

    @Override
    public AnalysisDto mapToTarget(AnalysisEntity analysisEntity) {
        // Get last part of the URL and remove .git
        String gitProjectName = StringUtils.splitByAndGetFirst(
                StringUtils.splitByAndGetLast(analysisEntity.getGitUrl(), "\\/"), "\\."
        );
        return AnalysisDto.builder()
                .id(analysisEntity.getId())
                .gitProjectName(gitProjectName)
                .projectUUID(analysisEntity.getProjectUUID())
                .gitUrl(analysisEntity.getGitUrl())
                .projectLang(analysisEntity.getProjectLang())
                .clusterResults(this.mapClusterResultsToTarget(analysisEntity.getClusterResults())).build();
    }

    private List<ClusterResultDto> mapClusterResultsToTarget(Set<ClusterResultEntity> clusterResultsEntity) {
        return clusterResultsEntity.stream()
                .map(ClusterResultMapper.INSTANCE::mapToTarget)
                .collect(Collectors.toList());
    }

    @Override
    public AnalysisEntity mapToInitial(AnalysisDto analysisDto) {
        return new AnalysisEntity(
                analysisDto.getId(),
                analysisDto.getProjectUUID(),
                analysisDto.getGitUrl(),
                analysisDto.getProjectLang(),
                new UserEntity(analysisDto.getUserId())
        );
    }

    public void mapAndAssignClusterResultsToAnalysis(List<ClusterResultDto> clusterResultsDto, AnalysisEntity analysisEntity) {
        analysisEntity.setClusterResults(clusterResultsDto.stream()
                .map(ClusterResultMapper.INSTANCE::mapToInitial)
                .peek(dto -> dto.setAnalysis(analysisEntity))
                .collect(Collectors.toSet()));
    }
}
