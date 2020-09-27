package com.github.playerforcehd.gcaptchavalidator.model.validator;

import com.github.playerforcehd.gcaptchavalidator.model.ReCaptchaVersion;

import java.util.Collections;
import java.util.Map;

/**
 * Immutable verifier configuration.
 *
 * Instances of this class fo not allow the configuration to be changed after instantiation.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class ImmutableValidatorConfiguration implements ValidatorConfiguration {


    /**
     * The ReCaptcha server side secret required for validation.
     */
    private final String secretToken;

    /**
     * The targeted ReCaptcha version which is used to decide between different
     * implementations. This allows support for all current available ReCaptcha versions
     * without sacrificing features like the score in version 3.
     */
    private final ReCaptchaVersion reCaptchaVersion;

    /**
     * The URL where the verification request is send to
     */
    private final String verifierUrl;

    /**
     * The HTTP headers to append/overwrite on requests using this configuration
     */
    private final Map<String, String> httpHeaders;

    /**
     * Constructor
     *
     * @param secretToken The secret ReCaptcha site verification token to use to identify on Google's side
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @param verifierUrl The URL where the requests will be send to
     * @param httpHeaders The HTTP headers to send with requests using this configuration
     */
    ImmutableValidatorConfiguration(
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
    public ImmutableValidatorConfiguration setSecretToken(String secretToken) {
        return this;
    }

    @Override
    public ReCaptchaVersion getReCaptchaVersion() {
        return reCaptchaVersion;
    }

    @Override
    public ImmutableValidatorConfiguration setReCaptchaVersion(ReCaptchaVersion reCaptchaVersion) {
        return this;
    }

    @Override
    public String getVerifierUrl() {
        return verifierUrl;
    }

    @Override
    public ImmutableValidatorConfiguration setVerifierUrl(String verifierUrl) {
        return this;
    }

    @Override
    public Map<String, String> getHttpHeaders() {
        return Collections.unmodifiableMap(this.httpHeaders);
    }

    @Override
    public ValidatorConfiguration setHttpHeaders(Map<String, String> httpHeaders) {
        return this;
    }

}
