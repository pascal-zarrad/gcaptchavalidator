package com.github.playerforcehd.gcaptchavalidator;

/**
 * Defines the interface between the library and a developer that utilizes it.
 * A normal developer that only wants to validate ReCaptcha responses should be satisfied by using
 * the base implementation of this interface, {@link CaptchaValidatorConfiguration} and
 * {@link CaptchaValidationResponse}.
 *
 * A {@link CaptchaValidator} combines a
 * {@link com.github.playerforcehd.gcaptchavalidator.serialize.CaptchaResponseDeserializer} and
 * the {@link com.github.playerforcehd.gcaptchavalidator.request.CaptchaRequestHandler} to result in an easy
 * validation of ReCaptcha responses.
 *
 * Note that the default implementation of the {@link CaptchaValidator} deals only with immutable configuraiton.
 * Changing something will require you to create a new instance.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public interface CaptchaValidator {
    /**
     * Create a default {@link CaptchaValidator} that uses a {@link GCaptchaValidator} internally
     * with it's default configuration.
     *
     * @param secret The secret to use for validation.
     * @return The created {@link CaptchaValidator}
     */
    static CaptchaValidator createDefault(String secret) {
        return new GCaptchaValidator(secret);
    }

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a boolean.
     *
     * @param response The response to validate
     * @return The result of the external validation as a simple boolean
     */
    boolean basicValidate(String response);

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a boolean.
     *
     * @param response The response to validate
     * @param remoteIP The remote IP of the one who issued the request
     * @return The result of the external validation as a simple boolean
     */
    boolean basicValidate(String response, String remoteIP);

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a {@link CaptchaValidationResponse}
     * that allows a rich processing of the result afterwards.
     * The {@link CaptchaValidationResponse} will contain the entire result of the validation
     * in a more structured manner for developers.
     *
     * @param response The response to validate
     * @return The result of the external validation as a {@link CaptchaValidationResponse}
     */
    CaptchaValidationResponse validate(String response);

    /**
     * Validate a Google ReCaptcha response.
     * The result if the response is valid is being returned as a {@link CaptchaValidationResponse}
     * that allows a rich processing of the result afterwards.
     * The {@link CaptchaValidationResponse} will contain the entire result of the validation
     * in a more structured manner for developers.
     *
     * @param response The response to validate
     * @param remoteIP The remote IP of the one who issued the request
     * @return The result of the external validation as a {@link CaptchaValidationResponse}
     */
    CaptchaValidationResponse validate(String response, String remoteIP);

    /**
     * Get the {@link CaptchaValidatorConfiguration} that is currently used by this {@link CaptchaValidator}
     *
     * @return The {@link CaptchaValidatorConfiguration} used by this {@link CaptchaValidator}
     */
    CaptchaValidatorConfiguration getConfiguration();
}
