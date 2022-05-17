package com.example.demo.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumber {

    public static String makeString(Date date) {
        String theAlphaNumericS;
        StringBuilder builder;
        String signal = "B";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        String nowDate = simpleDateFormat.format(date);
        String result;

        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        //create the StringBuffer
        builder = new StringBuilder(4);

        for (int m = 0; m < 4; m++) {

            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * Math.random());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        result = signal+nowDate+builder.toString();
        return result;
    }
}
