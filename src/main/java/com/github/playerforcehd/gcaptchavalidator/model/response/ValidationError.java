package com.github.playerforcehd.gcaptchavalidator.model.response;

/**
 * Provide a list of all possible error codes that a validation response can contain.
 *
 * The specific meaning of the error codes can be looked up on Google documentation.
 * Also provides a method to map the plain error codes to the enum.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#error_code_reference">Google ReCaptcha Errors</a>
 */
public enum ValidationError {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate");

    /**
     * Get a specific validation error by its error code (as defined in the ReCaptcha  configuration).
     *
     * @param plainError The error for which the {@link ValidationError} should be found for
     * @return The found {@link ValidationError} or null
     */
    public static ValidationError getValidationErrorByCode(String plainError) {
        for (ValidationError error : ValidationError.values()) {
            if (error.getPlainError().equals(plainError)) {
                return error;
            }
        }

        return null;
    }

    /**
     * The error that has been thrown.
     * Equals the error codes listed in Googles documentation.
     */
    private final String plainError;

    /**
     * Constructor
     *
     * @param plainError The original error string that has been thrown
     */
    ValidationError(String plainError) {
        this.plainError = plainError;
    }

    /**
     * Get the plain error as it was set on the received response of the siteverify servers.
     *
     * @return The error as it was received from the siteverify server
     */
    public String getPlainError() {
        return plainError;
    }
}
