package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.analysis.AnalysisHelper;
import edu.setokk.astrocluster.core.enums.SimilarFilesCriteria;
import edu.setokk.astrocluster.core.interest.SimilarFilesClusterStrategy;
import edu.setokk.astrocluster.core.interest.SimilarFilesNormalStrategy;
import edu.setokk.astrocluster.core.interest.SimilarFilesStrategy;
import edu.setokk.astrocluster.core.mapper.AnalysisMapper;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.AnalysisEntity;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.AnalysisRepository;
import edu.setokk.astrocluster.repository.PercentagePerClusterRepository;
import edu.setokk.astrocluster.request.InterestPdfAnalysisRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {

    private final AuthService authService;
    private final InterestService interestService;
    private final PdfService pdfService;
    private final AnalysisRepository analysisRepository;
    private final PercentagePerClusterRepository percentagePerClusterRepository;

    public AnalysisService(AuthService authService,
                           InterestService interestService,
                           PdfService pdfService,
                           AnalysisRepository analysisRepository,
                           PercentagePerClusterRepository percentagePerClusterRepository) {
        this.authService = authService;
        this.interestService = interestService;
        this.pdfService = pdfService;
        this.analysisRepository = analysisRepository;
        this.percentagePerClusterRepository = percentagePerClusterRepository;
    }

    @Transactional
    public AnalysisDto saveAnalysis(AnalysisDto analysisDto) {
        UserDto user = authService.getAuthenticatedUser();
        analysisDto.setUserId(user.getId());

        // Save analysis object
        AnalysisEntity analysisEntity = AnalysisMapper.INSTANCE.mapToInitial(analysisDto);
        analysisRepository.save(analysisEntity);
        AnalysisMapper.INSTANCE.mapAndAssignClusterResultsToAnalysis(analysisDto.getClusterResults(), analysisEntity);

        // Save percentage per cluster list
        percentagePerClusterRepository.saveAll(AnalysisHelper.calculatePercentagesPerCluster(analysisDto.getClusterResults(), analysisEntity.getId()));

        return AnalysisMapper.INSTANCE.mapToTarget(analysisEntity);
    }

    public AnalysisDto getAnalysis(long analysisId) {
        UserDto user = authService.getAuthenticatedUser();

        AnalysisEntity analysisEntity = analysisRepository
                .findByIdAndUserId(analysisId, user.getId())
                .orElseThrow(() -> new BusinessLogicException(HttpStatus.NOT_FOUND, "Analysis with id=" + analysisId + " does not exist."));
        return AnalysisMapper.INSTANCE.mapToTarget(analysisEntity);
    }

    public byte[] generateInterestPdfForAnalysis(InterestPdfAnalysisRequest requestBody) {
        // Get analysis
        AnalysisDto analysisDto = getAnalysis(requestBody.getAnalysisId());

        // Calculate interest
        SimilarFilesCriteria similarFilesCriteria = SimilarFilesCriteria.get(requestBody.getSimilarFilesCriteria()).get();
        SimilarFilesStrategy similarFilesStrategy;
        if (SimilarFilesCriteria.NORMAL.equals(similarFilesCriteria)) {
            similarFilesStrategy = new SimilarFilesNormalStrategy();
        } else {
            similarFilesStrategy = new SimilarFilesClusterStrategy();
        }
        interestService.calculateInterest(analysisDto, similarFilesStrategy);
        pdfService.generatePdf();
    }
}
