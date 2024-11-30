package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.core.enums.SimilarFilesCriteria;
import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.validation.IValidatable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class InterestPdfAnalysisRequest implements IValidatable {
    @NotNull(message = "[InterestPdfAnalysisRequest]: analysisId field is mandatory")
    private final Integer analysisId;
    @NotNull(message = "[InterestPdfAnalysisRequest]: similarFilesCriteria field is mandatory")
    @NotEmpty(message = "[InterestPdfAnalysisRequest]: similarFilesCriteria field cannot be empty")
    private final String similarFilesCriteria;
    @NotNull(message = "[InterestPdfAnalysisRequest]: isDescriptive field is mandatory")
    private final Boolean isDescriptive;
    private Double avgPerGenerationLOC;
    private Double perHourLOC;
    private Double perHourSalary;

    @Override
    public void postValidate() throws BusinessLogicException {
        BusinessLogicException e = new BusinessLogicException(HttpStatus.BAD_REQUEST);

        /* SimilarFilesCriteria field post validate */
        if (SimilarFilesCriteria.get(similarFilesCriteria).isEmpty()) {
            e.addErrorMessage(errorPrefix() + " similarFilesCriteria=" + similarFilesCriteria + " is not a valid value");
        }

        /* AvgPerGenerationLOC field post validate */
        avgPerGenerationLOC = (avgPerGenerationLOC == null) ? 40 : avgPerGenerationLOC;

        /* PerHourLOC field post validate */
        perHourLOC = (perHourLOC == null) ? 25 : perHourLOC;

        /* PerHourSalary field post validate */
        perHourSalary = (perHourSalary == null) ? 45 : perHourSalary;

        if (e.hasErrorMessages()) throw e;
    }
}
