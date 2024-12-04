package edu.setokk.astrocluster.response;

import edu.setokk.astrocluster.model.dto.AnalysisDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class GetLatestAnalysesResponse {
    private List<AnalysisDto> latestAnalyses;
}
