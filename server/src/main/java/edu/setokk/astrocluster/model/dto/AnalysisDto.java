package edu.setokk.astrocluster.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public final class AnalysisDto {
    private long id;
    private String gitProjectName;
    private String gitUrl;
    private List<ClusterResultDto> clusterResults;
}
