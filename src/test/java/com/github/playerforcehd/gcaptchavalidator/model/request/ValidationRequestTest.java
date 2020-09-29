package com.github.playerforcehd.gcaptchavalidator.model.request;

import org.junit.Before;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test validation request factory methods
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 * @see ValidationRequest
 */
public class ValidationRequestTest {

    /**
     * The dummy response to use during tests
     */
    private final String expectedResponse = "12345xyz";

    /**
     * The dummy remote IP to use during tests
     */
    private final String expectedRemoteIP = "127.0.0.1";

    /**
     * The dummy additional parameters to use during tests
     */
    private Map<String, Object> expectedAdditionalParameters;

    @Before
    public void setUp() {
        this.expectedAdditionalParameters = new HashMap<>();
        this.expectedAdditionalParameters.put("version", "2");
    }

    @Test
    public void testCreateWithResponse() {
        ValidationRequest actualValidationRequest = ValidationRequest.createValidationRequest(this.expectedResponse);

        Assert.assertTrue(actualValidationRequest instanceof BasicValidationRequest);
        Assert.assertEquals(actualValidationRequest.getResponse(), this.expectedResponse);
        Assert.assertTrue(actualValidationRequest.getRemoteIP().isEmpty());
        Assert.assertEquals(actualValidationRequest.getAdditionalParameters().size(), 0);
    }

    @Test
    public void testCreateWithResponseAndRemoteIp() {
        ValidationRequest actualValidationRequest = ValidationRequest.createValidationRequest(
            this.expectedResponse,
            this.expectedRemoteIP
        );

        Assert.assertTrue(actualValidationRequest instanceof BasicValidationRequest);
        Assert.assertEquals(actualValidationRequest.getResponse(), this.expectedResponse);
        Assert.assertEquals(actualValidationRequest.getRemoteIP(), this.expectedRemoteIP);
        Assert.assertEquals(actualValidationRequest.getAdditionalParameters().size(), 0);
    }

    @Test
    public void testCreateWithResponseAndRemoteIpAndAdditionalParameters() {
        ValidationRequest actualValidationRequest = ValidationRequest.createValidationRequest(
            this.expectedResponse,
            this.expectedRemoteIP,
            this.expectedAdditionalParameters
        );

        Assert.assertTrue(actualValidationRequest instanceof BasicValidationRequest);
        Assert.assertEquals(actualValidationRequest.getResponse(), this.expectedResponse);
        Assert.assertEquals(actualValidationRequest.getRemoteIP(), this.expectedRemoteIP);
        Assert.assertEquals(actualValidationRequest.getAdditionalParameters(), this.expectedAdditionalParameters);
    }
}
