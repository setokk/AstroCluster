package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.error.IValidatable;
import edu.setokk.astrocluster.core.enums.SupportedClusteringParadigms;
import edu.setokk.astrocluster.core.enums.SupportedLanguages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public final class PerformClusteringRequest implements IValidatable {
    @NotNull(message = "[PerformClusteringRequest]: gitUrl field is mandatory")
    private String gitUrl;

    @NotNull(message = "[PerformClusteringRequest]: lang field is mandatory")
    private String lang;

    @NotNull(message = "[PerformClusteringRequest]: clusteringParadigm field is mandatory")
    private String clusteringParadigm;

    private List<String> extensions;

    @Override
    public void postValidate() throws BusinessLogicException {
        BusinessLogicException e = new BusinessLogicException(HttpStatus.BAD_REQUEST);

        /* Extensions field post validate */
        if (extensions == null || extensions.isEmpty()) {
            var supportedLanguage = SupportedLanguages.get(lang);
            if (supportedLanguage.isPresent()) {
                extensions = supportedLanguage.get().getBasicExtensions();
            } else {
                e.addErrorMessage(errorPrefix() + " lang='" + lang + "' is not supported.");
            }
        }

        /* Clustering Paradigm field post validate */
        if (SupportedClusteringParadigms.get(clusteringParadigm).isEmpty()) {
            e.addErrorMessage(errorPrefix() + " clusteringParadigm='" + clusteringParadigm + "' is not supported.");
        }

        if (e.hasErrorMessages()) throw e;
    }
}
