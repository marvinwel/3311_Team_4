package com.example.password_saver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Store_Password extends AppCompatActivity {

    //create object
    EditText passtext,domain,usrname;
    ImageView imgView;
    Button addbttn;
    ImageButton bckbttn;
    ListView lstview;
    DatabaseReference mDatabse;
    String AES = "AES";
    String outputString;
    String CurrentUser;
    View overlay;
    FirebaseAuth Auth;



    //Arraylist
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store__password);



        //bind objects to widgets
        domain = findViewById(R.id.domain_name);
        usrname = findViewById(R.id.Text_username);
        passtext = findViewById(R.id.Password_text);
        imgView = (ImageView) findViewById(R.id.show_password_img);
        bckbttn = findViewById(R.id.storepsswrd_back_bttn);
        addbttn = findViewById(R.id.addToList);
        lstview = findViewById(R.id.listview);
        mDatabse = FirebaseDatabase.getInstance().getReference();
        imgView.setTag(1);

        //display list on page
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        lstview.setAdapter(adapter);

        //get generated password from home page
        Intent home_page = getIntent();
        String gen_pass = home_page.getStringExtra("Gen_PassWord");

        //display gen password on screen
        passtext.setText(gen_pass);


        CurrentUser = Auth.getInstance().getCurrentUser().getUid();



        //full screen
        overlay = findViewById(R.id.store_password);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //back button to home page
        bckbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Store_Password.this, home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



        //if add button is pressed
        addbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check to see if text fields are empty
                boolean result = CheckIfFieldsBlank();

                //if empty password will not be saved
                if (result)
                {

                }
                else
                {

                    //get password text to string
                    String encrypt_pass = passtext.getText().toString();

                    SecretKey secretKey = null;
                    try {
                        outputString = encrypt(encrypt_pass,usrname.getText().toString());

                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    //create string to be stored
                    String store = "Domain: " + domain.getText().toString() + " \n" + "Username: " + usrname.getText().toString() + "\n" + "password: " + outputString;

                    //store password on database
                    mDatabse.child("users").child(CurrentUser).push().setValue(store);

                    domain.setText("");
                    usrname.setText("");
                    passtext.setText("");

                }




            }
        });


        //dialog pops up each time listview is clicked on
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String decrypt_pass = null;
                String[] key = null;
                String[] temp;

                //takes all words as a string
                String word = adapterView.getItemAtPosition(position).toString();
                temp = word.split("Username: ");
                key = temp[1].split("\n");

                
                //split string to just get encrypted password password
                String[] wordarray = word.split("password: ");

                String tmp = key[0];

                try {
                    decrypt_pass = decrypt(wordarray[1],tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //show dialog box to show password
                AlertDialog.Builder alert = new AlertDialog.Builder(Store_Password.this);
                alert.setTitle("Info");




                alert.setMessage("Password: "+decrypt_pass);

                //alert.setMessage(tmp);
                //alert.setMessage(tmp.length());


                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.create().show();

                //Toast.makeText(Store_Password.this, position, Toast.LENGTH_SHORT).show();


            }
        });



        mDatabse.child("users").child(CurrentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                String string = snapshot.getValue(String.class);
                //String string = mDatabse.child("users").child(CurrentUser).getRef();
                arrayList.add(string);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

    //function to generate key to help encrypt and decrypt password
    private static SecretKeySpec generatekey(String password) throws Exception
    {

        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;

        

    }


    private String encrypt(String Data, String password)throws Exception
    {
        SecretKeySpec key = generatekey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = cipher.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;

    }

    private String decrypt(String outputString, String password)throws Exception
    {
        SecretKeySpec key = generatekey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(outputString,Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }


    public boolean CheckIfFieldsBlank()
    {
        boolean result = false;

        if(domain.getText().toString().isEmpty())
        {
            domain.setError("Please enter in a domain");
            result = true;
        }
        if(usrname.getText().toString().isEmpty())
        {
            usrname.setError("Please enter in a username");
            result = true;
        }
        if(passtext.getText().toString().isEmpty())
        {
            passtext.setError("Please enter in a password");
            result = true;
        }
        return result;
    }
    public void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}
