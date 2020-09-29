package com.github.playerforcehd.gcaptchavalidator.model.validator;

import com.github.playerforcehd.gcaptchavalidator.model.ReCaptchaVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see ValidatorConfiguration
 */
public class ValidatorConfigurationTest {

    /**
     * The dummy token used for testing
     */
    private final String expectedSecretToken = "TeSt";

    /**
     * The dummy version used for testing
     */
    private final ReCaptchaVersion expectedReCaptchaVersion = ReCaptchaVersion.VERSION_2;

    /**
     * The dummy verification url for testing
     */
    private final String expectedVerificationUrl = "https://example.org/verify";

    /**
     * The dummy http headers used for testing
     */
    private final Map<String, String> expectedHttpHeaders = new LinkedHashMap<>();

    @Test
    public void testCreateVerifierConfigurationWithSecretToken() {
        ValidatorConfiguration actualConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.expectedSecretToken
        );

        Assert.assertTrue(actualConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(actualConfiguration.getSecretToken(), this.expectedSecretToken);
        Assert.assertEquals(actualConfiguration.getReCaptchaVersion(), ValidatorConfiguration.DEFAULT_VERSION);
        Assert.assertEquals(actualConfiguration.getVerifierUrl(), ValidatorConfiguration.DEFAULT_VERIFICATION_URL);
        Assert.assertEquals(actualConfiguration.getHttpHeaders(), ValidatorConfiguration.DEFAULT_HTTP_HEADERS);
    }

    @Test
    public void testCreateVerifierConfigurationWithSecretTokenAndVersion() {
        ValidatorConfiguration actualConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.expectedSecretToken,
            this.expectedReCaptchaVersion
        );

        Assert.assertTrue(actualConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(actualConfiguration.getSecretToken(), this.expectedSecretToken);
        Assert.assertEquals(actualConfiguration.getReCaptchaVersion(), this.expectedReCaptchaVersion);
        Assert.assertEquals(actualConfiguration.getVerifierUrl(), ValidatorConfiguration.DEFAULT_VERIFICATION_URL);
        Assert.assertEquals(actualConfiguration.getHttpHeaders(), ValidatorConfiguration.DEFAULT_HTTP_HEADERS);
    }

    @Test
    public void testCreateVerifierConfigurationWithSecretTokenAndVersionAndVerificationUrl() {
        ValidatorConfiguration actualConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.expectedSecretToken,
            this.expectedReCaptchaVersion,
            this.expectedVerificationUrl
        );

        Assert.assertTrue(actualConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(actualConfiguration.getSecretToken(), this.expectedSecretToken);
        Assert.assertEquals(actualConfiguration.getReCaptchaVersion(), this.expectedReCaptchaVersion);
        Assert.assertEquals(actualConfiguration.getVerifierUrl(), this.expectedVerificationUrl);
        Assert.assertEquals(actualConfiguration.getHttpHeaders(), ValidatorConfiguration.DEFAULT_HTTP_HEADERS);
    }

    @Test
    public void testCreateVerifierConfigurationWithSecretTokenAndVersionAndVerificationUrlAndHttpHeaders() {
        ValidatorConfiguration actualConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.expectedSecretToken,
            this.expectedReCaptchaVersion,
            this.expectedVerificationUrl,
            this.expectedHttpHeaders
        );

        Assert.assertTrue(actualConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(actualConfiguration.getSecretToken(), this.expectedSecretToken);
        Assert.assertEquals(actualConfiguration.getReCaptchaVersion(), this.expectedReCaptchaVersion);
        Assert.assertEquals(actualConfiguration.getVerifierUrl(), this.expectedVerificationUrl);
        Assert.assertEquals(actualConfiguration.getHttpHeaders(), this.expectedHttpHeaders);
    }

    @Test
    public void testCreateVerifierConfigurationWithExplicitMutable() {
        ValidatorConfiguration actualConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            "SomeOtherDummy",
            ValidatorConfiguration.DEFAULT_VERSION,
            ValidatorConfiguration.DEFAULT_VERIFICATION_URL,
            ValidatorConfiguration.DEFAULT_HTTP_HEADERS,
            true
        );

        actualConfiguration.setSecretToken(this.expectedSecretToken);
        actualConfiguration.setReCaptchaVersion(this.expectedReCaptchaVersion);
        actualConfiguration.setVerifierUrl(this.expectedVerificationUrl);
        actualConfiguration.setHttpHeaders(this.expectedHttpHeaders);

        Assert.assertTrue(actualConfiguration instanceof MutableValidatorConfiguration);
        Assert.assertEquals(actualConfiguration.getSecretToken(), this.expectedSecretToken);
        Assert.assertEquals(actualConfiguration.getReCaptchaVersion(), this.expectedReCaptchaVersion);
        Assert.assertEquals(actualConfiguration.getVerifierUrl(), this.expectedVerificationUrl);
        Assert.assertEquals(actualConfiguration.getHttpHeaders(), this.expectedHttpHeaders);
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testCreateVerifierConfigurationWithExplicitImmutable() throws UnsupportedOperationException {
        ValidatorConfiguration actualConfiguration = ValidatorConfiguration.createVerifierConfiguration(
            this.expectedSecretToken,
            this.expectedReCaptchaVersion,
            this.expectedVerificationUrl,
            this.expectedHttpHeaders,
            false
        );

        actualConfiguration.setSecretToken("SomeOtherDummy");
        actualConfiguration.setReCaptchaVersion(ValidatorConfiguration.DEFAULT_VERSION);
        actualConfiguration.setVerifierUrl(ValidatorConfiguration.DEFAULT_VERIFICATION_URL);
        actualConfiguration.setHttpHeaders(ValidatorConfiguration.DEFAULT_HTTP_HEADERS);

        Assert.assertTrue(actualConfiguration instanceof ImmutableValidatorConfiguration);
        Assert.assertEquals(actualConfiguration.getSecretToken(), this.expectedSecretToken);
        Assert.assertEquals(actualConfiguration.getReCaptchaVersion(), this.expectedReCaptchaVersion);
        Assert.assertEquals(actualConfiguration.getVerifierUrl(), this.expectedVerificationUrl);
        Assert.assertEquals(actualConfiguration.getHttpHeaders(), this.expectedHttpHeaders);

        // Mutation of immutable map should throw UnsupportedOperationException
        actualConfiguration.getHttpHeaders().put("Some", "Test");
    }
}
