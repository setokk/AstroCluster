package edu.setokk.astrocluster.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AnalysisDto {
    private Long id;
    private String gitProjectName;
    private String gitUrl;
    private String projectUUID;
    private String projectLang;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private ZonedDateTime createdDate;
    @Singular("addClusterResult")
    private List<ClusterResultDto> clusterResults;
    @JsonIgnore
    private long userId;
}
