package com.example.password_saver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText email_txt, email_con,password_txt,password_con;
    Button sign_up;
    ImageButton back_bttn;
    View overlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sign_up = findViewById(R.id.signup_button);
        email_txt = findViewById(R.id.email_signup);
        email_con = findViewById(R.id.email_confirm);
        password_txt = findViewById(R.id.password_signup);
        password_con = findViewById(R.id.password_confirm);
        back_bttn = findViewById(R.id.signup_back_bttn);
        overlay = findViewById(R.id.signup_layout);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);


        //sign up button is pressed
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_txt.getText().toString();
                String email_confirm = email_con.getText().toString();
                String password = password_txt.getText().toString();
                String password_confirm = password_con.getText().toString();

                // checks to see if text fields are left empty
                //if no errors with password and email func return 1
                int num = isTxtBlank(email,email_confirm,password,password_confirm);
                int num1 = isEmailTheSame(email,email_confirm);
                int num2 = isPasswordTheSame(password,password_confirm);
                int num3 = checklengthOfPassword(password);
                boolean validatedemail = validateEmail(email);


                if((num+num1+num2+num3 == 4) && validatedemail)
                {
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(Register.this, "SignUp Unsuccessful, Please try again.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Register.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, LogIn.class);
                                intent.putExtra("USER_NAME",email_con.getText().toString());
                                intent.putExtra("PASSWORD",password_con.getText().toString());
                                intent.putExtra("value",true);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                            }
                        }
                    });
                }

            }
        });

        //back button to login page
        back_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, LogIn.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }
















    int isTxtBlank(String email, String email_confirm,String password, String password_confirm)
    {
        if(email.isEmpty() && email_confirm.isEmpty())
        {
            email_txt.setError("Please Enter Email");
            email_con.setError("Please confirm Email");
            email_txt.requestFocus();
            email_con.requestFocus();

        }
        if(email.isEmpty())
        {
            email_txt.setError("Please Enter Email");
            email_txt.requestFocus();

        }
        if(email_confirm.isEmpty())
        {
            email_con.setError("Please confirm Email");
            email_con.requestFocus();

        }
        if(password.isEmpty() && password_confirm.isEmpty())
        {
            password_txt.setError("Please Enter Password");
            password_con.setError("Please confirm Password");
            password_txt.requestFocus();
            password_con.requestFocus();

        }
        if(password.isEmpty() )
        {
            password_txt.setError("Please Enter Password");
            password_txt.requestFocus();

        }
        if(password_confirm.isEmpty())
        {
            password_con.setError("Please confirm Password");
            password_con.requestFocus();

        }
        else
        {
            return 1;
        }


        return 0;
    }



    int isEmailTheSame(String email, String email_confirm)
    {
        int l1 = email.length();
        int l2 = email_confirm.length();


        if (l1 < l2 || l1 > l2 ) {
            email_con.setError("Email does not math");
            email_con.requestFocus();
        }
        if(email.equalsIgnoreCase(email_confirm)== false)
        {
            email_con.setError("Email does not math");
            email_con.requestFocus();
        }
        else
        {
            return 1;
        }

        return 0;


    }


    int isPasswordTheSame(String password,String password_confirm)
    {
        int l1 = password.length();
        int l2 = password_confirm.length();


        if (l1 < l2 || l1 > l2 ) {
            password_con.setError("Password does not match");
            password_con.requestFocus();
        }
        if(password.compareTo(password_confirm) != 0)
        {
            password_con.setError("Password does not match");
            password_con.requestFocus();
        }
        else
        {
            return 1;
        }

        return 0;
    }

    int checklengthOfPassword(String password)
    {
        int l1 = password.length();

        if(l1 < 8)
        {
            password_txt.setError("Password needs to be 8 or more characters");
            password_txt.requestFocus();
        }
        else
        {
            return 1;
        }
        return 0;
    }

    boolean  validateEmail(String email)
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_txt.setError("Please enter a valid email address");
            return false;

        }

       return true;
    }

    public static final Pattern EMAIL_ADDRESS = Pattern.compile("[a-zA-A0-9]\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA_Z0-9][a-zA-Z0-9\\-]{0,25}]" + ")+");

    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}