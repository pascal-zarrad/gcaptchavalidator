/*
 * The MIT License
 *
 * Copyright (c) 2016 PlayerForceHD
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

import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationException;
import com.github.playerforcehd.gcaptchavalidator.GCaptchaValidator;
import com.github.playerforcehd.gcaptchavalidator.captchaconfiguration.CaptchaValidationConfiguration;
import com.github.playerforcehd.gcaptchavalidator.captchaverification.CaptchaValidationRequest;
import com.github.playerforcehd.gcaptchavalidator.captchaverification.CaptchaValidationResult;
import com.github.playerforcehd.gcaptchavalidator.util.Callback;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;

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
                .withResponse("03AHJ_Vus6NaaeVEVMLDUZ2gkXTc3q3FCG6_rNS7LlCyllroSbKFrf6WjHUkQPwV_EszE1kDPdSJOqJD_snLXrozAxFa_DIBXJB-LXlHq81xkwZ3tQ63yIAuVYC1lbqOd3CcqLpY9fBIBdzjpjKRg_ozd3THXoJ_BNQMoYiuIgssvuHd0GC3jiyuBbqksUx0j_HORSkkdl1DfEsoCPdpkH0I-uSqHgwrXB6hnWY-l_FvWxyTF2aP5TL_VCykfezFWL6DraEpiEGMAqk5EovWlAIERn-B0saNDxLXNYlKEIVv1ad711tY_iNlVy4hQNYnMKfZHUV0DIft2F-RCNV2BMeoPAyZFbKyTlE1K2uZRtOVmGMqiWfNmvz49sSP3XuWGwDHQnm5HyYfkIfRs7jYriBWOimU2W-ysdA5HpIax2C4n8PEyQ2t8XnSI69OnFOxRQFuCQJ9UM9FScIBQ9Eqn0G4_Blojy3Bbtj9L8Pd_m9UvUL8vIhAbu3EAeBkfhoWOuKmTPTyXINZaWsQ-rpnUpw9Bvt8q7j6_VdfVCSIR3Sa5F0wBnpVDvFx8SPwKQvb4Sg_8LD0QWa7ShvIpAdhegKKWGw-Fgbub4iM3-A6V3LStmUirrzHRJCE6kuYGeWpeowCbFmiKHCL91xXVwTNQwje2kKilW9bwqEb6czuJfvWKZ06esRmfAG278MECW4GCoNQNDnHTfC-Q3WKwBV8belTMXON3pB-1jJ4Q13owjWf0heP-LFTNFmpedbBGxPyN1-lneHQqwRd4K6BNp7s910G6ccPGcg1aZK03J4T-eqE2p0fDJB_kDCua261YgRLJP_oQtcsIl-0wgAOzgsqHegZbrHdz63B8BUh_IUYbdgAzZtPNCWSOYjRGjlESB2vBy6KMR1c4Yz11y7LoOuNG8Niqa0Q4obtmUUF-KUduQjbzZ5aQZYtml2r6C3GMHXun4f9DusTW7Y8KyQ_Hk7bFMS6RjHLlvb2sPk3_hT5AWiB2Hno9qvo-v4ds")
                .withRemoteIP("127.0.0.1")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNotNull("CaptchaValidationConfiguration.getResponse() is null, but has been initialized through builder!", configuration.getResponse());
        assertNotNull("CaptchaValidationConfiguration.getRemoteIP(); is null, but has been initialized through builder!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        try {
            CaptchaValidationResult result = request.fetchSync();
            assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
            assertTrue("True request was made but success is false", result.isSuccess());
            assertNotNull("ChallengeTS is null, but true request was made.", result.getChallengeTS());
            assertNotNull("HostName is not null, but true request was made.", result.getHostName());
            assertNull("ErrorCode is not null, but true request was made.", result.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed with an IOException");
        } catch (CaptchaValidationException e) {
            e.printStackTrace();
            fail("Failed with an CaptchaValidationException");
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Failed with an ParseException (Failed to parse date)");
        }
    }

    /**
     * Tests the Google ReCaptcha Validation synchronous without a given RemoteIP
     */
    @Test
    public void testGCaptchaValidationSynchronousWithoutRemoteIPSet() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withResponse("03AHJ_Vus6NaaeVEVMLDUZ2gkXTc3q3FCG6_rNS7LlCyllroSbKFrf6WjHUkQPwV_EszE1kDPdSJOqJD_snLXrozAxFa_DIBXJB-LXlHq81xkwZ3tQ63yIAuVYC1lbqOd3CcqLpY9fBIBdzjpjKRg_ozd3THXoJ_BNQMoYiuIgssvuHd0GC3jiyuBbqksUx0j_HORSkkdl1DfEsoCPdpkH0I-uSqHgwrXB6hnWY-l_FvWxyTF2aP5TL_VCykfezFWL6DraEpiEGMAqk5EovWlAIERn-B0saNDxLXNYlKEIVv1ad711tY_iNlVy4hQNYnMKfZHUV0DIft2F-RCNV2BMeoPAyZFbKyTlE1K2uZRtOVmGMqiWfNmvz49sSP3XuWGwDHQnm5HyYfkIfRs7jYriBWOimU2W-ysdA5HpIax2C4n8PEyQ2t8XnSI69OnFOxRQFuCQJ9UM9FScIBQ9Eqn0G4_Blojy3Bbtj9L8Pd_m9UvUL8vIhAbu3EAeBkfhoWOuKmTPTyXINZaWsQ-rpnUpw9Bvt8q7j6_VdfVCSIR3Sa5F0wBnpVDvFx8SPwKQvb4Sg_8LD0QWa7ShvIpAdhegKKWGw-Fgbub4iM3-A6V3LStmUirrzHRJCE6kuYGeWpeowCbFmiKHCL91xXVwTNQwje2kKilW9bwqEb6czuJfvWKZ06esRmfAG278MECW4GCoNQNDnHTfC-Q3WKwBV8belTMXON3pB-1jJ4Q13owjWf0heP-LFTNFmpedbBGxPyN1-lneHQqwRd4K6BNp7s910G6ccPGcg1aZK03J4T-eqE2p0fDJB_kDCua261YgRLJP_oQtcsIl-0wgAOzgsqHegZbrHdz63B8BUh_IUYbdgAzZtPNCWSOYjRGjlESB2vBy6KMR1c4Yz11y7LoOuNG8Niqa0Q4obtmUUF-KUduQjbzZ5aQZYtml2r6C3GMHXun4f9DusTW7Y8KyQ_Hk7bFMS6RjHLlvb2sPk3_hT5AWiB2Hno9qvo-v4ds")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNotNull("CaptchaValidationConfiguration.getResponse() is null, but has been initialized through builder!", configuration.getResponse());
        assertNull("CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        try {
            CaptchaValidationResult result = request.fetchSync();
            assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
            assertTrue("True request was made but success is false", result.isSuccess());
            assertNotNull("ChallengeTS is null, but true request was made.", result.getChallengeTS());
            assertNotNull("HostName is not null, but true request was made.", result.getHostName());
            assertNull("ErrorCode is not null, but true request was made.", result.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed with an IOException");
        } catch (CaptchaValidationException e) {
            e.printStackTrace();
            fail("Failed with an CaptchaValidationException");
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Failed with an ParseException (Failed to parse date)");
        }
    }

    @Test
    public void testGCaptchaValidationSynchronousWithoutRemoteIPSetAndWrongResponse() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                //Changed first 0 (zero) to O (o)
                .withResponse("O3AHJ_Vus6NaaeVEVMLDUZ2gkXTc3q3FCG6_rNS7LlCyllroSbKFrf6WjHUkQPwV_EszE1kDPdSJOqJD_snLXrozAxFa_DIBXJB-LXlHq81xkwZ3tQ63yIAuVYC1lbqOd3CcqLpY9fBIBdzjpjKRg_ozd3THXoJ_BNQMoYiuIgssvuHd0GC3jiyuBbqksUx0j_HORSkkdl1DfEsoCPdpkH0I-uSqHgwrXB6hnWY-l_FvWxyTF2aP5TL_VCykfezFWL6DraEpiEGMAqk5EovWlAIERn-B0saNDxLXNYlKEIVv1ad711tY_iNlVy4hQNYnMKfZHUV0DIft2F-RCNV2BMeoPAyZFbKyTlE1K2uZRtOVmGMqiWfNmvz49sSP3XuWGwDHQnm5HyYfkIfRs7jYriBWOimU2W-ysdA5HpIax2C4n8PEyQ2t8XnSI69OnFOxRQFuCQJ9UM9FScIBQ9Eqn0G4_Blojy3Bbtj9L8Pd_m9UvUL8vIhAbu3EAeBkfhoWOuKmTPTyXINZaWsQ-rpnUpw9Bvt8q7j6_VdfVCSIR3Sa5F0wBnpVDvFx8SPwKQvb4Sg_8LD0QWa7ShvIpAdhegKKWGw-Fgbub4iM3-A6V3LStmUirrzHRJCE6kuYGeWpeowCbFmiKHCL91xXVwTNQwje2kKilW9bwqEb6czuJfvWKZ06esRmfAG278MECW4GCoNQNDnHTfC-Q3WKwBV8belTMXON3pB-1jJ4Q13owjWf0heP-LFTNFmpedbBGxPyN1-lneHQqwRd4K6BNp7s910G6ccPGcg1aZK03J4T-eqE2p0fDJB_kDCua261YgRLJP_oQtcsIl-0wgAOzgsqHegZbrHdz63B8BUh_IUYbdgAzZtPNCWSOYjRGjlESB2vBy6KMR1c4Yz11y7LoOuNG8Niqa0Q4obtmUUF-KUduQjbzZ5aQZYtml2r6C3GMHXun4f9DusTW7Y8KyQ_Hk7bFMS6RjHLlvb2sPk3_hT5AWiB2Hno9qvo-v4ds")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNotNull("CaptchaValidationConfiguration.getResponse() is null, but has been initialized through builder!", configuration.getResponse());
        assertNull("CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        try {
            CaptchaValidationResult result = request.fetchSync();
            assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
            assertFalse("True request was made but success is false", result.isSuccess());
            assertNotNull("ErrorCode is null, but false request was made.", result.getErrorCode());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed with an IOException");
        } catch (CaptchaValidationException e) {
            e.printStackTrace();
            fail("Failed with an CaptchaValidationException");
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Failed with an ParseException (Failed to parse date)");
        }
    }

    @Test
    public void testGCaptchaValidationAsynchronousWithoutRemoteIPSet() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withResponse("03AHJ_Vus6NaaeVEVMLDUZ2gkXTc3q3FCG6_rNS7LlCyllroSbKFrf6WjHUkQPwV_EszE1kDPdSJOqJD_snLXrozAxFa_DIBXJB-LXlHq81xkwZ3tQ63yIAuVYC1lbqOd3CcqLpY9fBIBdzjpjKRg_ozd3THXoJ_BNQMoYiuIgssvuHd0GC3jiyuBbqksUx0j_HORSkkdl1DfEsoCPdpkH0I-uSqHgwrXB6hnWY-l_FvWxyTF2aP5TL_VCykfezFWL6DraEpiEGMAqk5EovWlAIERn-B0saNDxLXNYlKEIVv1ad711tY_iNlVy4hQNYnMKfZHUV0DIft2F-RCNV2BMeoPAyZFbKyTlE1K2uZRtOVmGMqiWfNmvz49sSP3XuWGwDHQnm5HyYfkIfRs7jYriBWOimU2W-ysdA5HpIax2C4n8PEyQ2t8XnSI69OnFOxRQFuCQJ9UM9FScIBQ9Eqn0G4_Blojy3Bbtj9L8Pd_m9UvUL8vIhAbu3EAeBkfhoWOuKmTPTyXINZaWsQ-rpnUpw9Bvt8q7j6_VdfVCSIR3Sa5F0wBnpVDvFx8SPwKQvb4Sg_8LD0QWa7ShvIpAdhegKKWGw-Fgbub4iM3-A6V3LStmUirrzHRJCE6kuYGeWpeowCbFmiKHCL91xXVwTNQwje2kKilW9bwqEb6czuJfvWKZ06esRmfAG278MECW4GCoNQNDnHTfC-Q3WKwBV8belTMXON3pB-1jJ4Q13owjWf0heP-LFTNFmpedbBGxPyN1-lneHQqwRd4K6BNp7s910G6ccPGcg1aZK03J4T-eqE2p0fDJB_kDCua261YgRLJP_oQtcsIl-0wgAOzgsqHegZbrHdz63B8BUh_IUYbdgAzZtPNCWSOYjRGjlESB2vBy6KMR1c4Yz11y7LoOuNG8Niqa0Q4obtmUUF-KUduQjbzZ5aQZYtml2r6C3GMHXun4f9DusTW7Y8KyQ_Hk7bFMS6RjHLlvb2sPk3_hT5AWiB2Hno9qvo-v4ds")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNotNull("CaptchaValidationConfiguration.getResponse() is null, but has been initialized through builder!", configuration.getResponse());
        assertNull("CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        request.fetchAsync(new Callback<CaptchaValidationResult>() {
            @Override
            public void invoke(CaptchaValidationResult result) {
                assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
                assertTrue("True request was made but success is false", result.isSuccess());
                assertNotNull("ChallengeTS is null, but true request was made.", result.getChallengeTS());
                assertNotNull("HostName is not null, but true request was made.", result.getHostName());
                assertNull("ErrorCode is not null, but true request was made.", result.getErrorCode());
                countDownLatch.countDown();
            }

            @Override
            public void fail(Throwable throwable) {
                throwable.printStackTrace();
                Assert.fail("Failed with an Exception!");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGCaptchaValidationAsynchronousWithoutRemoteIPSetAndWrongResponse() {
        CaptchaValidationConfiguration configuration = GCaptchaValidator.createConfigurationBuilder()
                .withSecret(this.gReCaptchaTestSecret)
                .withResponse("O3AHJ_Vus6NaaeVEVMLDUZ2gkXTc3q3FCG6_rNS7LlCyllroSbKFrf6WjHUkQPwV_EszE1kDPdSJOqJD_snLXrozAxFa_DIBXJB-LXlHq81xkwZ3tQ63yIAuVYC1lbqOd3CcqLpY9fBIBdzjpjKRg_ozd3THXoJ_BNQMoYiuIgssvuHd0GC3jiyuBbqksUx0j_HORSkkdl1DfEsoCPdpkH0I-uSqHgwrXB6hnWY-l_FvWxyTF2aP5TL_VCykfezFWL6DraEpiEGMAqk5EovWlAIERn-B0saNDxLXNYlKEIVv1ad711tY_iNlVy4hQNYnMKfZHUV0DIft2F-RCNV2BMeoPAyZFbKyTlE1K2uZRtOVmGMqiWfNmvz49sSP3XuWGwDHQnm5HyYfkIfRs7jYriBWOimU2W-ysdA5HpIax2C4n8PEyQ2t8XnSI69OnFOxRQFuCQJ9UM9FScIBQ9Eqn0G4_Blojy3Bbtj9L8Pd_m9UvUL8vIhAbu3EAeBkfhoWOuKmTPTyXINZaWsQ-rpnUpw9Bvt8q7j6_VdfVCSIR3Sa5F0wBnpVDvFx8SPwKQvb4Sg_8LD0QWa7ShvIpAdhegKKWGw-Fgbub4iM3-A6V3LStmUirrzHRJCE6kuYGeWpeowCbFmiKHCL91xXVwTNQwje2kKilW9bwqEb6czuJfvWKZ06esRmfAG278MECW4GCoNQNDnHTfC-Q3WKwBV8belTMXON3pB-1jJ4Q13owjWf0heP-LFTNFmpedbBGxPyN1-lneHQqwRd4K6BNp7s910G6ccPGcg1aZK03J4T-eqE2p0fDJB_kDCua261YgRLJP_oQtcsIl-0wgAOzgsqHegZbrHdz63B8BUh_IUYbdgAzZtPNCWSOYjRGjlESB2vBy6KMR1c4Yz11y7LoOuNG8Niqa0Q4obtmUUF-KUduQjbzZ5aQZYtml2r6C3GMHXun4f9DusTW7Y8KyQ_Hk7bFMS6RjHLlvb2sPk3_hT5AWiB2Hno9qvo-v4ds")
                .build();
        assertNotNull("CaptchaValidationConfiguration (configuration) is null, but has been initialized through builder!", configuration);
        assertNotNull("CaptchaValidationConfiguration.getSecret() is null, but has been initialized through builder!", configuration.getSecret());
        assertNotNull("CaptchaValidationConfiguration.getResponse() is null, but has been initialized through builder!", configuration.getResponse());
        assertNull("CaptchaValidationConfiguration.getRemoteIP(); is not null, but never has been initialized!!", configuration.getRemoteIP());
        CaptchaValidationRequest request = GCaptchaValidator.createRequest(configuration);
        assertNotNull("CaptchaValidationRequest (request) is null, but has been initialized through GCaptchaValidator.createRequest(configuration)", request);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        request.fetchAsync(new Callback<CaptchaValidationResult>() {
            @Override
            public void invoke(CaptchaValidationResult result) {
                assertNotNull("CaptchaValidationResult (result) is null, but validation has runned!", result);
                assertFalse("True request was made but success is false", result.isSuccess());
                assertNotNull("ErrorCode is null, but false request was made.", result.getErrorCode());
                countDownLatch.countDown();
            }

            @Override
            public void fail(Throwable throwable) {
                throwable.printStackTrace();
                Assert.fail("Failed with an Exception!");
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
