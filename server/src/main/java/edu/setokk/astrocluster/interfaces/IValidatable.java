package edu.setokk.astrocluster.interfaces;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public interface IValidatable {
    default void validate() {
        var violations = prepareViolations();
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        } else {
            postValidate(); // Request body validation was successful, proceed
        }
    }

    default void postValidate() {
        return;
    }

    Set<? extends ConstraintViolation<IValidatable>> prepareViolations();
}
