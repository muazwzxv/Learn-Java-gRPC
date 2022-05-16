package com.muazwzxv.protobuf;

import com.muazwzxv.models.Credentials;
import com.muazwzxv.models.EmailCredentials;
import com.muazwzxv.models.PhoneOTP;

public class OneOfDemo {

    public static void main(String[] args) {


        EmailCredentials email = EmailCredentials.newBuilder()
                .setEmail("muazwazir@gmail.com")
                .setPassword("root")
                .build();

        PhoneOTP otp = PhoneOTP.newBuilder()
                .setNumber(01122232)
                .setCode(123)
                .build();

        login(Credentials.newBuilder()
                .setEmailType(email)
                .build()
        );
        login(Credentials.newBuilder()
                .setOtpType(otp)
                .build()
        );
    }

    public static void login(Credentials credentials) {
//        if (credentials.hasEmailType())
//            System.out.println(credentials.getEmailType());
//
//        System.out.println(credentials.getOtpType());
        switch (credentials.getTypeCase()) {
            case EMAILTYPE:
                System.out.println(credentials.getEmailType());
                break;

            case OTPTYPE:
                System.out.println(credentials.getOtpType());
                break;
        }

    }
}
