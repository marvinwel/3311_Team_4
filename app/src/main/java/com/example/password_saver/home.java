package com.example.password_saver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    Button logout_bttn;
    FirebaseAuth mAuth;
    TextView passwor_display, num_display;
    SeekBar seekBar;
    Button gen_bttn;
    CheckBox check1, check2, check3, check4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout_bttn = findViewById(R.id.logout_button);
        passwor_display = findViewById(R.id.gen_pss_display);
        num_display = findViewById(R.id.num_display);
        seekBar = findViewById(R.id.seekBar);
        gen_bttn = findViewById(R.id.Gen_button);
        check1 = findViewById(R.id.checkBox1);
        check2 = findViewById(R.id.checkBox2);
        check3 = findViewById(R.id.checkBox3);
        check4 = findViewById(R.id.checkBox4);

        mAuth = FirebaseAuth.getInstance();



        //logout button
        logout_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UserLogout();

            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                num_display.setText("Length : " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        gen_bttn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                int length;

                boolean chck_bttn1 = false;
                boolean chck_bttn2 = false;
                boolean chck_bttn3 = false;
                boolean chck_bttn4 = false;

                if(check1.isChecked())
                {
                    chck_bttn1 = true;
                }
                if(check2.isChecked())
                {
                    chck_bttn2 = true;
                }
                if(check3.isChecked())
                {
                    chck_bttn3 = true;
                }
                if(check4.isChecked())
                {
                    chck_bttn4 = true;
                }
                


                length = CheckSeekBarLength();


                String password = PasswordGenerator.process(length,chck_bttn1,chck_bttn2,chck_bttn3,chck_bttn4);
                passwor_display.setText(password);

            }
        });

    }

    int CheckSeekBarLength()
    {
        int length = 0;


        if(seekBar.getProgress() < 1)
        {
            length = 0;
        }
        else
            length = seekBar.getProgress();

        return length;
    }

    //thi function prevent user from going back to login in screen from home
    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "Cant go back", Toast.LENGTH_SHORT).show();
    }
     void UserLogout ()
    {
        mAuth.signOut();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LogIn.class));
        finish();
    }


}