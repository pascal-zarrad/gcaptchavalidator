## GCaptchaValidator - A ReCaptcha 2.0 Validator [![Build Status](https://travis-ci.org/pascal-zarrad/GCaptchaValidator.svg?branch=master)](https://travis-ci.org/pascal-zarrad/GCaptchaValidator)

### What is GCaptchaValidator?

GCaptchaValidator is a lightweight Java library to verify that an user is a valid human.
When developing a web application server, it is possible that an users action (like submitting
a form) should be validated to avoid bots from spamming. A client-sided validation isn't enough at this point,
because small changes in the JavaScript are enough to bypass a captcha.

If you create a web application you can easily verify a Google ReCaptcha validation with this library.
All the background work like sending the request to google or processing the JSon result
is handled by GCaptchaValidator.

This supports Google ReCaptcha 2.0 and the Invisible ReCaptcha by Google (Both have the same server-sided validation process).

### How to use GCaptchaValidator

#### Installation

If you're using Maven:
```xml

<!-- GCaptchaValidator - Simple Google ReCpatcha 2.0 validation -->
<dependency>
    <groupId>com.github.playerforcehd</groupId>
    <artifactId>gcaptchavalidator</artifactId>
    <version>2.1.0</version>
    <scope>compile</scope>
</dependency>
    
```

Else:
~~Download a release from GitHub and add it to your project,
like all your other libraries.~~

Support for manual downloads has been dropped with version 2.0.0, please use maven or build the library on your local machine!

**NOTE: GCaptchaValidator requires at least Java 7.**

#### How to use it

**Step 1:** Create a CaptchaValidationConfiguration and set the information required for the validation.
The values 'secret' and 'response' are required.
The value 'remoteIP' is optional.
For more information about these values, take a look at Googles
ReCaptcha 2.0 API documentation.

```java

// Create the configuration for the request(s)
CaptchaValidationConfiguration captchaValidationConfiguration = GCaptchaValidator.createConfigurationBuilder()
    .withSecret("xxxxYourSecretxxxx")
    .build();

```

**Step 2:** Create a CaptchaValidationRequest and fetch a result
After creating the configuration you need a request which can be fetched.
To create a CaptchaValidationRequest you have to call the 'GCaptchaValidator#createRequest' method.
Then fetch a result asynchronous or synchronous through the 'validate(response)' method of your obtained CaptchaValidationRequest object.
After fetching the result you can work with the result through the gathered 'CaptchaValidationResult' object, which stores the entire response from Google mapped to a POJO.

```java

// Create the request which will be executed
CaptchaValidationRequest captchaValidationRequest = GCaptchaValidator.createRequest(captchaValidationConfiguration);

// Fetching a result synchronous
try {
    ListenableFuture<CaptchaValidationResult> futureResult = captchaValidationRequest.validate("xxxxYourResponsexxxx");
    CaptchaValidationResult synchronousResult = futureResult.get();
    // Do anything with your result, like checking if the request was a success
    if(synchronousResult.isValid()){
        System.out.println("Yeah! valid response. Captcha was really filled out by the user!");
    }
} catch (IOException | CaptchaValidationException e) {
    // Anything went wrong
    e.printStackTrace();
}

// Fetching a result asynchronous
FutureCallback<CaptchaValidationResult> resultCallback = new FutureCallback<CaptchaValidationResult>() {
    @Override
    public void onSuccess(@Nullable CaptchaValidationResult result) {
        // Do anything with your result, like checking if the request was a success
        if(asynchronousResult.isValid()){
            System.out.println("Yeah! valid response. Captcha was really filled out by the user!");
        }
    }
    @Override
    public void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        // Anything went wrong
    }
};
ListenableFuture<CaptchaValidationResult> futureResult = captchaValidationRequest.validate("xxxxYourResponsexxxx");
captchaValidationRequest.addFutureCallback(futureResult, resultCallback);

```

That's all what this library does.
It simply simplifies the validation of a Google ReCaptcha 2.0 request
in Java.

### Help to improve this library

Every developer out there can help to improve this library.
Make your changes to the code and create a pull request.
If your changes are good and working, they will be accepted.

### Reporting a bug

To report a bug use GitHubs Issues function.
The report should include:

- A *good* description of the problem that occured
- How often does the problem occur
- If you know how: Tell how to duplicate the problem.
- The code which produces the error

