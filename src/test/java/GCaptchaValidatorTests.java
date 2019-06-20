/*
 * The MIT License
 *
 * Copyright (c) 2019 Pascal Zarrad
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
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.testng.Assert.*;

/**
 * Tests all functions of the {@link com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator} library
 *
 * @author PlayerForceHD
 * @since 1.0.0
 */
public class GCaptchaValidatorTests {

    /**
     * A test secret key provided by Google
     */
    private final String gReCaptchaTestSecret = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";

    /**
     * The remote ip used for testing
     */
    private final String remoteIP = "127.0.0.1";

    /**
     * The valid response used for testing
     */
    private String acceptedResponse = "03AJz9lvRRl27ls2cnen32HC_LRxepB9xmLps0GcDMJfIGHIOaPWW29X-_DlvNGo5Tmx6lANsUHhko4CpKLYKvTLQQDjLrefVEyl7A5nuF26FsooF_GQ_O5r-EOX_FbAQ0RVc9vGrI7Lk_Bp_JzukTPdq4WgP-qSLbYErV-btJIwYh9MNmxrFn-5RUqC08T4WnSp6-er8nAt2YwkqM1hKTlsMm-6VulyQD49UwoJ-Y_YBah8v4snxw-KI-8Fa09gQp0a449BK6N5XiH9AfUbH7V7f_jRXuIUu22HTLJBz3AfHH-P5t6U9ZsY4";

    /**
     * The {@link WireMockServer}used to mock a WebServer to test the execution of WebHooks
     */
    private WireMockServer wireMockServer;

    /**
     * Initializes the WireMockServer used to test the ReCaptcha requests
     */
    @BeforeMethod
    public void prepare() {
        // Start a mocked WebServer
        this.wireMockServer = new WireMockServer(options().dynamicPort());
        this.wireMockServer.start();
    }

    @AfterMethod
    public void reset() {
        // Stop a mocked WebServer
        this.wireMockServer.stop();
    }

