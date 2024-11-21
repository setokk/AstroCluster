package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.ClusterResultJpo;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;
import io.jsonwebtoken.lang.Collections;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum AnalysisMapper implements IMapper<AnalysisJpo, AnalysisDto> {
    INSTANCE;

    @Override
    public AnalysisDto mapToTarget(AnalysisJpo analysisJpo) {
        // Get last part of the URL and remove .git
        String gitProjectName = StringUtils.splitByAndGetFirst(
                StringUtils.splitByAndGetLast(analysisJpo.getGitUrl(), "\\/"), "\\."
        );
        return AnalysisDto.builder()
                .id(analysisJpo.getId())
                .gitProjectName(gitProjectName)
                .gitUrl(analysisJpo.getGitUrl())
                .clusterResults(this.mapClusterResultsToTarget(analysisJpo.getClusterResults())).build();
    }

    @Override
    public AnalysisJpo mapToInitial(AnalysisDto analysisDto) {
        return new AnalysisJpo(
                analysisDto.getId(),
                analysisDto.getProjectUUID(),
                analysisDto.getGitUrl(),
                this.mapClusterResultsToInitial(analysisDto.getClusterResults(), analysisDto.getId())
        );
    }

    private List<ClusterResultDto> mapClusterResultsToTarget(Set<ClusterResultJpo> clusterResultsJpo) {
        return clusterResultsJpo.stream()
                .map(ClusterResultMapper.INSTANCE::mapToTarget)
                .collect(Collectors.toList());
    }

    private Set<ClusterResultJpo> mapClusterResultsToInitial(List<ClusterResultDto> clusterResultsDto, Long analysisId) {
        return clusterResultsDto.stream()
                .map(ClusterResultMapper.INSTANCE::mapToInitial)
                .peek(dto -> dto.setAnalysis(new AnalysisJpo(analysisId, null, null, null)))
                .collect(Collectors.toSet());
    }
}
