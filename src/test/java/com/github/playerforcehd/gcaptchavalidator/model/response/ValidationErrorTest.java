package com.github.playerforcehd.gcaptchavalidator.model.response;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the validation error parser
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class ValidationErrorTest {
    @Test(dataProvider = "getValidationErrorByCodeDataProvider")
    public void testGetValidationErrorByCode(String testError, ValidationError expectedError) {
        ValidationError actualError = ValidationError.getValidationErrorByCode(testError);

        Assert.assertEquals(actualError, expectedError);
    }

    @DataProvider
    public Object[][] getValidationErrorByCodeDataProvider() {
        return new Object[][] {
            {"missing-input-secret", ValidationError.MISSING_INPUT_SECRET},
            {"invalid-input-secret", ValidationError.INVALID_INPUT_SECRET},
            {"missing-input-response", ValidationError.MISSING_INPUT_RESPONSE},
            {"invalid-input-response", ValidationError.INVALID_INPUT_RESPONSE},
            {"bad-request", ValidationError.BAD_REQUEST},
            {"timeout-or-duplicate", ValidationError.TIMEOUT_OR_DUPLICATE}
        };
    }
}
