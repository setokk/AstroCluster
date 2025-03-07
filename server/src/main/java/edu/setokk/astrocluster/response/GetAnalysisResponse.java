package edu.setokk.astrocluster.response;

import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;
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
public final class GetAnalysisResponse {
    private AnalysisDto analysisData;
    @Singular("addPercentagePerCluster") private List<PercentagePerClusterDto> percentagesPerCluster;
}
