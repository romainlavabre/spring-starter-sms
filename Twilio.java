package com.replace.replace.api.sms;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.configuration.environment.Variable;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Twilio implements SmsSender {

    protected Environment environment;


    public Twilio( final Environment environment ) {
        this.environment = environment;
    }


    @Override
    public boolean send( final String to, final String message, String from, String name ) {

        return this.core( to, message, from, name );
    }


    @Override
    public Boolean[] send( final List< String > to, final String message, String from, String name ) {
        final Boolean[] results = new Boolean[ to.size() ];

        int i = 0;

        for ( final String recipient : to ) {
            results[ i ] = this.core( recipient, message, from, name );

            i++;
        }

        return results;
    }


    protected boolean core( String to, final String message, String from, String name ) {
        assert to != null && !to.isBlank() : "variable to should not be null or blank";
        assert name != null || from != null : "variable name or from should not be null";

        com.twilio.Twilio.init(
                this.environment.getEnv( Variable.SMS_TWILIO_SID ),
                this.environment.getEnv( Variable.SMS_PRIVATE_KEY )
        );

        String redirectSms = environment.getEnv( Variable.REDIRECT_SMS );

        if ( redirectSms != null && !redirectSms.isBlank() && !redirectSms.toUpperCase().equals( "NONE" ) ) {
            to = redirectSms;
        }

        Message
                .creator(
                        new PhoneNumber( to ),
                        new PhoneNumber( name == null ? from : name ),
                        message
                )
                .create();

        return true;
    }
}
