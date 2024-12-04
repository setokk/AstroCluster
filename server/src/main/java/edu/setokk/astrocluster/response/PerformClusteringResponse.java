package edu.setokk.astrocluster.response;

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
public final class PerformClusteringResponse {
    private boolean isAsync;
    private Short ack;
    private Long analysisId;
}
