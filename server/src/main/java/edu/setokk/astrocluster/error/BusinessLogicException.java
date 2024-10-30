package edu.setokk.astrocluster.error;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final List<String> errorMessages;

    public BusinessLogicException(HttpStatus httpStatus) {
        super("[ERROR]: AstroCluster Business Logic Exception");
        this.httpStatus = httpStatus;
        this.errorMessages = new ArrayList<>();
    }

    public BusinessLogicException(HttpStatus httpStatus, String errorMessage) {
        super("[ERROR]: AstroCluster Business Logic Exception");
        this.httpStatus = httpStatus;
        this.errorMessages = Collections.singletonList(errorMessage);
    }

    public boolean hasErrorMessages() {
        return !this.errorMessages.isEmpty();
    }

    public void addErrorMessage(String errorMessage) {
        this.errorMessages.add(errorMessage);
    }

    public List<String> getErrorMessages() {
        return this.errorMessages;
    }
}
