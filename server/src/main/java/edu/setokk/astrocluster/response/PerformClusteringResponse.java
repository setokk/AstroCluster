package edu.setokk.astrocluster.response;

import edu.setokk.astrocluster.core.cluster.PercentagePerCluster;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public final class PerformClusteringResponse {
    @NotNull
    private AnalysisDto analysisData;

    @NotNull
    private List<PercentagePerCluster> percentagesPerCluster;
}
