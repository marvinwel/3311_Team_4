package com.example.password_saver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    Button logout_bttn,view_bttn;
    TextView save_bttn;
    FirebaseAuth mAuth;
    TextView passwor_display, num_display;
    SeekBar seekBar;
    Button gen_bttn,copybttn;
    CheckBox check1, check2, check3, check4,check5, check6, check7, check8,check9, check10, check11, check12,check13, check14;
    View overlay;
    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //bind objects to widgets
        logout_bttn = findViewById(R.id.logout_button);
        passwor_display = findViewById(R.id.gen_pss_display);
        num_display = findViewById(R.id.num_display);
        seekBar = findViewById(R.id.seekBar);
        gen_bttn = findViewById(R.id.Gen_button);
        save_bttn = findViewById(R.id.save_password1);
        view_bttn = findViewById(R.id.view_bttn);
        copybttn = findViewById(R.id.cpybttn);
        check1 = findViewById(R.id.checkBox1);
        check2 = findViewById(R.id.checkBox2);
        check3 = findViewById(R.id.checkBox3);
        check4 = findViewById(R.id.checkBox4);
        check5 = findViewById(R.id.checkBox5);
        check6 = findViewById(R.id.checkBox6);
        check7 = findViewById(R.id.checkBox7);
        check8 = findViewById(R.id.checkBox8);
        check9 = findViewById(R.id.checkBox9);
        check10 = findViewById(R.id.checkBox10);
        check11 = findViewById(R.id.checkBox11);
        check12 = findViewById(R.id.checkBox12);
        check13 = findViewById(R.id.checkBox13);
        check14 = findViewById(R.id.checkBox14);

        mAuth = FirebaseAuth.getInstance();


        //clipboard sevice
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);


        //full screen
        overlay = findViewById(R.id.home_page);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //logout button
        logout_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //UserLogout();
                showPopup(view);

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

        //when save button is pressed
        save_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(passwor_display.getText().toString() == "")
                {
                    Toast.makeText(home.this, "Please generate a password before saving", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(home.this , Store_Password.class);
                    intent.putExtra("Gen_PassWord",passwor_display.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }


            }
        });


        //button clicked goes to store_Password page
        view_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, Store_Password.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        //when copy bttn is pressed
        copybttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = passwor_display.getText().toString();

                if(!text.equals(""))
                {
                    ClipData clipData = ClipData.newPlainText("text",text);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(home.this, "Copied", Toast.LENGTH_SHORT).show();

                }

            }
        });


        //when generate button is pressed
        gen_bttn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                String character, UpperCase, lowercase, NumCharacters, AllSymbols ;


                int length;

                boolean chck_bttn1 = Check_bttn1();
                boolean chck_bttn2 = Check_bttn2();
                boolean chck_bttn3 = Check_bttn3();
                boolean chck_bttn4 = Check_bttn4();
                boolean specialChar = Check_bttn();



                length = CheckSeekBarLength();


                //max characters
                if(length == 999)
                {
                    //if seek bar is 0 display error message
                    passwor_display.setText("");
                    Toast.makeText(home.this, "Max Characters is 40", Toast.LENGTH_SHORT).show();
                }
                //error handling for seek bar
                else if(!check1.isChecked() && !check2.isChecked() && !check3.isChecked() && !check4.isChecked() && !check5.isChecked() && !check6.isChecked() && !check7.isChecked() && !check8.isChecked() && !check9.isChecked()
                        && !check10.isChecked() && !check11.isChecked() && !check12.isChecked() && !check13.isChecked() && !check14.isChecked())
                {
                    //if nothing is checked display error message
                    passwor_display.setText("");
                    Toast.makeText(home.this, "Please check an option ", Toast.LENGTH_SHORT).show();

                }
                else if(length < 1)
                {
                    //if seek bar is 0 display error message
                    passwor_display.setText("");
                    Toast.makeText(home.this, "Character length should be greater than 0 ", Toast.LENGTH_SHORT).show();
                }

                else if(length > 0 && (!check1.isChecked() && !check2.isChecked() && !check3.isChecked() && !check4.isChecked() && !check5.isChecked() && !check6.isChecked() && !check7.isChecked() && !check8.isChecked() && !check9.isChecked()
                        && !check10.isChecked() && !check11.isChecked() && !check12.isChecked() && !check13.isChecked() && !check14.isChecked()) )
                {
                    //if nothing is checked display error message
                    passwor_display.setText("");
                    Toast.makeText(home.this, "Please check an option ", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    character = CheckCharacter(); //checks to see which unique character is picked

                    String password = PasswordGenerator.process(length,character, chck_bttn1, chck_bttn2, chck_bttn3, chck_bttn4, specialChar);
                    passwor_display.setText(password);
                }




            }
        });

    }

    String CheckCharacter()
    {
        StringBuffer result = new StringBuffer();
        String characters;

        if(check5.isChecked())
        {
            result.append(check5.getText().toString()+"!!!");
        }
        if(check6.isChecked())
        {
            result.append(check6.getText().toString()+"@@@");
        }
        if(check7.isChecked())
        {
            result.append(check7.getText().toString()+"###");
        }
        if(check8.isChecked())
        {
            result.append(check8.getText().toString()+"$$$");
        }
        if(check9.isChecked())
        {
            result.append(check9.getText().toString()+"%%%");
        }
        if(check10.isChecked())
        {
            result.append(check10.getText().toString()+"&&&");
        }
        if(check11.isChecked())
        {
            result.append(check11.getText().toString()+"***");
        }
        if(check12.isChecked())
        {
            result.append(check12.getText().toString()+"___");
        }
        if(check13.isChecked())
        {
            result.append(check13.getText().toString()+"---");
        }
        if(check14.isChecked())
        {
            result.append(check14.getText().toString()+"~~~");
        }

        characters = result.toString();

        return characters;
    }

    String CheckUppercase()
    {

        String characters;

        if(check1.isChecked())
        {
            characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;
        }
        else
            characters = "";



        return characters;
    }


    String Checklowercase()
    {
        String characters;

        if(check2.isChecked())
        {
            characters = "abcdefghijklmnopqrstuvwxyz" ;
        }
        else
            characters = "";


        return characters;
    }

    String CheckNumCharacters()
    {
        String characters;

        if(check3.isChecked())
        {
            characters = "0123456789" ;
        }
        else
            characters = "";


        return characters;
    }

    String CheckAllSymbols()
    {
        String characters;

        if(check4.isChecked())
        {
            characters = "!@#$%^&*()_-+=<>?/{}~|" ;
        }
        else
            characters = "";


        return characters;
    }

    boolean Check_bttn1()
    {
        boolean chck_bttn = false;

        if(check1.isChecked())
        {
            chck_bttn = true;
        }

        return chck_bttn;
    }

    boolean Check_bttn2()
    {
        boolean chck_bttn = false;

        if(check2.isChecked())
        {
            chck_bttn = true;
        }

        return chck_bttn;
    }
    boolean Check_bttn3()
    {
        boolean chck_bttn = false;

        if(check3.isChecked())
        {
            chck_bttn = true;
        }

        return chck_bttn;
    }
    boolean Check_bttn4()
    {
        boolean chck_bttn = false;

        if(check4.isChecked())
        {
            chck_bttn = true;
        }

        return chck_bttn;
    }
    boolean Check_bttn()
    {
        boolean chck_bttn = false;

        if(check5.isChecked() || check6.isChecked() || check7.isChecked() || check8.isChecked()
        || check9.isChecked() || check10.isChecked() || check11.isChecked() || check12.isChecked()
        || check13.isChecked() || check14.isChecked())
        {
            chck_bttn = true;
        }

        return chck_bttn;
    }


    int CheckSeekBarLength()
    {
        int length = 0;


        if(seekBar.getProgress() < 1)
        {
            length = 0;
        }
        if(seekBar.getProgress() > 40)
        {
            length = 999;
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

    //logout of app
     void UserLogout ()
    {
        mAuth.signOut();
        Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),LogIn.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void showPopup(View v)
    {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_logut);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.item1:
                UserLogout();
                return true;
            default:
                return false;
        }
    }
}