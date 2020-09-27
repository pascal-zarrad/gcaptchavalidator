package com.github.playerforcehd.gcaptchavalidator.model.validator;

import com.github.playerforcehd.gcaptchavalidator.model.ReCaptchaVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class ValidatorConfigurationTest {

    /**
     * The dummy token used for testing
     */
    private final String dummySecretToken = "TeSt";

    /**
     * The dummy version used for testing
     */
    private final ReCaptchaVersion dummyVersion = ReCaptchaVersion.VERSION_2;

    /**
     * The dummy verification url for testing
     */
    private final String dummyVerificationUrl = "https://example.org/verify";

    /**
     * The dummy http headers used for testing
     */
    private final Map<String, String> dummyHttpHeaders = new LinkedHashMap<>();

    @Test
    public void testCreateVerifierConfigurationWithSecretToken() {
        ValidatorConfiguration createdConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.dummySecretToken
        );

        Assert.assertTrue(createdConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(createdConfiguration.getSecretToken(), this.dummySecretToken);
        Assert.assertEquals(createdConfiguration.getReCaptchaVersion(), ValidatorConfiguration.DEFAULT_VERSION);
        Assert.assertEquals(createdConfiguration.getVerifierUrl(), ValidatorConfiguration.DEFAULT_VERIFICATION_URL);
        Assert.assertEquals(createdConfiguration.getHttpHeaders(), ValidatorConfiguration.DEFAULT_HTTP_HEADERS);
    }

    @Test
    public void testCreateVerifierConfigurationWithSecretTokenAndVersion() {
        ValidatorConfiguration createdConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.dummySecretToken,
            this.dummyVersion
        );

        Assert.assertTrue(createdConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(createdConfiguration.getSecretToken(), this.dummySecretToken);
        Assert.assertEquals(createdConfiguration.getReCaptchaVersion(), this.dummyVersion);
        Assert.assertEquals(createdConfiguration.getVerifierUrl(), ValidatorConfiguration.DEFAULT_VERIFICATION_URL);
        Assert.assertEquals(createdConfiguration.getHttpHeaders(), ValidatorConfiguration.DEFAULT_HTTP_HEADERS);
    }

    @Test
    public void testCreateVerifierConfigurationWithSecretTokenAndVersionAndVerificationUrl() {
        ValidatorConfiguration createdConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.dummySecretToken,
            this.dummyVersion,
            this.dummyVerificationUrl
        );

        Assert.assertTrue(createdConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(createdConfiguration.getSecretToken(), this.dummySecretToken);
        Assert.assertEquals(createdConfiguration.getReCaptchaVersion(), this.dummyVersion);
        Assert.assertEquals(createdConfiguration.getVerifierUrl(), this.dummyVerificationUrl);
        Assert.assertEquals(createdConfiguration.getHttpHeaders(), ValidatorConfiguration.DEFAULT_HTTP_HEADERS);
    }

    @Test
    public void testCreateVerifierConfigurationWithSecretTokenAndVersionAndVerificationUrlAndHttpHeaders() {
        ValidatorConfiguration createdConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.dummySecretToken,
            this.dummyVersion,
            this.dummyVerificationUrl,
            this.dummyHttpHeaders
        );

        Assert.assertTrue(createdConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(createdConfiguration.getSecretToken(), this.dummySecretToken);
        Assert.assertEquals(createdConfiguration.getReCaptchaVersion(), this.dummyVersion);
        Assert.assertEquals(createdConfiguration.getVerifierUrl(), this.dummyVerificationUrl);
        Assert.assertEquals(createdConfiguration.getHttpHeaders(), this.dummyHttpHeaders);
    }

    @Test
    public void testCreateVerifierConfigurationWithExplicitMutable() {
        ValidatorConfiguration createdConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            "SomeOtherDummy",
            ValidatorConfiguration.DEFAULT_VERSION,
            ValidatorConfiguration.DEFAULT_VERIFICATION_URL,
            ValidatorConfiguration.DEFAULT_HTTP_HEADERS,
            true
        );

        createdConfiguration.setSecretToken(this.dummySecretToken);
        createdConfiguration.setReCaptchaVersion(this.dummyVersion);
        createdConfiguration.setVerifierUrl(this.dummyVerificationUrl);
        createdConfiguration.setHttpHeaders(this.dummyHttpHeaders);

        Assert.assertTrue(createdConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(createdConfiguration.getSecretToken(), this.dummySecretToken);
        Assert.assertEquals(createdConfiguration.getReCaptchaVersion(), this.dummyVersion);
        Assert.assertEquals(createdConfiguration.getVerifierUrl(), this.dummyVerificationUrl);
        Assert.assertEquals(createdConfiguration.getHttpHeaders(), this.dummyHttpHeaders);
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testCreateVerifierConfigurationWithExplicitImmutable() throws UnsupportedOperationException {
        ValidatorConfiguration createdConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.dummySecretToken,
            this.dummyVersion,
            this.dummyVerificationUrl,
            this.dummyHttpHeaders,
            false
        );

        createdConfiguration.setSecretToken("SomeOtherDummy");
        createdConfiguration.setReCaptchaVersion(ValidatorConfiguration.DEFAULT_VERSION);
        createdConfiguration.setVerifierUrl(ValidatorConfiguration.DEFAULT_VERIFICATION_URL);
        createdConfiguration.setHttpHeaders(ValidatorConfiguration.DEFAULT_HTTP_HEADERS);

        Assert.assertTrue(createdConfiguration instanceof ImmutableValidatorConfiguration);
        Assert.assertEquals(createdConfiguration.getSecretToken(), this.dummySecretToken);
        Assert.assertEquals(createdConfiguration.getReCaptchaVersion(), this.dummyVersion);
        Assert.assertEquals(createdConfiguration.getVerifierUrl(), this.dummyVerificationUrl);
        Assert.assertEquals(createdConfiguration.getHttpHeaders(), this.dummyHttpHeaders);

        // Mutation of immutable map should throw UnsupportedOperationException
        createdConfiguration.getHttpHeaders().put("Some", "Test");
    }
}