    /**
     * Setup the stubs that handle the
     */
    private void setupWebStubs() {
        this.wireMockServer.addMockServiceRequestListener(new RequestListener() {
            @Override
            public void requestReceived(Request request, Response response) {
                System.out.println(request.getBodyAsString());
                System.out.println("Response: " + response.getBodyAsString());
            }
        });
        // Stub validation with everything set
        this.wireMockServer.stubFor(any(urlPathEqualTo("/recaptcha/api/siteverify"))
                .withHeader("User-Agent", equalTo("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo("secret=" + this.gReCaptchaTestSecret + "&remoteip=" + this.remoteIP + "&response=" + this.acceptedResponse))
                .willReturn(aResponse().withBody("{ \"success\": true, \"challenge_ts\": \"2019-06-17T20:33:57Z\", \"hostname\": \"testkey.google.com\" }"))
        );
        // Stub validation without remote ip set
        this.wireMockServer.stubFor(any(urlPathEqualTo("/recaptcha/api/siteverify"))
                .withHeader("User-Agent", equalTo("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo("secret=" + this.gReCaptchaTestSecret + "&remoteip&response=" + this.acceptedResponse))
                .willReturn(aResponse().withBody("{ \"success\": true, \"challenge_ts\": \"2019-06-17T20:33:57Z\", \"hostname\": \"testkey.google.com\" }")));

        // Stub validation with wrong response and without remote ip set
        this.wireMockServer.stubFor(any(urlPathEqualTo("/recaptcha/api/siteverify"))
                .withHeader("User-Agent", equalTo("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo("secret=" + this.gReCaptchaTestSecret + "&remoteip&response=THIS+IS+A+INVALID+RESPONSE"))
                .willReturn(aResponse().withBody("{ \"success\": false, \"error-codes\": [\"invalid-input-response\", \"invalid-input-secret\"] }"))
        );
        // Stub validation without any configuration
        this.wireMockServer.stubFor(any(urlPathEqualTo("/recaptcha/api/siteverify"))
                .withHeader("User-Agent", equalTo("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo("secret=&remoteip&response="))
                .willReturn(aResponse().withBody("{ \"success\": false, \"error-codes\": [\"invalid-input-response\", \"invalid-input-secret\"] }")));
    }

    /**
     * Returns the fake SiteVerify URL
     */
    private String getMockedSiteVerifyURL() {
        return "http://localhost:" + this.wireMockServer.port() + "/recaptcha/api/siteverify";
    }

    /**
     * Tests if the default URL of a {@link CaptchaValidationConfiguration} does point to Google's SiteVerify API
     */
    @Test
    public void testGCaptchaValidationConfigurationPointsToGoogleByDefault() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withRemoteIP(this.remoteIP)
                .build();
        assertEquals(configuration.getSiteVerifyURL(), GCaptchaValidator.GOOGLE_SITEVERIFY_URL, "The default SiteVerifyURL does not point to Google!");
    }

    /**
     * Tests if the default URL of a {@link CaptchaValidationConfiguration} does point to Google's SiteVerify API
     */
    @Test
    public void testGCaptchaValidationConfigurationPointsToCustomURL() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withRemoteIP(this.remoteIP)
                .withGoogleSiteVerifyURL("TEST")
                .build();
        assertNotEquals(configuration.getSiteVerifyURL(), GCaptchaValidator.GOOGLE_SITEVERIFY_URL, "The SiteVerifyURL does point to Google but a custom URL has been declared in the configuration!");
    }

    /**
     * Tests the Google ReCaptcha Validation synchronous with a given RemoteIP
     */
    @Test
    public void testGCaptchaValidationWithRemoteIPSet() {
        // Prepare Test in our mock server
        this.setupWebStubs();
        // Test
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withRemoteIP(this.remoteIP)
                .withGoogleSiteVerifyURL(this.getMockedSiteVerifyURL()) // Set custom URL for testing
                .build();
        assertNotNull(configuration, "CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!");
        assertNotNull(configuration.getSecret(), "CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!");
        assertNotNull(configuration.getRemoteIP(), "CaptchaValidationConfiguration.getRemoteIP(); is null, but has been initialized through builder!");
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull(request, "CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)");
        CaptchaValidationResult result = null;
        try {
            result = request.validate(this.acceptedResponse).get();
        } catch (InterruptedException | ExecutionException e) {
            fail("Failed to validate response", e);
        }
        assertNotNull(result, "CaptchaValidationResult (result) is null, but validation has run!");
        assertTrue(result.isSuccess(), "True request was made but success is false");
        assertNotNull(result.getChallengeTS(), "ChallengeTS is null, but true request was made.");
        assertNotNull(result.getHostName(), "HostName is not null, but true request was made.");
        assertTrue(result.getErrorCodes().isEmpty(), "ErrorCodes is not empty, but true request was made.");
    }

    @Test
    public void testGCaptchaValidationWithoutRemoteIPSet() {
        // Prepare Test in our mock server
        this.setupWebStubs();
        // Test
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withGoogleSiteVerifyURL(getMockedSiteVerifyURL()) // Set custom URL for testing
                .build();
        assertNotNull(configuration, "CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!");
        assertNotNull(configuration.getSecret(), "CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!");
        assertNull(configuration.getRemoteIP(), "CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!");
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull(request, "CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)");
        CaptchaValidationResult result = null;
        try {
            result = request.validate(this.acceptedResponse).get();
        } catch (InterruptedException | ExecutionException e) {
            fail("Failed to validate response", e);
        }
        assertNotNull(result, "CaptchaValidationResult (result) is null, but validation has runned!");
        assertTrue(result.isSuccess(), "True request was made but success is false");
        assertNotNull(result.getChallengeTS(), "ChallengeTS is null, but true request was made.");
        assertNotNull(result.getHostName(), "HostName is not null, but true request was made.");
        assertTrue(result.getErrorCodes().isEmpty(), "ErrorCodes is not empty, but true request was made.");
    }

    @Test
    public void testGCaptchaValidationWithoutRemoteIPSetAndWrongResponse() {
        // Prepare Test in our mock server
        this.setupWebStubs();
        // Test
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withGoogleSiteVerifyURL(getMockedSiteVerifyURL()) // Set custom URL for testing
                .build();
        assertNotNull(configuration, "CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!");
        assertNotNull(configuration.getSecret(), "CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!");
        assertNull(configuration.getRemoteIP(), "CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!");
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull(request, "CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)");
        CaptchaValidationResult result = null;
        try {
            result = request.validate("THIS IS A INVALID RESPONSE").get();
        } catch (InterruptedException | ExecutionException e) {
            fail("Failed to validate response", e);
        }
        assertNotNull(result, "CaptchaValidationResult (result) is null, but validation has runned!");
        assertFalse(result.isSuccess(), "False request was made but success is true");
        assertFalse(result.getErrorCodes().isEmpty(), "ErrorCodes is empty, but false request was made.");
    }

    @Test
    public void testGCaptchaValidationWithoutAnything() {
        // Prepare Test in our mock server
        this.setupWebStubs();
        // Test
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret("")
                .withGoogleSiteVerifyURL(getMockedSiteVerifyURL()) // Set custom URL for testing
                .build();
        assertNotNull(configuration, "CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!");
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull(request, "CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)");
        CaptchaValidationResult result = null;
        try {
            result = request.validate("").get();
        } catch (InterruptedException | ExecutionException e) {
            fail("Failed to validate response", e);
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
            assertTrue(result.getErrorCodes().contains(CaptchaValidationError.MISSING_INPUT_SECRET), "Result does not include '" + CaptchaValidationError.INVALID_INPUT_SECRET.toString() + "', but the response was empty");
        }
        if (missingResponse) {
            assertTrue(result.getErrorCodes().contains(CaptchaValidationError.MISSING_INPUT_RESPONSE), "Result does not include '" + CaptchaValidationError.INVALID_INPUT_RESPONSE.toString() + "', but the response was empty");
        }
    }
}
