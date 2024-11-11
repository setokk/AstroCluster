package edu.setokk.astrocluster.model.dto;

import lombok.*;

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
    @Singular("addClusterResult") private List<ClusterResultDto> clusterResults;
}
