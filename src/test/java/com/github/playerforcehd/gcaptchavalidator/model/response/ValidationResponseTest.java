package com.github.playerforcehd.gcaptchavalidator.model.response;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Test if the factory methods of ValidationResponse work as intended
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class ValidationResponseTest {
    /**
     * The expected succeeded state
     */
    private final boolean expectedSucceeded = true;

    /**
     * The date that is expected
     */
    private final Date expectedChallengeTimestamp = new Date();

    /**
     * The expected client type
     */
    private final ClientType expectedClientType = ClientType.WEB;

    /**
     * The expected hostname or android package name
     */
    private final String expectedHostnameOrPackageName = "127.0.0.1";

    /**
     * The expected validation errors
     */
    private final ValidationError[] expectedErrors = {
        ValidationError.MISSING_INPUT_RESPONSE,
        ValidationError.INVALID_INPUT_SECRET
    };

    @Test
    public void testCreateValidationResponseWitAdditionalData() {
        Map<String, Object> expectedMap = Maps.newHashMap();
        expectedMap.put("Test", "It");

        ValidationResponse actual = ValidationResponse.createValidationResponse(
            this.expectedSucceeded,
            this.expectedChallengeTimestamp,
            this.expectedClientType,
            this.expectedHostnameOrPackageName,
            this.expectedErrors,
            expectedMap
        );

        Assert.assertTrue(actual instanceof ImmutableResponse);
        Assert.assertEquals(actual.hasSucceeded(), this.expectedSucceeded);
        Assert.assertEquals(actual.getChallengeTimestamp(), this.expectedChallengeTimestamp);
        Assert.assertEquals(actual.getClientType(), this.expectedClientType);
        Assert.assertEquals(actual.getHostnameOrPackageName(), this.expectedHostnameOrPackageName);
        Assert.assertEquals(actual.getErrors(), this.expectedErrors);
        Assert.assertEquals(actual.getAdditionalData(), expectedMap);
    }

    @Test
    public void testCreateValidationResponseWithoutAdditionalData() {
        ValidationResponse actual = ValidationResponse.createValidationResponse(
            this.expectedSucceeded,
            this.expectedChallengeTimestamp,
            this.expectedClientType,
            this.expectedHostnameOrPackageName,
            this.expectedErrors
        );

        Assert.assertTrue(actual instanceof ImmutableResponse);
        Assert.assertEquals(actual.hasSucceeded(), this.expectedSucceeded);
        Assert.assertEquals(actual.getChallengeTimestamp(), this.expectedChallengeTimestamp);
        Assert.assertEquals(actual.getClientType(), this.expectedClientType);
        Assert.assertEquals(actual.getHostnameOrPackageName(), this.expectedHostnameOrPackageName);
        Assert.assertEquals(actual.getErrors(), this.expectedErrors);
        Assert.assertTrue(actual.getAdditionalData() instanceof HashMap);
        Assert.assertEquals(actual.getAdditionalData().size(), 0);
    }
}
