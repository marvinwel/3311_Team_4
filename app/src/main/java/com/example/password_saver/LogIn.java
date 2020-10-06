package com.example.password_saver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView textview, textview1;
    EditText email_in, password_in;
    Button login_bttn;
    View overlay;
    private CheckBox checkBoxRemember;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //connect objects to widgets
        login_bttn = findViewById(R.id.login_button);
        email_in = findViewById(R.id.usrname_in);
        password_in = findViewById(R.id.psswrd_in);
        textview = findViewById(R.id.ForgotPassword_link);
        textview1 = findViewById(R.id.signup_link);
        checkBoxRemember = findViewById(R.id.remember_box);

        //get username from sign up page
        Intent signup_page = getIntent();
        String signup_usrname = signup_page.getStringExtra("USER_NAME");
        String signup_pass = signup_page.getStringExtra("PASSWORD");
        boolean value = signup_page.getBooleanExtra("value",false);


        //full screen
        overlay = findViewById(R.id.login_layout);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //display username and password on login page
        if(value == true)
        {
            mPrefs.edit().clear().apply();
            email_in.setText(signup_usrname);
            password_in.setText(signup_pass);

        }
        else
            getPreferencesData();



        //what happens when log in button is pressed
        login_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });


        //set link for forgot password and signup
        String text = "forgot Password?";
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




    private void getPreferencesData()
    {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        if(sp.contains("pref_name"))
        {
            String u = sp.getString("pref_name","not found");
            email_in.setText(u.toString());
        }
        if(sp.contains("pref_check"))
        {
            Boolean b = sp.getBoolean("pref_check",false);
            checkBoxRemember.setChecked(b);
        }
    }


    private void userLogin()
    {
        String email = email_in.getText().toString();
        String password = password_in.getText().toString();

        int num1 = CheckIfEmailisEmpty(email);
        int num2 = CheckIfPasswordIsEmpty(password);

        if(num1+num2 == 2)
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {


                    if(!task.isSuccessful())
                    {
                        Toast.makeText(LogIn.this, "Login Error, Please check Password or Email", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(checkBoxRemember.isChecked())
                        {
                            Boolean boolIsChecked = checkBoxRemember.isChecked();
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("pref_name",email_in.getText().toString());
                            editor.putBoolean("pref_check",boolIsChecked);
                            editor.apply();
                        }
                        else
                        {
                            mPrefs.edit().clear().apply();
                        }
                        Toast.makeText(LogIn.this, "Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this , home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                    }

                }
            });
        }

    }

     int CheckIfEmailisEmpty(String email)
    {
        if(email.isEmpty() )
        {
            email_in.setError("Please Enter Email");
            email_in.requestFocus();
        }
        else
        {
            return 1;
        }
        return 0;
    }

     int CheckIfPasswordIsEmpty(String password)
    {
        if(password.isEmpty() )
        {
            password_in.setError("Please Enter Password");
            password_in.requestFocus();
        }
        else
        {
            return 1;
        }
        return 0;
    }

    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}