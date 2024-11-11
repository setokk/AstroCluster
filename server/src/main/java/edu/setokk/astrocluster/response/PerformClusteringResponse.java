package edu.setokk.astrocluster.response;

import edu.setokk.astrocluster.core.cluster.PercentagePerCluster;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import jakarta.validation.constraints.NotNull;
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
public final class PerformClusteringResponse {
    @NotNull(message = "analysisData is mandatory")
    private AnalysisDto analysisData;

    @NotNull(message = "percentagesPerCluster is mandatory")
    @Singular("addPercentagePerCluster") private List<PercentagePerCluster> percentagesPerCluster;
}
