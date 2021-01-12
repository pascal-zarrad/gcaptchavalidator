/*
 * The MIT License
 *
 * Copyright (c) 2021 Pascal Zarrad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.playerforcehd.gcaptchavalidator;

import com.github.playerforcehd.gcaptchavalidator.data.ClientType;
import com.github.playerforcehd.gcaptchavalidator.data.ReCaptchaVersion;
import com.github.playerforcehd.gcaptchavalidator.data.ValidationError;
import com.github.playerforcehd.gcaptchavalidator.request.CaptchaRequestHandler;
import com.github.playerforcehd.gcaptchavalidator.request.CaptchaRequestHandlerException;
import com.github.playerforcehd.gcaptchavalidator.serialize.CaptchaResponseDeserializer;
import org.testng.annotations.Test;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit and integration tests for the default validator.
 * <p>
 * The integration test will check if the requests are processed properly
 * with truthy responses using the default test credentials provided by Google.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class GCaptchaValidatorTest {
    /**
     * The secret used to run integration tests for the entire library.
     * All requests to this secret should result in a truthy response (even with invalid responses).
     * This is the official test secret from: https://developers.google.com/recaptcha/docs/faq
     */
    private static final String TEST_SECRET = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";

    // --- Unit tests

    @Test
    public void testBasicValidateWithoutIP() {
        CaptchaRequestHandler captchaRequestHandler = mock(CaptchaRequestHandler.class);
        CaptchaResponseDeserializer captchaResponseDeserializer = mock(CaptchaResponseDeserializer.class);
        CaptchaValidatorConfiguration captchaValidatorConfiguration = new ValidatorConfiguration("Test");
        String testResponse = "SomeResponse";
        String requestHandlerResponse = "{\"success\": true}";
        CaptchaValidationResponse captchaValidationResponse = new ValidationResponse(
            ReCaptchaVersion.VERSION_2,
            true,
            mock(Date.class),
            ClientType.WEB,
            "localhost",
            0.5f,
            "home",
            new ValidationError[0]
        );

        try {
            when(
                captchaRequestHandler.request(
                    any(ImmutableValidatorConfiguration.class),
                    eq(testResponse),
                    eq("")
                )
            ).thenReturn(requestHandlerResponse);
            when(
                captchaResponseDeserializer.deserialize(requestHandlerResponse)
            ).thenReturn(captchaValidationResponse);
        } catch (CaptchaRequestHandlerException e) {
            fail("Unexpected exception occurred while preparing tests", e);
        }

        CaptchaValidator captchaValidator = new GCaptchaValidator(
            captchaRequestHandler,
            captchaResponseDeserializer,
            captchaValidatorConfiguration
        );

        boolean result = captchaValidator.basicValidate(testResponse);

        assertTrue(result);
    }

    @Test
    public void testBasicValidateWithIP() {
        CaptchaRequestHandler captchaRequestHandler = mock(CaptchaRequestHandler.class);
        CaptchaResponseDeserializer captchaResponseDeserializer = mock(CaptchaResponseDeserializer.class);
        CaptchaValidatorConfiguration captchaValidatorConfiguration = new ValidatorConfiguration("Test");
        String testResponse = "SomeResponse";
        String testIP = "127.0.0.1";
        String requestHandlerResponse = "{\"success\": true}";
        CaptchaValidationResponse captchaValidationResponse = new ValidationResponse(
            ReCaptchaVersion.VERSION_2,
            true,
            mock(Date.class),
            ClientType.WEB,
            "localhost",
            0.5f,
            "home",
            new ValidationError[0]
        );

        try {
            when(
                captchaRequestHandler.request(
                    any(ImmutableValidatorConfiguration.class),
                    eq(testResponse),
                    eq(testIP)
                )
            ).thenReturn(requestHandlerResponse);
            when(
                captchaResponseDeserializer.deserialize(requestHandlerResponse)
            ).thenReturn(captchaValidationResponse);
        } catch (CaptchaRequestHandlerException e) {
            fail("Unexpected exception occurred while preparing tests", e);
        }

        CaptchaValidator captchaValidator = new GCaptchaValidator(
            captchaRequestHandler,
            captchaResponseDeserializer,
            captchaValidatorConfiguration
        );

        boolean result = captchaValidator.basicValidate(testResponse, testIP);

        assertTrue(result);
    }

    @Test
    public void testValidateWithoutIP() {
        CaptchaRequestHandler captchaRequestHandler = mock(CaptchaRequestHandler.class);
        CaptchaResponseDeserializer captchaResponseDeserializer = mock(CaptchaResponseDeserializer.class);
        CaptchaValidatorConfiguration captchaValidatorConfiguration = new ValidatorConfiguration("Test");
        String testResponse = "SomeResponse";
        String requestHandlerResponse = "{\"success\": true}";
        CaptchaValidationResponse captchaValidationResponse = new ValidationResponse(
            ReCaptchaVersion.VERSION_2,
            true,
            mock(Date.class),
            ClientType.WEB,
            "localhost",
            0.5f,
            "home",
            new ValidationError[0]
        );

        try {
            when(
                captchaRequestHandler.request(
                    any(ImmutableValidatorConfiguration.class),
                    eq(testResponse),
                    eq("")
                )
            ).thenReturn(requestHandlerResponse);
            when(
                captchaResponseDeserializer.deserialize(requestHandlerResponse)
            ).thenReturn(captchaValidationResponse);
        } catch (CaptchaRequestHandlerException e) {
            fail("Unexpected exception occurred while preparing tests", e);
        }

        CaptchaValidator captchaValidator = new GCaptchaValidator(
            captchaRequestHandler,
            captchaResponseDeserializer,
            captchaValidatorConfiguration
        );

        CaptchaValidationResponse result = captchaValidator.validate(testResponse);

        assertEquals(result, captchaValidationResponse);
    }

    @Test
    public void testValidateWithIP() {
        CaptchaRequestHandler captchaRequestHandler = mock(CaptchaRequestHandler.class);
        CaptchaResponseDeserializer captchaResponseDeserializer = mock(CaptchaResponseDeserializer.class);
        CaptchaValidatorConfiguration captchaValidatorConfiguration = new ValidatorConfiguration("Test");
        String testResponse = "SomeResponse";
        String testIP = "127.0.0.1";
        String requestHandlerResponse = "{\"success\": true}";
        CaptchaValidationResponse captchaValidationResponse = new ValidationResponse(
            ReCaptchaVersion.VERSION_2,
            true,
            mock(Date.class),
            ClientType.WEB,
            "localhost",
            0.5f,
            "home",
            new ValidationError[0]
        );

        try {
            when(
                captchaRequestHandler.request(
                    any(ImmutableValidatorConfiguration.class),
                    eq(testResponse),
                    eq(testIP)
                )
            ).thenReturn(requestHandlerResponse);
            when(
                captchaResponseDeserializer.deserialize(requestHandlerResponse)
            ).thenReturn(captchaValidationResponse);
        } catch (CaptchaRequestHandlerException e) {
            fail("Unexpected exception occurred while preparing tests", e);
        }

        CaptchaValidator captchaValidator = new GCaptchaValidator(
            captchaRequestHandler,
            captchaResponseDeserializer,
            captchaValidatorConfiguration
        );

        CaptchaValidationResponse result = captchaValidator.validate(testResponse, testIP);

        assertEquals(result, captchaValidationResponse);
    }

    @Test
    public void testValidateWithException() {
        CaptchaRequestHandler captchaRequestHandler = mock(CaptchaRequestHandler.class);
        CaptchaResponseDeserializer captchaResponseDeserializer = mock(CaptchaResponseDeserializer.class);
        CaptchaValidatorConfiguration captchaValidatorConfiguration = new ValidatorConfiguration("Test");
        String testResponse = "SomeResponse";
        String testIP = "127.0.0.1";

        try {
            when(
                captchaRequestHandler.request(
                    any(ImmutableValidatorConfiguration.class),
                    eq(testResponse),
                    eq(testIP)
                )
            ).thenThrow(new CaptchaRequestHandlerException());
        } catch (CaptchaRequestHandlerException e) {
            fail("Unexpected exception occurred while preparing tests", e);
        }

        CaptchaValidator captchaValidator = new GCaptchaValidator(
            captchaRequestHandler,
            captchaResponseDeserializer,
            captchaValidatorConfiguration
        );

        CaptchaValidationResponse result = captchaValidator.validate(testResponse, testIP);

        assertEquals(result.getReCaptchaVersion(), ReCaptchaVersion.VERSION_2);
        assertFalse(result.hasSucceeded());
        assertNull(result.getChallengeTimestamp());
        assertNull(result.getClientType());
        assertEquals(result.getHostnameOrPackageName(), "");
        assertEquals(result.getScore(), -1f);
        assertEquals(result.getAction(), "");
        ValidationError[] expectedErrors = new ValidationError[]{
            ValidationError.GCAPTCHAVALIDATOR_INTERNAL_ERROR
        };
        assertEquals(result.getErrors(), expectedErrors);
    }

    // --- Integration tests

    @Test
    public void testBasicValidateWithIPIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator(TEST_SECRET);

        boolean result = captchaValidator.basicValidate(
            "SomeData",
            "127.0.0.1"
        );

        assertTrue(result);
    }

    @Test
    public void testBasicValidateWithIPWithErrorIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator("SomeDefinitlyInvalidSecret!");

        boolean result = captchaValidator.basicValidate(
            "SomeData",
            "127.0.0.1"
        );
        assertFalse(result);
    }

    @Test
    public void testBasicValidateWithoutIPIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator(TEST_SECRET);

        boolean result = captchaValidator.basicValidate("SomeData");
        assertTrue(result);
    }

    @Test
    public void testBasicValidateWithoutIPWithErrorIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator("SomeDefinitlyInvalidSecret!");

        boolean result = captchaValidator.basicValidate("SomeData");
        assertFalse(result);
    }

    @Test
    public void testValidateWithIPIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator(TEST_SECRET);

        CaptchaValidationResponse captchaValidationResponse = captchaValidator.validate(
            "SomeData",
            "127.0.0.1"
        );
        assertTrue(captchaValidationResponse.hasSucceeded());
        assertNotNull(captchaValidationResponse.getChallengeTimestamp());
        assertEquals(captchaValidationResponse.getReCaptchaVersion(), ReCaptchaVersion.VERSION_2);
        assertEquals(captchaValidationResponse.getClientType(), ClientType.WEB);
        assertEquals(captchaValidationResponse.getHostnameOrPackageName(), "testkey.google.com");
        assertEquals(captchaValidationResponse.getErrors().length, 0);
    }

    @Test
    public void testValidateWithIPWithErrorIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator("SomeDefinitlyInvalidSecret!");

        CaptchaValidationResponse captchaValidationResponse = captchaValidator.validate(
            "SomeData",
            "127.0.0.1"
        );
        assertFalse(captchaValidationResponse.hasSucceeded());
        ValidationError[] expectedError = new ValidationError[]{
            ValidationError.INVALID_INPUT_RESPONSE,
            ValidationError.INVALID_INPUT_SECRET
        };
        assertEquals(captchaValidationResponse.getErrors(), expectedError);
    }

    @Test
    public void testValidateWithoutIPIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator(TEST_SECRET);

        CaptchaValidationResponse captchaValidationResponse = captchaValidator.validate("SomeData");
        assertTrue(captchaValidationResponse.hasSucceeded());
        assertNotNull(captchaValidationResponse.getChallengeTimestamp());
        assertEquals(captchaValidationResponse.getReCaptchaVersion(), ReCaptchaVersion.VERSION_2);
        assertEquals(captchaValidationResponse.getClientType(), ClientType.WEB);
        assertEquals(captchaValidationResponse.getHostnameOrPackageName(), "testkey.google.com");
        assertEquals(captchaValidationResponse.getErrors().length, 0);
    }

    @Test
    public void testValidateWithoutIPWithErrorIntegration() {
        CaptchaValidator captchaValidator = new GCaptchaValidator("SomeDefinitlyInvalidSecret!");

        CaptchaValidationResponse captchaValidationResponse = captchaValidator.validate("SomeData");
        assertFalse(captchaValidationResponse.hasSucceeded());
        ValidationError[] expectedError = new ValidationError[]{
            ValidationError.INVALID_INPUT_RESPONSE,
            ValidationError.INVALID_INPUT_SECRET
        };
        assertEquals(captchaValidationResponse.getErrors(), expectedError);
    }
}
