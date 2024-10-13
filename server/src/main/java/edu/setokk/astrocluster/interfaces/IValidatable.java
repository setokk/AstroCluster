package edu.setokk.astrocluster.interfaces;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public interface IValidatable {
    default void validateAndThrowErrors() {
        prepareViolations();
        if (!violations().isEmpty()) {
            throw new ConstraintViolationException(violations());
        }
    }

    void prepareViolations();

    Set<? extends ConstraintViolation<IValidatable>> violations();
}
