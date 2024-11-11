package edu.setokk.astrocluster.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum SupportedClusteringParadigms {
    MVC("MVC", "Model-View-Controller");

    private final String id;
    private final String value;

    public static Optional<SupportedClusteringParadigms> get(@NonNull String id) {
        return Arrays.stream(values())
                .filter(c -> id.equals(c.getId()))
                .findAny();
    }
}
