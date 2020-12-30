## GCaptchaValidator - A Google ReCaptcha Validator ![CI](https://github.com/pascal-zarrad/gcaptchavalidator/workflows/CI/badge.svg?branch=develop)

### What is GCaptchaValidator?

When developing a web application server, it is possible that an action of a user (like submitting
a form) should be validated to avoid bots from spamming you with requests. 
A client-sided validation isn't enough at this point, because small changes in the JavaScript through the browsers 
Devtools are enough to bypass such a captcha. This is where Google's ReCaptcha comes in play.
Googles ReCaptcha are the well-known checkboxes that you often find on registration pages or at contact forms
on websites. ReCaptcha will send a "response" to the server that can ba validated on the backend side of the
application by asking Google's service if the received response is valid.

This is where GCaptchaValidator comes into play. GCaptchaValidator will handle the request to Googles servers
and the necessary post-processing and output a developer-friendly object which contains all the data received.
The only compile/runtime dependency that GCaptchaValidator has is Google GSon (to
deserialize JSon) to keep the library slim.

This library supports:
 - Google ReCaptcha (Version 3)
 - Google ReCaptcha (Version 2)
 - Google Invisible ReCaptcha

### How to use GCaptchaValidator

#### Installation
**NOTE:** GCaptchaValidator requires at least Java 8.

_If you use Maven:_
```xml
<!-- GCaptchaValidator - Simple Google ReCaptcha 2.0 validation -->
<dependency>
    <groupId>com.github.playerforcehd</groupId>
    <artifactId>gcaptchavalidator</artifactId>
    <version>3.0.0</version>
    <scope>compile</scope>
</dependency>
```

The latest stable version is always equal to the current state of the `master` branch.
The current snapshot version is always equal to the current state of `develop` branch - except that "-SNAPSHOT" is 
appended at the end of the version when it was automatically deployed to Maven central.

The jars on Maven central are signed. 
The public key to check the signature can be pulled using the following  (requires GPG on your local machine):
```bash
gpg --keyserver hkp://pool.sks-keyservers.net --recv-keys EC417FA4D2890521
```

_If you're not using Maven:_
Download the current release from [GitHubs Releases](https://github.com/pascal-zarrad/GCaptchaValidator/releases).
The newest release is always on sync with the newest version on Maven central as artifacts and releases are created
automatically by GitHub Actions.

#### How to use GCaptchaValidator

##### Preamble:
It is pretty simple to get started with GCaptchaValidator.
The library allows validating responses in only two lines of code or
to apply more heavy customisations with a few more lines of code.

The result on a full validation request contains all the data received from Google.
To make work with the received response easier, the received data is structured as the following:
| Response         | Mapping                                              |
| ---------------- | ---------------------------------------------------- |
| success          | succeeded                                            |
| challenge_ts     | challengeTimestamp                                   |
| hostname         | hostnameOrPackageName in conjunction with clientType |
| apk_package_name | hostnameOrPackageName in conjunction with clientType |
| score            | score                                                |
| action           | action                                               |
| errors           | errors                                               |

Note that the default validator does make the passed configuration immutable. You won't be able to adjust it later
on without creating a new configurator and validator.

##### Basic usage
The basic of GCaptchaValidator consists of the creation of a CaptchaValidator
and calling a validation method that does the magic. This can be done in two lines of code.

```java
import com.github.playerforcehd.gcaptchavalidator.CaptchaValidationResponse;
import com.github.playerforcehd.gcaptchavalidator.CaptchaValidator;

class Scratch {
    public static void main(String[] args) {
        // Create a validator
        CaptchaValidator captchaValidator = CaptchaValidator.createDefault("YourSecret");

        // The CaptchaValidationResponse contains all the received data
        // You can use basicValidate to just get a boolean as return value
        // Also you can always supply the remoteIP parameter
        CaptchaValidationResponse valid = captchaValidator.validate("TheResponse");
        System.out.println(valid.hasSucceeded());
    }
}
```

##### Advanced usage
You can also create the validator manually by creating a new GCaptchaValidator. 
In this case you have many options for the constructor, and it is even possible to supply an own request handler
or deserializer to customize the processing done by GCaptchaValidator.
Take a look at the GCaptchaValidator class to get an overview of the available constructors.
```java
import com.github.playerforcehd.gcaptchavalidator.*;
import com.github.playerforcehd.gcaptchavalidator.request.SiteVerifyCaptchaRequestHandler;
import com.github.playerforcehd.gcaptchavalidator.serialize.SiteVerifyCaptchaResponseDeserializer;

class Scratch {
    public static void main(String[] args) {
        CaptchaValidatorConfiguration captchaValidatorConfiguration = new ValidatorConfiguration(
                "YourSecret"
        );
        // Create a validator
        CaptchaValidator captchaValidator = new GCaptchaValidator(
                new SiteVerifyCaptchaRequestHandler(),
                new SiteVerifyCaptchaResponseDeserializer(),
                captchaValidatorConfiguration
        );

        // The CaptchaValidationResponse contains all the received data
        // You can use basicValidate to just get a boolean as return value
        // Also you can always supply the remoteIP parameter
        CaptchaValidationResponse valid = captchaValidator.validate("TheResponse");
        System.out.println(valid.hasSucceeded());
    }
}
```

That's all what this library does.
It simply simplifies the validation of a Google ReCaptcha 2.0 request
in Java.

## Contributing

You are welcome to contribute to GCaptchaValidator if you want to provide improvements or bug fixes.
Refer to [Contributing](/CONTRIBUTING.md) for additional information about issues and code contributions.


