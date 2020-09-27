package com.example.password_saver;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ThreadLocalRandom;

public class PasswordGenerator {

    private static  final int MIN_CODE = 33, MAX_CODE = 126;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String process(int length)
    {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length; i++)
        {
            builder.append((char) ThreadLocalRandom.current().nextInt(MIN_CODE,MAX_CODE+1));
        }

        return builder.toString();
    }
}
