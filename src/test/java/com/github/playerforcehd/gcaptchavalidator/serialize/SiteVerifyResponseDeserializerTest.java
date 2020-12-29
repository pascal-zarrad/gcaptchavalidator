/*
 * The MIT License
 *
 * Copyright (c) 2020 Pascal Zarrad
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

package com.github.playerforcehd.gcaptchavalidator.serialize;

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse;
import com.github.playerforcehd.gcaptchavalidator.data.ClientType;
import com.github.playerforcehd.gcaptchavalidator.data.ValidationError;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;

/**
 * Tests for the default site verify response deserializer.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class SiteVerifyResponseDeserializerTest {
    @Test(dataProvider = "deserializeWithVersion2DataProvider")
    public void testDeserializeWithVersion2(String testResponse) {
        SiteVerifyResponseDeserializer deserializer = new SiteVerifyResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        Assert.assertTrue(result.hasSucceeded());
        Assert.assertEquals(result.getScore(), -1f);
        Assert.assertEquals(result.getAction(), "");
        Assert.assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        Assert.assertEquals(result.getClientType(), ClientType.WEB);
        Assert.assertEquals(result.getHostnameOrPackageName(), "localhost");
        Assert.assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithVersion2DataProvider() {
        return new Object[][] {
            {
                "{\"success\": true,\"challenge_ts\": \"2020-12-29T17:21:02Z\",\"hostname\": \"localhost\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithVersion2DataWithoutSuccessProvider")
    public void testDeserializeWithVersion2WithoutSuccess(String testResponse) {
        SiteVerifyResponseDeserializer deserializer = new SiteVerifyResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        Assert.assertFalse(result.hasSucceeded());
        Assert.assertEquals(result.getScore(), -1f);
        Assert.assertEquals(result.getAction(), "");
        Assert.assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        Assert.assertEquals(result.getClientType(), ClientType.WEB);
        Assert.assertEquals(result.getHostnameOrPackageName(), "localhost");
        Assert.assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithVersion2DataWithoutSuccessProvider() {
        return new Object[][] {
            {
                "{\"success\": false,\"challenge_ts\": \"2020-12-29T17:21:02Z\",\"hostname\": \"localhost\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithVersion3DataProvider")
    public void testDeserializeWithVersion3(String testResponse) {
        SiteVerifyResponseDeserializer deserializer = new SiteVerifyResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        Assert.assertTrue(result.hasSucceeded());
        Assert.assertEquals(result.getScore(), 0.5f);
        Assert.assertEquals(result.getAction(), "home");
        Assert.assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        Assert.assertEquals(result.getClientType(), ClientType.WEB);
        Assert.assertEquals(result.getHostnameOrPackageName(), "localhost");
        Assert.assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithVersion3DataProvider() {
        return new Object[][] {
            {
                "{\"success\": true,\"score\": 0.5,\"action\": \"home\"," +
                    "\"challenge_ts\": \"2020-12-29T17:21:02Z\",\"hostname\": \"localhost\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithPlatformAndroidDataProvider")
    public void testDeserializeWithPlatformAndroid(String testResponse) {
        SiteVerifyResponseDeserializer deserializer = new SiteVerifyResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        Assert.assertTrue(result.hasSucceeded());
        Assert.assertEquals(result.getScore(), -1f);
        Assert.assertEquals(result.getAction(), "");
        Assert.assertEquals(result.getChallengeTimestamp().getTime(), 1609262462000L);
        Assert.assertEquals(result.getClientType(), ClientType.ANDROID);
        Assert.assertEquals(result.getHostnameOrPackageName(), "com.github.playerforcehd.gcaptchavalidator");
        Assert.assertEquals(result.getErrors().length, 0);
    }

    @DataProvider
    public Object[][] deserializeWithPlatformAndroidDataProvider() {
        return new Object[][] {
            {
                "{\"success\": true,\"challenge_ts\": \"2020-12-29T17:21:02Z\"," +
                    "\"apk_package_name\": \"com.github.playerforcehd.gcaptchavalidator\"}"
            }
        };
    }

    @Test(dataProvider = "deserializeWithErrorCodesDataProvider")
    public void testDeserializeWithErrorCodes(String testResponse) {
        SiteVerifyResponseDeserializer deserializer = new SiteVerifyResponseDeserializer();
        CaptchaValidationResponse result = deserializer.deserialize(testResponse);

        Assert.assertFalse(result.hasSucceeded());
        Assert.assertEquals(result.getScore(), -1f);
        Assert.assertEquals(result.getAction(), "");
        Assert.assertNull(result.getChallengeTimestamp());
        Assert.assertNull(result.getClientType());
        Assert.assertEquals(result.getHostnameOrPackageName(), "");
        ValidationError[] expectedErrors = {
            ValidationError.MISSING_INPUT_SECRET,
            ValidationError.MISSING_INPUT_RESPONSE
        };
        Assert.assertEquals(result.getErrors(), expectedErrors);
    }

    @DataProvider
    public Object[][] deserializeWithErrorCodesDataProvider() {
        return new Object[][] {
            {
                "{\"success\": false, \"error-codes\": [\"missing-input-secret\", \"missing-input-response\"]}"
            }
        };
    }
}
