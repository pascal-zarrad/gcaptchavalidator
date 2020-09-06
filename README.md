## GCaptchaValidator - A ReCaptcha 2.0 Validator ![CI](https://github.com/pascal-zarrad/gcaptchavalidator/workflows/CI/badge.svg?branch=develop)

### What is GCaptchaValidator?

GCaptchaValidator is a lightweight Java library to verify that an user is a valid human.
When developing a web application server, it is possible that an action of a user (like submitting
a form) should be validated to avoid bots from spamming. A client-sided validation isn't enough at this point,
because small changes in the JavaScript are enough to bypass a captcha. This is where Google's ReCaptcha comes in play.

If you create a web application you can easily verify a Google ReCaptcha response with this library on the backend
of your application.
All the background work like sending the request to Google or processing the JSon result
is being handled by GCaptchaValidator.

This library currently supports Google ReCaptcha 2.0 and Invisible ReCaptcha by Google (Both have the same server-sided validation process).
The new score based v3 has not been tested and is not officially supported. It might work for basic validation, 
but won't provide the verification score.
Official support will be added in future versions.

### How to use GCaptchaValidator

#### Installation

_If you're using Maven:_
```xml

<!-- GCaptchaValidator - Simple Google ReCaptcha 2.0 validation -->
<dependency>
    <groupId>com.github.playerforcehd</groupId>
    <artifactId>gcaptchavalidator</artifactId>
    <version>2.1.0</version>
    <scope>compile</scope>
</dependency>
    
```

_If you're not using Maven:_
Download the current release fro [GitHubs Releases](https://github.com/pascal-zarrad/GCaptchaValidator/releases).

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

## Contributing

You are welcome to contribute to GCaptchaValidator if you have improvements or bug fixes.
Refer to [Contributing](/CONTRIBUTING.md) if you want to be a contributor or simply create an issue.


