package edu.setokk.astrocluster.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

import edu.setokk.astrocluster.error.BusinessLogicException;

public interface IValidatable {
    default void validate() throws BusinessLogicException {
        prepareViolations();
        postValidate(); // Request body validation was successful, proceed with post validation actions
    }

    void prepareViolations() throws BusinessLogicException;

    default void postValidate() throws BusinessLogicException {
        return;
    }
}
