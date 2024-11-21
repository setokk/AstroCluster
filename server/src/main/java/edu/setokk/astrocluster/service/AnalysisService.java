package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.mapper.AnalysisMapper;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.AnalysisJpo;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.AnalysisRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnalysisService {

    private final AuthService authService;
    private final AnalysisRepository analysisRepository;

    public AnalysisService(AuthService authService, AnalysisRepository analysisRepository) {
        this.authService = authService;
        this.analysisRepository = analysisRepository;
    }

    public void saveAnalysis(AnalysisDto analysisDto) {
        Optional<UserDto> optionalUser = authService.getAuthenticatedUser();
        if (optionalUser.isEmpty()) {
            return;
        }
        AnalysisJpo analysisToBeSaved = AnalysisMapper.INSTANCE.mapToInitial(analysisDto);
        analysisRepository.save(analysisToBeSaved);
    }

    public AnalysisDto getAnalysis(long id) {
        AnalysisJpo analysisJpo = analysisRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(HttpStatus.NOT_FOUND, "Analysis with id=" + id + " does not exist."));
        return AnalysisMapper.INSTANCE.mapToTarget(analysisJpo);
    }
}
