package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.error.IValidatable;
import edu.setokk.astrocluster.util.SupportedLanguages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public final class PerformClusteringRequest implements IValidatable {
    @NotNull(message = "gitUrl field is mandatory")
    private String gitUrl;
    @NotNull(message = "lang field is mandatory")
    private String lang;
    private List<String> extensions;

    @Override
    public void postValidate() throws BusinessLogicException {
        BusinessLogicException e = new BusinessLogicException(HttpStatus.BAD_REQUEST);
        if (extensions == null || extensions.isEmpty()) {
            var supportedLanguage = SupportedLanguages.get(lang);
            if (supportedLanguage.isPresent()) {
                extensions = supportedLanguage.get().getBasicExtensions();
            } else {
                e.addErrorMessage("lang=\"" + lang + "\" is not supported.");
            }
        }
        if (e.hasErrorMessages()) throw e;
    }
}
