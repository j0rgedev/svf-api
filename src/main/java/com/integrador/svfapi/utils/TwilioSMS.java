package com.integrador.svfapi.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioSMS {
    @Value("${twilio.account-sid}")
    private String account_sid;

    @Value("${twilio.auth-token}")
    private  String auth_token;

    @Value("${twilio.phone-number}")
    private  String phone_number;

    public void sendMessage(String number, String token) {
        Twilio.init(account_sid, auth_token);
        Message message = Message.creator(
                new PhoneNumber("+51"+number),
                new PhoneNumber(phone_number),
                "SVF: Tu código de verificación es: " + token)
                .create();
    }
}
