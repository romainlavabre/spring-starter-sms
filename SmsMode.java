package com.replace.replace.api.sms;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.api.rest.RequestBuilder;
import com.replace.replace.api.rest.Response;
import com.replace.replace.api.rest.Rest;
import com.replace.replace.configuration.environment.Variable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service( "smsSenderSmsMode" )
public class SmsMode implements SmsSender {
    protected final Environment environment;


    public SmsMode( Environment environment ) {
        this.environment = environment;
    }


    @Override
    public Result send( String to, String message, String from, String name ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.POST, "https://rest.smsmode.com/sms/v1/messages" )
                        .withXApiKey( environment.getEnv( Variable.SMS_SMS_MODE_API_KEY ) )
                        .addHeader( "Accept", "application/json" )
                        .jsonBody( Map.of(
                                "recipient", Map.of(
                                        "to", to
                                ),
                                "body", Map.of(
                                        "text", message
                                ),
                                "from", name == null ? "" : name
                        ) )
                        .buildAndSend();


        return response.isSuccess()
                ? new Result( SmsSender.PROVIDER_SMS_MODE, response.getBodyAsMap().get( "messageId" ).toString(), true )
                : new Result( SmsSender.PROVIDER_SMS_MODE, null, false );
    }


    @Override
    public Result[] send( List< String > to, String message, String from, String name ) {
        final Result[] results = new Result[ to.size() ];

        int i = 0;

        for ( final String recipient : to ) {
            results[ i ] = send( recipient, message, from, name );

            i++;
        }

        return results;
    }
}
