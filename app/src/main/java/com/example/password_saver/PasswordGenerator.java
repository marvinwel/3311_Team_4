package com.example.password_saver;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Random;

public class PasswordGenerator {

    private static  final int MIN_CODE = 65, MAX_CODE = 90;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String process(int length,String character, boolean upperCase, boolean lowerCase, boolean numbers, boolean Characters, boolean specialCharacters)
    {


        if(specialCharacters && Characters)
        {
            specialCharacters = false;
        }

        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String specialChars = character;
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String characters = "!@#$%^&*()_-+=<>?/{}~|";
        String allowedChars = "";

        Random rn = new Random();
        StringBuilder builder = new StringBuilder(length);



        if(upperCase )
        {
            allowedChars += upperCaseChars;
            builder.append(upperCaseChars.charAt(rn.nextInt(upperCaseChars.length()-1)));
        }

        if(lowerCase  )
        {
            allowedChars += lowerCaseChars;
            builder.append(lowerCaseChars.charAt(rn.nextInt(lowerCaseChars.length()-1)));
        }

        if(numbers )
        {
            allowedChars += numberChars;
            builder.append(numberChars.charAt(rn.nextInt(numberChars.length()-1)));
        }
        if(Characters  )
        {
            allowedChars += characters;
            builder.append(characters.charAt(rn.nextInt(characters.length()-1)));
        }

        if(specialCharacters  )
        {
            allowedChars += specialChars;
            if(specialChars.length() < 2)
            {
                builder.append(specialChars.charAt(rn.nextInt(specialChars.length())));
            }
            else
                builder.append(specialChars.charAt(rn.nextInt(specialChars.length()-1)));
        }



        //fill the allowed length from different chars now.
        for(int i=builder.length(); i < length; ++i){
            builder.append(allowedChars.charAt(rn.nextInt(allowedChars.length())));
        }

        return builder.toString();
    }



}
