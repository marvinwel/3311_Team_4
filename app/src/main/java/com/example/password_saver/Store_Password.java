package com.example.password_saver;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Store_Password extends AppCompatActivity {

    EditText passtext;
    ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store__password);

        //bind objects to widgets
        passtext = findViewById(R.id.Password_text);
        imgView = (ImageView) findViewById(R.id.show_password_img);
        imgView.setTag(1);

        //get generated password from home page
        Intent home_page = getIntent();
        String gen_pass = home_page.getStringExtra("Gen_PassWord");

        //display gen password on screen
        passtext.setText(gen_pass);


        //shows and hide password when click on eye image
        imgView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if(imgView.getTag().equals(1))
                {
                    imgView.setImageResource(R.drawable.hide_password);
                    passtext.setTransformationMethod(null);
                    imgView.setTag(2);
                }else{
                    imgView.setImageResource(R.drawable.show_password);
                    passtext.setTransformationMethod(new PasswordTransformationMethod());
                    imgView.setTag(1);
                }
            }
        });


    }


}
