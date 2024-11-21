package edu.setokk.astrocluster.core.mapper;

import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.ClusterResultJpo;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.ClusterResultDto;
import edu.setokk.astrocluster.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class AnalysisMapper implements IMapper<AnalysisJpo, AnalysisDto> {
    private static AnalysisMapper instance;
    private AnalysisMapper() {}

    public static AnalysisMapper instance() {
        if (instance == null)
            instance = new AnalysisMapper();
        return instance;
    }

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
                .clusterResults(mapClusterResultsToTarget(analysisJpo.getClusterResults())).build();
    }

    private List<ClusterResultDto> mapClusterResultsToTarget(Set<ClusterResultJpo> clusterResultsJpo) {
        return clusterResultsJpo.stream()
                .map(jpo -> ClusterResultMapper.instance().mapToTarget(jpo))
                .collect(Collectors.toList());
    }
}
