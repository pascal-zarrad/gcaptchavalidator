package com.github.playerforcehd.gcaptchavalidator.model.validator;

import com.github.playerforcehd.gcaptchavalidator.model.ReCaptchaVersion;

import java.util.Map;

/**
 * Mutable verifier configuration.
 *
 * Instances of this class allow the configuration to be changed after instantiation.
 *
 * {@inheritDoc }
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class MutableValidatorConfiguration implements ValidatorConfiguration {

    /**
     * The ReCaptcha server side secret required for validation.
     */
    private String secretToken;

    /**
     * The targeted ReCaptcha version which is used to decide between different
     * implementations. This allows support for all current available ReCaptcha versions
     * without sacrificing features like the score in version 3.
     */
    private ReCaptchaVersion reCaptchaVersion;

    /**
     * The URL where the verification request is send to
     */
    private String verifierUrl;

    /**
     * The HTTP headers to append/overwrite on requests using this configuration
     */
    private Map<String, String> httpHeaders;

    /**
     * Constructor
     *
     * @param secretToken The secret ReCaptcha site verification token to use to identify on Google's side
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @param verifierUrl The URL where the requests will be send to
     * @param httpHeaders The HTTP headers to send with requests using this configuration
     */
    MutableValidatorConfiguration(
        String secretToken,
        ReCaptchaVersion reCaptchaVersion,
        String verifierUrl,
        Map<String, String> httpHeaders
    ) {
        this.secretToken = secretToken;
        this.reCaptchaVersion = reCaptchaVersion;
        this.verifierUrl = verifierUrl;
        this.httpHeaders = httpHeaders;
    }

    @Override
    public String getSecretToken() {
        return secretToken;
    }

    @Override
    public MutableValidatorConfiguration setSecretToken(String secretToken) {
        this.secretToken = secretToken;

        return  this;
    }

    @Override
    public ReCaptchaVersion getReCaptchaVersion() {
        return reCaptchaVersion;
    }

    @Override
    public MutableValidatorConfiguration setReCaptchaVersion(ReCaptchaVersion reCaptchaVersion) {
        this.reCaptchaVersion = reCaptchaVersion;

        return this;
    }

    @Override
    public String getVerifierUrl() {
        return verifierUrl;
    }

    @Override
    public MutableValidatorConfiguration setVerifierUrl(String verifierUrl) {
        this.verifierUrl = verifierUrl;

        return this;
    }

    @Override
    public Map<String, String> getHttpHeaders() {
        return this.httpHeaders;
    }

    @Override
    public ValidatorConfiguration setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;

        return this;
    }
}
