package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.analysis.AnalysisHelper;
import edu.setokk.astrocluster.core.file.CsvMessage;
import edu.setokk.astrocluster.core.file.FileMessage;
import edu.setokk.astrocluster.core.file.PdfMessage;
import edu.setokk.astrocluster.core.mapper.AnalysisMapper;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.AnalysisEntity;
import edu.setokk.astrocluster.model.dto.AnalysisDto;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.AnalysisRepository;
import edu.setokk.astrocluster.repository.PercentagePerClusterRepository;
import edu.setokk.astrocluster.request.InterestPdfAnalysisRequest;
import edu.setokk.astrocluster.util.Csv;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public AnalysisDto getAnalysis(long analysisId) {
        UserDto user = authService.getAuthenticatedUser();

        AnalysisEntity analysisEntity = analysisRepository
                .findByIdAndUserId(analysisId, user.getId())
                .orElseThrow(() -> new BusinessLogicException(HttpStatus.NOT_FOUND, "Analysis with id=" + analysisId + " does not exist."));
        return AnalysisMapper.INSTANCE.mapToTarget(analysisEntity);
    }

    public List<AnalysisDto> getLatestAnalyses() {
        UserDto user = authService.getAuthenticatedUser();
        return analysisRepository.findLatestAnalysesOfUser(user.getId())
                .stream()
                .map(AnalysisMapper.INSTANCE::mapToTargetWithoutClusterResults)
                .toList();
    }

    public FileMessage generateInterestResultsFileForAnalysis(InterestPdfAnalysisRequest requestBody, FileMessage fileMessage) throws IOException {
        AnalysisDto analysisDto = getAnalysis(requestBody.getAnalysisId());
        Csv interestResultsCsv = interestService.calculateInterestCsv(analysisDto, requestBody);
        return switch (fileMessage) {
            case PdfMessage ignored -> pdfService.generatePdfFromCsv(interestResultsCsv);
            case CsvMessage ignored -> {
                byte[] csvBytes = interestResultsCsv.toString().getBytes(StandardCharsets.UTF_8);
                yield new CsvMessage(csvBytes, "interest-results-csv.csv");
            }
            default -> throw new IllegalStateException("Unexpected type: " + fileMessage);
        };
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
        percentagePerClusterRepository.saveAll(AnalysisHelper.calculatePercentagesPerCluster(analysisDto.getClusterResults(), analysisEntity));

        return AnalysisMapper.INSTANCE.mapToTarget(analysisEntity);
    }
}
