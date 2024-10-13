package edu.setokk.astrocluster.cluster;

import edu.setokk.astrocluster.error.AstroConstraintViolation;
import edu.setokk.astrocluster.interfaces.IValidatable;
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
    public Set<? extends ConstraintViolation<IValidatable>> prepareViolations() {
        var violations = new HashSet<ConstraintViolation<IValidatable>>();
        if (extensions.isEmpty()) {
            violations.add(new AstroConstraintViolation<>("extensions must have at least one element", this, "extensions"));
        }
        return violations;
    }
}
