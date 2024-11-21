package edu.setokk.astrocluster.response;

import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class GetAnalysisResponse {
    @NotNull(message = "analysisData is mandatory")
    private AnalysisDto analysisData;

    @NotNull(message = "percentagesPerCluster is mandatory")
    @Singular("addPercentagePerCluster") private List<PercentagePerClusterDto> percentagesPerCluster;
}
