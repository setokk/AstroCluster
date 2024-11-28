package edu.setokk.astrocluster.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum SimilarFilesCriteria {
    NORMAL("Normal", "Normal Criteria"),
    CLUSTER("Cluster", "Cluster Criteria");

    private final String id;
    private final String value;

    public static Optional<SimilarFilesCriteria> get(@NonNull String id) {
        return Arrays.stream(values())
                .filter(l -> id.equals(l.getId()))
                .findAny();
    }
}
