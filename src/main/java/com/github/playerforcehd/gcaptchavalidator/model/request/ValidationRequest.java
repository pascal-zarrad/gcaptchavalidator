package com.github.playerforcehd.gcaptchavalidator.model.request;

import com.github.playerforcehd.gcaptchavalidator.model.validator.ValidatorConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * A single request that is send to the site verification server to validate a client response.
 *
 * Consists of a response and a remote IP.
 * The remote IP is optional, as documented in Googles official documentation.
 *
 * If response or remote IP is empty, the variable will be omitted from the request
 * send to the server.
 *
 * The additional parameters have a higher priority than the predefined variables
 * (including {@link ValidatorConfiguration#getSecretToken()}) and can
 * override them.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#api_request">Google ReCaptcha Documentation</a>
 */
public interface ValidationRequest {

    /**
     * Create a new basic validation request with the supplied parameters.
     *
     * @param response The response of the client to set on the request
     * @return The created basic validation request that has been created
     */
    static ValidationRequest createValidationRequest(String response) {
        return new BasicValidationRequest(response, "", new HashMap<>());
    }

    /**
     * Create a new basic validation request with the supplied parameters.
     *
     * @param response The response of the client to set on the request
     * @param remoteIP The remote IP of the client that send the response
     * @return The created basic validation request that has been created
     */
    static ValidationRequest createValidationRequest(String response, String remoteIP) {
        return new BasicValidationRequest(response, remoteIP, new HashMap<>());
    }

    /**
     * Create a new basic validation request with the supplied parameters.
     *
     * @param response The response of the client to set on the request
     * @param remoteIP The remote IP of the client that send the response
     * @param additionalParameters The additional parameters to set on the request
     * @return The created basic validation request that has been created
     */
    static ValidationRequest createValidationRequest(
        String response,
        String remoteIP,
        Map<String, Object> additionalParameters
    ) {
        return new BasicValidationRequest(response, remoteIP, additionalParameters);
    }

    /**
     * Get the response that is set on the request
     *
     * @return The set response
     */
    String getResponse();

    /**
     * Set the response on this verification request
     *
     * @param response The response to set
     * @return This verification request
     */
    ValidationRequest setResponse(String response);

    /**
     * Get the remote IP of the response
     *
     * @return The IP that send the response
     */
    String getRemoteIP();

    /**
     * The remote IP to set on the verification request
     *
     * @param remoteIP The remote IP to set
     * @return This verification request
     */
    ValidationRequest setRemoteIP(String remoteIP);

    /**
     * Additional parameters that are applied to the send request.
     * Additional parameters override existing ones.
     *
     * @return The additional parameters that are set
     */
    Map<String, Object> getAdditionalParameters();

    /**
     * Set the additional parameters to put into the request sent to the site verification servers.
     * Note that setting any parameters may override default parameters.
     *
     * @param additionalParameters The additional parameters to set
     * @return This verification request
     */
    ValidationRequest setAdditionalParameters(Map<String, Object> additionalParameters);
}
