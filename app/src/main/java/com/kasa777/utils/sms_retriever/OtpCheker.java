package com.kasa777.utils.sms_retriever;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpCheker {
    public static void main(String [] args){
        System.out.println(isValidUserName("abnsdTYS12@3425"));
    }

    public static boolean isValidOTP(String otp) {
        Pattern pattern ;
        Matcher matcher ;
        String emailPattern = "^[0-9]{4}$";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(otp);
        return matcher.matches();
    }

    public static boolean isValidUserName(String otp) {
        Pattern pattern ;
        Matcher matcher ;
        String emailPattern = "^[a-zA-Z][a-zA-Z0-9]{5,}$";
        pattern = Pattern.compile(emailPattern);
        matcher = pattern.matcher(otp);
        return matcher.matches();
    }
}
