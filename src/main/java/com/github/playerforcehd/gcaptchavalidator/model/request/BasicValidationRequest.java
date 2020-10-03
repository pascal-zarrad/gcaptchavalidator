package com.github.playerforcehd.gcaptchavalidator.model.request;

import java.util.Map;

/**
 * Basic validation request
 *
 * A basic request model that allows to get and set the data necessary on every request.
 * One {@link BasicValidationRequest} equals exactly one request from the client to the siteverify servers.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see com.github.playerforcehd.gcaptchavalidator.model.request.ValidationRequest
 */
public class BasicValidationRequest implements ValidationRequest {
    /**
     * The response from the client
     */
    private String response;

    /**
     * The IP of the client that requested the ReCaptcha verification
     */
    private String remoteIP;

    /**
     * The additional parameters appended to the request
     */
    private Map<String, Object> additionalParameters;

    /**
     * Constructor
     *
     * @param response The response of the client
     * @param remoteIP The IP of the client
     * @param additionalParameters Additional parameters that are merged into the generated request
     */
    public BasicValidationRequest(String response, String remoteIP, Map<String, Object> additionalParameters) {
        this.response = response;
        this.remoteIP = remoteIP;
        this.additionalParameters = additionalParameters;
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public ValidationRequest setResponse(String response) {
        this.response = response;

        return this;
    }

    @Override
    public String getRemoteIP() {
        return this.remoteIP;
    }

    @Override
    public ValidationRequest setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;

        return this;
    }

    @Override
    public Map<String, Object> getAdditionalParameters() {
        return this.additionalParameters;
    }

    @Override
    public ValidationRequest setAdditionalParameters(Map<String, Object> additionalParameters) {
        this.additionalParameters = additionalParameters;

        return this;
    }
}
