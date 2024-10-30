package edu.setokk.astrocluster.error;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.http.HttpStatus;

/**
 * Custom {@link RuntimeException} type used for business logic exceptions.
 * <br/>
 * <br/>
 * These include: <br/><br/>
 * <ul>
 *  <li>Validation errors, in which case the possible errors <b>can be more than 1.</b></li>
 *  <li>Database or service errors, in which case the possible errors <b>can be at maximum 1.</b></li>
 * </ul>
 */
public class BusinessLogicException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final List<String> errorMessages;

    /**
     * 
     * @param httpStatus the HTTP status to be returned in the response
     */
    public BusinessLogicException(HttpStatus httpStatus) {
        super("[ERROR]: AstroCluster Business Logic Exception");
        this.httpStatus = httpStatus;
        this.errorMessages = new ArrayList<>();
    }

    /**
     * Constructor for a single error message.
     * @param httpStatus the HTTP status to be returned in the response
     * @param errorMessage a message describing the error
     */
    public BusinessLogicException(HttpStatus httpStatus, String errorMessage) {
        super("[ERROR]: AstroCluster Business Logic Exception");
        this.httpStatus = httpStatus;
        this.errorMessages = Collections.singletonList(errorMessage);
    }

    /**
     * Checks if any errors are found at the current state of the object.
     * @return <b>true</b> if at least 1 error was found. Otherwise, <b>returns false</b>.
     */
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
