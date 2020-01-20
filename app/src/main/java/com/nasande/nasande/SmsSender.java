package com.nasande.nasande;

import android.util.Log;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID =
            "AC7a3ffedfcc648d07ab3870d6416d3763";
    public static final String AUTH_TOKEN =
            "your_auth_token";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber("+14159352345"), // to
                        new PhoneNumber("+14158141829"), // from
                        "Where's Wallace?")
                .create();

        Log.d("Message",message.getSid());
    }
}