package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnalysisService {

    private final AuthService authService;

    public AnalysisService(AuthService authService) {
        this.authService = authService;
    }

    public void saveAnalysis(AnalysisDto analysis) {
        Optional<UserDto> optionalUser = authService.getAuthenticatedUser();
        if (optionalUser.isEmpty()) {
            return;
        }


    }
}
