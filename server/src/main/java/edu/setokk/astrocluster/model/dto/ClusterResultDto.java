package edu.setokk.astrocluster.model.dto;

import lombok.*;

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
