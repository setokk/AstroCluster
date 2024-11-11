package edu.setokk.astrocluster.error;

/**
 * This interface <b>should</b> be implemented by all classes that represent the request bodies of all endpoints.
 * <br/><br/>
 * It provides an easy way for request data to be validated 
 * just by implementing {@link #prepareErrors()} and {@link #postValidate()} methods.
 */
public interface IValidatable {
    default void validate() throws BusinessLogicException {
        prepareErrors();
        postValidate(); // Request body validation was successful, proceed with post validation actions
    }

    /**
     * Checks for trivial errors (empty lists, field lengths etc.)
     * @throws BusinessLogicException if any error in the request body was detected
     */
    default void prepareErrors() throws BusinessLogicException {
    }

    /**
     * Performs various actions if {@link #prepareErrors()} was successful (no errors were found).
     * <br/><br/>
     * Examples of such actions are:
     * <ul>
     *  <li>Set default values for unspecified fields</li>
     *  <li>Check if values correspond to certain enums</li>
     * </ul>
     * @throws BusinessLogicException if any error in the values themselves was found (ex. value does not exist for enum)
     */
    default void postValidate() throws BusinessLogicException {
    }

    default String errorPrefix() {
        return "[" + getClass().getSimpleName() + "]:";
    }
}
