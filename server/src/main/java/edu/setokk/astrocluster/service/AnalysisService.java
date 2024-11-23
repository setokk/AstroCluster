package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.mapper.AnalysisMapper;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.UserJpo;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.AnalysisRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AnalysisService {

    private final AuthService authService;
    private final AnalysisRepository analysisRepository;

    public AnalysisService(AuthService authService, AnalysisRepository analysisRepository) {
        this.authService = authService;
        this.analysisRepository = analysisRepository;
    }

    @Transactional
    public AnalysisDto saveAnalysis(AnalysisDto analysisDto) {
        UserDto user = authService.getAuthenticatedUser();
        analysisDto.setUserId(user.getId());

        AnalysisJpo analysisJpo = AnalysisMapper.INSTANCE.mapToInitial(analysisDto);
        analysisRepository.save(analysisJpo);
        AnalysisMapper.INSTANCE.mapAndAssignClusterResultsToAnalysis(analysisDto.getClusterResults(), analysisJpo);
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
