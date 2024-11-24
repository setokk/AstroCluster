package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.ClusterResultJpo;
import edu.setokk.astrocluster.model.UserJpo;
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
                .projectUUID(analysisJpo.getProjectUUID())
                .gitUrl(analysisJpo.getGitUrl())
                .projectLang(analysisJpo.getProjectLang())
                .clusterResults(this.mapClusterResultsToTarget(analysisJpo.getClusterResults())).build();
    }

    private List<ClusterResultDto> mapClusterResultsToTarget(Set<ClusterResultJpo> clusterResultsJpo) {
        return clusterResultsJpo.stream()
                .map(ClusterResultMapper.INSTANCE::mapToTarget)
                .collect(Collectors.toList());
    }

    @Override
    public AnalysisJpo mapToInitial(AnalysisDto analysisDto) {
        return new AnalysisJpo(
                analysisDto.getId(),
                analysisDto.getProjectUUID(),
                analysisDto.getGitUrl(),
                analysisDto.getProjectLang(),
                new UserJpo(analysisDto.getUserId())
        );
    }

    public void mapAndAssignClusterResultsToAnalysis(List<ClusterResultDto> clusterResultsDto, AnalysisJpo analysisJpo) {
        analysisJpo.setClusterResults(clusterResultsDto.stream()
                .map(ClusterResultMapper.INSTANCE::mapToInitial)
                .peek(dto -> dto.setAnalysis(analysisJpo))
                .collect(Collectors.toSet()));
    }
}
