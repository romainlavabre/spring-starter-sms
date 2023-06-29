package com.replace.replace.api.sms;

import com.replace.replace.api.environment.Environment;
import com.replace.replace.configuration.environment.Variable;
import com.replace.replace.exception.HttpInternalServerErrorException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service( "smsSender" )
public class SmsSenderImpl implements SmsSender {

    protected final Environment environment;
    protected final SmsSender   smsSenderTwilio;
    protected final SmsSender   smsSenderSmsMode;


    public SmsSenderImpl( Environment environment, SmsSender smsSenderTwilio, SmsSender smsSenderSmsMode ) {
        this.environment      = environment;
        this.smsSenderTwilio  = smsSenderTwilio;
        this.smsSenderSmsMode = smsSenderSmsMode;
    }


    @Override
    public Result send( String to, String message, String from, String name ) {
        String redirectSms = environment.getEnv( Variable.REDIRECT_SMS );

        if ( redirectSms != null && !redirectSms.isBlank() && !redirectSms.toUpperCase().equals( "NONE" ) ) {
            to = redirectSms;
        }

        switch ( getProvider() ) {
            case SmsSender.PROVIDER_TWILIO -> {
                return smsSenderTwilio.send( to, message, from, name );
            }
            case SmsSender.PROVIDER_SMS_MODE -> {
                return smsSenderSmsMode.send( to, message, from, name );
            }
        }

        throw new HttpInternalServerErrorException( "SMS_BAD_PROVIDER" );
    }


    @Override
    public Result[] send( List< String > to, String message, String from, String name ) {
        String redirectSms = environment.getEnv( Variable.REDIRECT_SMS );

        List< String > toRedirect;

        if ( redirectSms != null && !redirectSms.isBlank() && !redirectSms.toUpperCase().equals( "NONE" ) ) {
            toRedirect = new ArrayList<>();

            for ( int i = 0; i < to.size(); i++ ) {
                toRedirect.add( redirectSms );
            }
        } else {
            toRedirect = to;
        }

        switch ( getProvider() ) {
            case SmsSender.PROVIDER_TWILIO -> {
                return smsSenderTwilio.send( toRedirect, message, from, name );
            }
            case SmsSender.PROVIDER_SMS_MODE -> {
                return smsSenderSmsMode.send( toRedirect, message, from, name );
            }
        }

        throw new HttpInternalServerErrorException( "SMS_BAD_PROVIDER" );
    }


    protected String getProvider() {
        return environment.getEnv( Variable.SMS_PROVIDER ) == null
                || SmsSender.PROVIDER_TWILIO.equals( environment.getEnv( Variable.SMS_PROVIDER ) )
                ? SmsSender.PROVIDER_TWILIO
                : environment.getEnv( Variable.SMS_PROVIDER );
    }
}
