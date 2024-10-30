package edu.setokk.astrocluster.cluster;

import edu.setokk.astrocluster.error.IValidatable;
import edu.setokk.astrocluster.util.SupportedLanguages;
import edu.setokk.astrocluster.error.BusinessLogicException;
import org.springframework.http.HttpStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClusterRequestBody implements IValidatable {
    @NotNull(message = "gitUrl field is mandatory")
    private String gitUrl;
    @NotNull(message = "lang field is mandatory")
    private String lang;
    @NotNull(message = "extensions field is mandatory")
    private List<String> extensions;

    @Override
    public void prepareErrors() throws BusinessLogicException {
        BusinessLogicException e = new BusinessLogicException(HttpStatus.BAD_REQUEST);
        if (extensions.isEmpty()) {
            e.addErrorMessage("'extensions' field is mandatory");
        }
        if (e.hasErrorMessages()) throw e;
    }

    @Override
    public void postValidate() throws BusinessLogicException {
        BusinessLogicException e = new BusinessLogicException(HttpStatus.BAD_REQUEST);
        if (extensions.isEmpty()) {
            var supportedLanguage = SupportedLanguages.get(lang);
            if (supportedLanguage.isPresent()) {
                extensions = SupportedLanguages.get(lang).getBasicExtensions();
            } else {
                e.addErrorMessage("'lang' field=" + lang + " is not supported.");
            }
        }
        if (e.hasErrorMessages()) throw e;
    }
}
