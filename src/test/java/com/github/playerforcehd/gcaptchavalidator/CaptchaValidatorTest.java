package com.github.playerforcehd.gcaptchavalidator;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test the {@link CaptchaValidator#createDefault(String)} function to create a properly configured
 * {@link GCaptchaValidator} instance.
 *
 * @author Pascal Zarrad
 * @since 3.0.0
 */
public class CaptchaValidatorTest {

    @Test
    public void testCreateDefault() {
        CaptchaValidator captchaValidator = CaptchaValidator.createDefault("Test");

        assertEquals(captchaValidator.getConfiguration().getSecretToken(), "Test");
        // All other values should be default...
    }

}
