package edu.setokk.astrocluster.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AnalysisDto {
    private long id;
    private String gitProjectName;
    private String gitUrl;
    private String projectUUID;
    @Singular("addClusterResult") private List<ClusterResultDto> clusterResults;
}
