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

package com.github.playerforcehd.gcaptchavalidator.data;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

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

        assertEquals(actualError, expectedError);
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
