package com.github.playerforcehd.gcaptchavalidator.model.validator;

import com.github.playerforcehd.gcaptchavalidator.model.ReCaptchaVersion;

import java.io.Serializable;
import java.util.*;

/**
 * Represents the basic configuration required to send request to the a site verification server.
 *
 * The necessary configuration consists of the following parameters:
 *  - The secret token
 *  - The ReCaptcha version that is dealt with
 *  - The site verification servers URL
 *
 * The default site verification URL points to Googles 'siteverify' URL as defined in the
 * ReCaptcha documentation.
 * The default ReCaptcha version that the library is expecting is version 3.
 *
 * This class consists of fluent setters to allow it to be used like a builder.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see <a href="https://developers.google.com/recaptcha/docs/verify#api_request">Google ReCaptcha Documentation</a>
 */
public interface ValidatorConfiguration extends Serializable {
    /**
     * The default version used when no ReCaptcha version is specified manually
     */
    ReCaptchaVersion DEFAULT_VERSION = ReCaptchaVersion.VERSION_3;

    /**
     * The default verification URL used when no site verification URL is specified manually
     */
    String DEFAULT_VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify";

    /**
     * The default HTTP headers to send with requests created using this configuration
     */
    Map<String, String> DEFAULT_HTTP_HEADERS = new HashMap<>();

    /**
     * Factory method to create a new mutable verifier configuration
     *
     * @param secretToken The secret token to configure
     * @return The created verifier configuration instance
     */
    static ValidatorConfiguration createVerifierConfiguration(String secretToken) {
        return new MutableValidatorConfiguration(
            secretToken,
            DEFAULT_VERSION,
            DEFAULT_VERIFICATION_URL,
            new HashMap<>(DEFAULT_HTTP_HEADERS)
        );
    }

    /**
     * Factory method to create a new mutable verifier configuration
     *
     * @param secretToken The secret token to configure
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @return The created verifier configuration instance
     */
    static ValidatorConfiguration createVerifierConfiguration(String secretToken, ReCaptchaVersion reCaptchaVersion) {
        return new MutableValidatorConfiguration(
            secretToken,
            reCaptchaVersion,
            DEFAULT_VERIFICATION_URL,
            new HashMap<>(DEFAULT_HTTP_HEADERS)
        );
    }

    /**
     * Factory method to create a new mutable verifier configuration
     *
     * @param secretToken The secret token to configure
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @param verifierUrl The URL where requests should be send to
     * @return The created verifier configuration instance
     */
    static ValidatorConfiguration createVerifierConfiguration(
        String secretToken,
        ReCaptchaVersion reCaptchaVersion,
        String verifierUrl
    ) {
        return new MutableValidatorConfiguration(
            secretToken,
            reCaptchaVersion,
            verifierUrl,
            new HashMap<>(DEFAULT_HTTP_HEADERS)
        );
    }

    /**
     * Factory method to create a new mutable verifier configuration
     *
     * @param secretToken The secret token to configure
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @param verifierUrl The URL where requests should be send to
     * @param httpHeaders The HTTP headers to sent when using thi configuration
     * @return The created verifier configuration instance
     */
    static ValidatorConfiguration createVerifierConfiguration(
        String secretToken,
        ReCaptchaVersion reCaptchaVersion,
        String verifierUrl,
        Map<String, String> httpHeaders
    ) {
        return new MutableValidatorConfiguration(
            secretToken,
            reCaptchaVersion,
            verifierUrl,
            httpHeaders
        );
    }

    /**
     * Factory method to create a new mutable verifier configuration
     *
     * @param secretToken The secret token to configure
     * @param reCaptchaVersion The targeted ReCaptcha version
     * @param verifierUrl The URL where requests should be send to
     * @param httpHeaders The HTTP headers to sent when using thi configuration
     * @return The created verifier configuration instance
     */
    static ValidatorConfiguration createVerifierConfiguration(
        String secretToken,
        ReCaptchaVersion reCaptchaVersion,
        String verifierUrl,
        Map<String, String> httpHeaders,
        boolean mutable
    ) {
        if (mutable) {
            return new MutableValidatorConfiguration(secretToken, reCaptchaVersion, verifierUrl, httpHeaders);
        }

        return new ImmutableValidatorConfiguration(secretToken, reCaptchaVersion, verifierUrl, httpHeaders);
    }

    /**
     * Get the configured secret token
     *
     * @return The configured secret token
     */
    String getSecretToken();

    /**
     * Set the secret token
     *
     * @param secretToken Th secret token to set
     * @return This VerifierConfiguration instance
     */
    ValidatorConfiguration setSecretToken(String secretToken);

    /**
     * Get the ReCaptcha version that is used
     *
     * @return The used ReCaptcha version
     */
    ReCaptchaVersion getReCaptchaVersion();

    /**
     * Set the ReCaptcha version that is used
     *
     * @param reCaptchaVersion The used ReCaptcha version
     * @return This VerifierConfiguration instance
     */
    ValidatorConfiguration setReCaptchaVersion(ReCaptchaVersion reCaptchaVersion);

    /**
     * Get the verification URL used to verify responses
     *
     * @return The verification URL used for verification
     */
    String getVerifierUrl();

    /**
     * Set the verification URL used to verify responses
     *
     * @param verifierUrl The new verification URL used for verification
     * @return This VerifierConfiguration instance
     */
    ValidatorConfiguration setVerifierUrl(String verifierUrl);

    /**
     * Get the custom HTTP headers to send using this configuration.
     *
     * @return The HTTP headers to send when using this configuration
     */
    Map<String, String> getHttpHeaders();

    /**
     * Set the HTTP headers to send when using this configuration
     *
     * Note that this will override the default headers that are sent by GCaptchaValidator.
     *
     * @param httpHeaders The HTTP headers to send when using this configuration
     * @return This VerifierConfiguration instance
     */
    ValidatorConfiguration setHttpHeaders(Map<String, String> httpHeaders);
}
