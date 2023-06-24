# SMS

The mail library permit to send sms, (the implementation is Twilio).

Service:

```java
com.replace.replace.api.sms.SmsSender;
```

All documentation is available on interface.

### Installation

You need to add this variables

```java
public interface Variable {
    String SMS_TWILIO_SID         = "if your provider is twilio";
    String SMS_TWILIO_PRIVATE_KEY = "if your provider is twilio";
    String SMS_SMS_MODE_API_KEY   = "if your provider is sms mode";
    String SMS_PROVIDER           = "TWILIO|SMS_MODE";
}
```

### Requirements

Maven dependency

```xml

<dependencies>
    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
        <version>8.9.0</version>
    </dependency>
</dependencies>
```