package com.github.playerforcehd.gcaptchavalidator.request;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidatorConfiguration;

import java.io.UnsupportedEncodingException;

/**
 * An interface that abstracts the starting point of the interaction
 * between this library and the targeted SiteVerify servers.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public interface CaptchaRequestHandler {
    /**
     * Execute a request to validate a response from a client.
     * Note that the supplied Strings should be UTF-8,
     *
     * @param captchaValidatorConfiguration The configuration of the validator
     * @param response The response to validate
     * @param remoteIP Optional. The remoteIP of the user to validate. Can be empty.
     * @return The response from the SiteVerify servers as a String (most likely JSON if everything went well)
     * @throws CaptchaRequestHandlerException Thrown when there was an error when executing the request
     */
    String request(
        CaptchaValidatorConfiguration captchaValidatorConfiguration,
        String response,
        String remoteIP
    ) throws CaptchaRequestHandlerException;
}
