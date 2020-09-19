package com.example.password_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {


    private FirebaseAuth mAuth;
    TextView textview, textview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //set link for forgot password and signup
        textview = findViewById(R.id.ForgotPassword_link);
        textview1 = findViewById(R.id.signup_link);
        String text = "forgot Passowrd?";
        String text1 = "Don't have an account? Sign up";

        Spannable ss = new SpannableString(text);
        Spannable ss1 = new SpannableString(text1);

        //click forgot password? to go to forgot_Password page
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view)
            {
                Intent intent = new Intent(LogIn.this , Forgot_Password.class);
                startActivity(intent);
            }};



        //click sign up to go to Register page
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view)
            {
                Intent intent = new Intent(LogIn.this , Register.class);
                startActivity(intent);
            }};

        //highlight letters to make clickable
        ss.setSpan(clickableSpan1,0,16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(clickableSpan2,23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(ss);
        textview1.setText(ss1);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        textview1.setMovementMethod(LinkMovementMethod.getInstance());

    }



}