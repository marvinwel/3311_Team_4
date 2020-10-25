package com.example.password_saver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    ImageButton back_bttn;
    Button request_bttn;
    View overlay;
    EditText email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        email = findViewById(R.id.request_email);
        request_bttn = findViewById(R.id.request_button);
        firebaseAuth = FirebaseAuth.getInstance();

        //fullscreen
        overlay = findViewById(R.id.forgotpsswrd_layout);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        back_bttn = findViewById(R.id.forgotpsswrd_back_bttn2);

        back_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Forgot_Password.this, LogIn.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        //when request button is pressed
        request_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean decision = ValidateEmail();

                if (decision)
                {
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Forgot_Password.this, "Password Sent", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Forgot_Password.this,LogIn.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(Forgot_Password.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
            }
        });

    }
    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public boolean ValidateEmail()
    {
        String emailinput = email.getText().toString();

        if(emailinput.isEmpty())
        {
            email.setError("Please Enter a valid Email address");
            email.requestFocus();
            return false;
        }
        if(!emailinput.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(emailinput).matches())
        {
            email.setError("Please Enter a valid Email address");
            email.requestFocus();
            return false;
        }
        return true;
    }
    public void checkIffieldIsEmpty()
    {
        String emailinput = email.getText().toString();

        if(emailinput.isEmpty())
        {
            email.setError("Please Enter a valid Email address");
            email.requestFocus();
        }
    }
}