package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.analysis.AnalysisHelper;
import edu.setokk.astrocluster.core.mapper.AnalysisMapper;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.AnalysisRepository;
import edu.setokk.astrocluster.repository.PercentagePerClusterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {

    private final AuthService authService;
    private final AnalysisRepository analysisRepository;
    private final PercentagePerClusterRepository percentagePerClusterRepository;

    public AnalysisService(AuthService authService,
                           AnalysisRepository analysisRepository,
                           PercentagePerClusterRepository percentagePerClusterRepository) {
        this.authService = authService;
        this.analysisRepository = analysisRepository;
        this.percentagePerClusterRepository = percentagePerClusterRepository;
    }

    @Transactional
    public AnalysisDto saveAnalysis(AnalysisDto analysisDto) {
        UserDto user = authService.getAuthenticatedUser();
        analysisDto.setUserId(user.getId());

        // Save analysis object
        AnalysisJpo analysisJpo = AnalysisMapper.INSTANCE.mapToInitial(analysisDto);
        analysisRepository.save(analysisJpo);
        AnalysisMapper.INSTANCE.mapAndAssignClusterResultsToAnalysis(analysisDto.getClusterResults(), analysisJpo);

        // Save percentage per cluster list
        percentagePerClusterRepository.saveAll(AnalysisHelper.calculatePercentagesPerCluster(analysisDto.getClusterResults(), analysisJpo.getId()));

        return AnalysisMapper.INSTANCE.mapToTarget(analysisJpo);
    }

    public AnalysisDto getAnalysis(long analysisId) {
        UserDto user = authService.getAuthenticatedUser();

        AnalysisJpo analysisJpo = analysisRepository
                .findByIdAndUserId(analysisId, user.getId())
                .orElseThrow(() -> new BusinessLogicException(HttpStatus.NOT_FOUND, "Analysis with id=" + analysisId + " does not exist."));
        return AnalysisMapper.INSTANCE.mapToTarget(analysisJpo);
    }
}
