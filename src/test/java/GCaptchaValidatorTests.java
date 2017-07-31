/*
 * The MIT License
 *
 * Copyright (c) 2016 Pascal Zarrad
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

import com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator;
import com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration;
import com.github.playerforcehd.gcaptchavalidator.captchaverification.CaptchaValidationError;
import com.github.playerforcehd.gcaptchavalidator.captchaverification.CaptchaValidationRequest;
import com.github.playerforcehd.gcaptchavalidator.captchaverification.CaptchaValidationResult;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * Tests all functions of the {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} library
 *
 * @author PlayerForceHD
 * @version 1.0.0
 * @since 1.0.0
 */
public class GCaptchaValidatorTests {

    /**
     * A test secret key provided by Google
     */
    private String gReCaptchaTestSecret;

    /**
     * Initializes the Test values
     */
    @Before
    public void initializeTest() {
        this.gReCaptchaTestSecret = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";
    }

    /**
     * Tests the Google ReCaptcha Validation synchronous with a given RemoteIP
     */
    @Test
    public void testGCaptchaValidationSynchronousWithRemoteIPSet() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withRemoteIP("127.0.0.1")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNotNull("CaptchaValidationConfiguration.getRemoteIP(); is null, but has been initialized through builder!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        CaptchaValidationResult result = null;
        try {
            result = request.validate("03AJz9lvRRl27ls2cnen32HC_LRxepB9xmLps0GcDMJfIGHIOaPWW29X-_DlvNGo5Tmx6lANsUHhko4CpKLYKvTLQQDjLrefVEyl7A5nuF26FsooF_GQ_O5r-EOX_FbAQ0RVc9vGrI7Lk_Bp_JzukTPdq4WgP-qSLbYErV-btJIwYh9MNmxrFn-5RUqC08T4WnSp6-er8nAt2YwkqM1hKTlsMm-6VulyQD49UwoJ-Y_YBah8v4snxw-KI-8Fa09gQp0a449BK6N5XiH9AfUbH7V7f_jRXuIUu22HTLJBz3AfHH-P5t6U9ZsY4").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail("Failed to validate response");
        }
        assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
        assertTrue("True request was made but success is false", result.isSuccess());
        assertNotNull("ChallengeTS is null, but true request was made.", result.getChallengeTS());
        assertNotNull("HostName is not null, but true request was made.", result.getHostName());
        assertTrue("ErrorCodes is not empty, but true request was made.", result.getErrorCodes().isEmpty());
    }

    @Test
    public void testGCaptchaValidationAsynchronousWithoutRemoteIPSet() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNull("CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        FutureCallback<CaptchaValidationResult> resultCallback = new FutureCallback<CaptchaValidationResult>() {
            @Override
            public void onSuccess(@Nullable CaptchaValidationResult result) {
                assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
                assertTrue("True request was made but success is false", result.isSuccess());
                assertNotNull("ChallengeTS is null, but true request was made.", result.getChallengeTS());
                assertNotNull("HostName is not null, but true request was made.", result.getHostName());
                assertTrue("ErrorCodes is not empty, but true request was made.", result.getErrorCodes().isEmpty());
            }
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                fail("Failed to validate response");
            }
        };
        ListenableFuture<CaptchaValidationResult> validationFuture = request.validate("03AJz9lvRRl27ls2cnen32HC_LRxepB9xmLps0GcDMJfIGHIOaPWW29X-_DlvNGo5Tmx6lANsUHhko4CpKLYKvTLQQDjLrefVEyl7A5nuF26FsooF_GQ_O5r-EOX_FbAQ0RVc9vGrI7Lk_Bp_JzukTPdq4WgP-qSLbYErV-btJIwYh9MNmxrFn-5RUqC08T4WnSp6-er8nAt2YwkqM1hKTlsMm-6VulyQD49UwoJ-Y_YBah8v4snxw-KI-8Fa09gQp0a449BK6N5XiH9AfUbH7V7f_jRXuIUu22HTLJBz3AfHH-P5t6U9ZsY4");
        request.addFutureCallback(validationFuture, resultCallback);
    }

    @Test
    public void testGCaptchaValidationAsynchronousWithoutRemoteIPSetAndWrongResponse() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNull("CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        FutureCallback<CaptchaValidationResult> resultCallback = new FutureCallback<CaptchaValidationResult>() {
            @Override
            public void onSuccess(@Nullable CaptchaValidationResult result) {
                assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
                assertFalse("False request was made but success is true", result.isSuccess());
                assertFalse("ErrorCodes is empty, but false request was made.", result.getErrorCodes().isEmpty());
            }
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                Assert.fail("Failed with an Exception!");
            }
        };
        // Changed first 0 to 9
        ListenableFuture<CaptchaValidationResult> futureResult = request.validate("93AJz9lvRRl27ls2cnen32HC_LRxepB9xmLps0GcDMJfIGHIOaPWW29X-_DlvNGo5Tmx6lANsUHhko4CpKLYKvTLQQDjLrefVEyl7A5nuF26FsooF_GQ_O5r-EOX_FbAQ0RVc9vGrI7Lk_Bp_JzukTPdq4WgP-qSLbYErV-btJIwYh9MNmxrFn-5RUqC08T4WnSp6-er8nAt2YwkqM1hKTlsMm-6VulyQD49UwoJ-Y_YBah8v4snxw-KI-8Fa09gQp0a449BK6N5XiH9AfUbH7V7f_jRXuIUu22HTLJBz3AfHH-P5t6U9ZsY4");
        request.addFutureCallback(futureResult, resultCallback);
    }

    @Test
    public void testGCaptchaValidationSynchronousWithoutAnything() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret("")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        CaptchaValidationResult result = null;
        try {
            result = request.validate("").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            fail("Failed to validate response");
        }
        boolean missingSecret = false;
        boolean missingResponse = false;
        for (CaptchaValidationError captchaValidationError : result.getErrorCodes()) {
            if (captchaValidationError.equals(CaptchaValidationError.MISSING_INPUT_SECRET)) {
                missingSecret = true;
                continue;
            }
            if (captchaValidationError.equals(CaptchaValidationError.MISSING_INPUT_RESPONSE)) {
                missingResponse = true;
            }
        }
        if (missingSecret) {
            assertTrue("Result does not include '" + CaptchaValidationError.INVALID_INPUT_SECRET.toString() + "', but the response was empty", result.getErrorCodes().contains(CaptchaValidationError.MISSING_INPUT_SECRET));
        }
        if (missingResponse) {
            assertTrue("Result does not include '" + CaptchaValidationError.INVALID_INPUT_RESPONSE.toString() + "', but the response was empty", result.getErrorCodes().contains(CaptchaValidationError.MISSING_INPUT_RESPONSE));
        }
    }
}
