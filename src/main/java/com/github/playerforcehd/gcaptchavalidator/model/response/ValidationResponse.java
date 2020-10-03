package com.github.playerforcehd.gcaptchavalidator.model.response;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single response of a request that was send to the siteverify servers.
 *
 * The response contains all currently defined fields as stated in Googles documentation.
 * Although, the mapping of the fields is a bit different to make it easier to work with the
 * received response on developers side.
 *
 * Mapping used by ReCaptcha Validator:
 *  - success          - succeeded
 *  - challenge_ts     - challengeTimestamp
 *  - hostname         - hostnameOrPackageName in conjunction with clientType
 *  - apk_package_name - hostnameOrPackageName in conjunction with clientType
 *  - errors           - errors
 *
 * The mapped fields are accessed through their getters:
 *   - succeeded             - {@link #hasSucceeded()}
 *   - challenge_ts          - {@link #getChallengeTimestamp()}
 *   - clientType            - {@link #getClientType()}
 *   - hostnameOrPackageName - {@link #getHostnameOrPackageName()}
 *   - errors                - {@link #getErrors()}
 *
 *  The mapping for hostname and apk_package_name works as the following:
 *   - If the hostname key is present, {@link #getClientType()} will contain {@link ClientType#WEB }
 *     and {@link #getHostnameOrPackageName()} the hostname value of the response.
 *   - If the apk_package_name key is present, {@link #getClientType()} will contain {@link ClientType#ANDROID }
 *     and {@link #getHostnameOrPackageName()} the android package name value of the response.
 *   - If both values are available, the response won't happen as the request will have been failed
 *     with an exception as the mapping cannot be applied.
 *
 * Also if some additional information is being defined later on, it will end up in the additional data.
 *
 * An instantiated {@link ValidationResponse} is immutable.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#api_request">Google ReCaptcha Documentation</a>
 */
public interface ValidationResponse extends Serializable {
    /**
     * Create a new immutable validation response
     *
     * @param succeeded The state if the validation returned a positive result
     * @param challengeTimestamp The time when the challenge has been loaded
     * @param clientType The type of the client that requested the validation
     * @param hostnameOrPackageName The hostname/ip or package name of the client
     * @param errors The errors returned by a failed validation
     * @return The created {@link ValidationResponse}
     */
    static ValidationResponse createValidationResponse(
        boolean succeeded,
        Date challengeTimestamp,
        ClientType clientType,
        String hostnameOrPackageName,
        ValidationError[] errors
    ) {
        return new ImmutableResponse(
            succeeded,
            challengeTimestamp,
            clientType,
            hostnameOrPackageName,
            errors,
            new HashMap<>()
        );
    }

    /**
     * Create a new immutable validation response
     *
     * @param succeeded The state if the validation returned a positive result
     * @param challengeTimestamp The time when the challenge has been loaded
     * @param clientType The type of the client that requested the validation
     * @param hostnameOrPackageName The hostname/ip or package name of the client
     * @param validationErrors The errors returned by a failed validation
     * @param additionalData The data that couldn't be mapped to the existing fields
     * @return The created {@link ValidationResponse}
     */
    static ValidationResponse createValidationResponse(
        boolean succeeded,
        Date challengeTimestamp,
        ClientType clientType,
        String hostnameOrPackageName,
        ValidationError[] validationErrors,
        Map<String, Object> additionalData
    ) {
        return new ImmutableResponse(
            succeeded,
            challengeTimestamp,
            clientType,
            hostnameOrPackageName,
            validationErrors,
            additionalData
        );
    }

    /**
     * Check if the validation was successful
     *
     * @return The status if the request was successful
     */
    boolean hasSucceeded();

    /**
     * Get the timestamp when the challenge has been loaded.
     *
     * @return The timestamp when the challenge has been loaded
     */
    Date getChallengeTimestamp();

    /**
     * Get the type of client that requested the validation.
     * The type can be used to determine if the hostnameOrPackage field contains
     * either an hostname or Android package name.
     *
     * @return The type of client that send the request
     */
    ClientType getClientType();

    /**
     * Get the hostname or apk_package_name of the response.
     * Use {@link #getClientType()} to determine if this method will return a
     * hostname or apk_package_name.
     *
     * @return The hostname or apk_package_name of the response
     */
    String getHostnameOrPackageName();

    /**
     * Get the errors that have been set on the response
     *
     * @return The errors that are set on the response
     */
    ValidationError[] getErrors();

    /**
     * Get the data that has been send additionally - if there is some.
     * All values that cannot be mapped to the available predefined values
     * of this standardized response type will be put into the additional data map.
     *
     * The values can either be primitive or another Map&gt;String, Object&lt;.
     *
     * @return A map containing data that does not belong to standardized response keys
     */
    Map<String, Object> getAdditionalData();
}
