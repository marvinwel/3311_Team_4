package com.example.password_saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
//import butterknife.Butterknife;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    Button logout_bttn;
    FirebaseAuth mAuth;
    TextView passwor_display, num_display;
    SeekBar seekBar;
    Button gen_bttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout_bttn = findViewById(R.id.logout_button);
        passwor_display = findViewById(R.id.gen_pss_display);
        num_display = findViewById(R.id.num_display);
        seekBar = findViewById(R.id.seekBar);
        gen_bttn = findViewById(R.id.Gen_button);

        mAuth = FirebaseAuth.getInstance();
        //ButterKnife.bind(this);

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
            @Override
            public void onClick(View view) {
                String password = PasswordGenerator.process(seekBar.getProgress());
                passwor_display.setText(password);
            }
        });

    }
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