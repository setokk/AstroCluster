package edu.setokk.astrocluster.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ClusterResultDto {
    private long id;
    private String filename;
    private String filepath;
    private int clusterLabel;
}
