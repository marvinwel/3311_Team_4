package com.example.password_saver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class LogIn<choice> extends AppCompatActivity {



    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView textview, textview1;
    EditText email_in, password_in;
    Button login_bttn;
    View overlay;
    ImageView FingerPrint;
    private CheckBox checkBoxRemember;
    public SharedPreferences mPrefs;

    public SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "PrefsFile";
    private static final String SETTINGS = "settings";
    FingerprintManager fingerprintManager;
    KeyguardManager keyguardmanager;
    int choice = 5;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //firebase
        mAuth = FirebaseAuth.getInstance();

        //user name password
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mPrefs = getSharedPreferences(SETTINGS,MODE_PRIVATE);

        //connect objects to widgets
        login_bttn = findViewById(R.id.login_button);
        email_in = findViewById(R.id.usrname_in);
        password_in = findViewById(R.id.psswrd_in);
        textview = findViewById(R.id.ForgotPassword_link);
        textview1 = findViewById(R.id.signup_link);
        checkBoxRemember = findViewById(R.id.remember_box);
        FingerPrint = findViewById(R.id.fingerprint_icon);

        //get username from sign up page
        Intent signup_page = getIntent();
        String signup_usrname = signup_page.getStringExtra("USER_NAME");
        String signup_pass = signup_page.getStringExtra("PASSWORD");
        boolean value = signup_page.getBooleanExtra("value", false);


        //full screen
        overlay = findViewById(R.id.login_layout);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //display username and password on login page
        if (value)
        {
            editor = mPrefs.edit();

            mPrefs.edit().clear().apply();
            email_in.setText(signup_usrname);
            password_in.setText(signup_pass);

        } else
            getPreferencesData();




        //what happens when log in button is pressed
        login_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor = mPrefs.edit();
                mPrefs.edit().clear().apply();
                editor.putString("pref_name", email_in.getText().toString());
                editor.apply();
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
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LogIn.this, Forgot_Password.class);
                startActivity(intent);
            }
        };

        //click sign up to go to Register page
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LogIn.this, Register.class);
                startActivity(intent);
            }
        };

        //highlight letters to make clickable
        ss.setSpan(clickableSpan1, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(clickableSpan2, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(ss);
        textview1.setText(ss1);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        textview1.setMovementMethod(LinkMovementMethod.getInstance());











        //execute after fingerprint prompt is pressed
        Executor executor = Executors.newSingleThreadExecutor();

        final BiometricPrompt biometricPrompt = new BiometricPrompt(LogIn.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // user clicked negative button
                } else {
                    //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result)
            {
                super.onAuthenticationSucceeded(result);
                //TODO: Called when a biometric is recognized.

                //set password to password txt and sign in
                String email = email_in.getText().toString();
                password_in.setText(getpassword());
                mAuth.signInWithEmailAndPassword(email, getpassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                        }
                        else
                        {
                            Intent intent = new Intent(LogIn.this, home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            Toast.makeText(LogIn.this, "Logged in", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }
                });

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //TODO: Called when a biometric is valid but not recognized.
            }
        });







        //build biometric fingerprint prompt
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setDescription("Log in using biometric credential")
                .setNegativeButtonText("Cancel")
                .build();


        fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        keyguardmanager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);





        //if fingerprint is clicked
        findViewById(R.id.fingerprint_icon).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //if previous email and email in txt dont match no finger print
                // editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String email = mPrefs.getString("pref_name","");
                if(!checkBoxRemember.isChecked())
                {
                    editor = mPrefs.edit();
                    mPrefs.edit().clear().apply();
                    mPrefs.getBoolean("pref_check", false);
                }
                if(email_in.getText().toString().equalsIgnoreCase("") == true)
                {
                    email_in.setError("Please Enter Email");
                    email_in.requestFocus();
                    return;
                }
                if( email.equalsIgnoreCase(email_in.getText().toString())  == true)
                {
                    biometricPrompt.authenticate(promptInfo);

                }
                else
                    Toast.makeText(LogIn.this, "Please Sign in with password", Toast.LENGTH_SHORT).show();


            }
        });
    }







    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);



        if (sp.contains("pref_name")) {
            //SharedPreferences editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String email = mPrefs.getString("pref_name","");
            email_in.setText(email);


            //String u = sp.getString("pref_name", "not found");
            //email_in.setText(u.toString());
        }
        if (sp.contains("pref_check")) {
            Boolean b = mPrefs.getBoolean("pref_check", false);
            checkBoxRemember.setChecked(b);
        }
    }


    private void userLogin() {
        String email = email_in.getText().toString();
        String password = password_in.getText().toString();

        int num1 = CheckIfEmailisEmpty(email);
        int num2 = CheckIfPasswordIsEmpty(password);

        if (num1 + num2 == 2) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (!task.isSuccessful()) {
                        Toast.makeText(LogIn.this, "Login Error, Please check Password or Email", Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkBoxRemember.isChecked())
                        {
                            editor = mPrefs.edit();
                            Boolean boolIsChecked = checkBoxRemember.isChecked();
                            //SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("pref_name", email_in.getText().toString());
                            editor.putBoolean("pref_check", boolIsChecked);
                            editor.apply();
                        } else {
                            mPrefs.edit().clear().apply();
                        }

                        Intent intent = new Intent(LogIn.this, home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                        if (choice == 5) {
                            opendialog(intent);

                        }

                        if (choice == 3) {
                            Toast.makeText(LogIn.this, "Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
 /*                       if(choice == 1)
                        {
                            Toast.makeText(LogIn.this, "Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }*/


                    }

                }
            });
        }

    }


    public void opendialog(final Intent intent) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogIn.this);
        alertDialog.setTitle("Fingerprint Authentication")
                .setMessage("Would you like to enable Fingerprint Authentication?")
                .setNeutralButton("NO, AND DONT ASK AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice = 3;
                        Toast.makeText(LogIn.this, "Logged In", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LogIn.this, "Logged In and Fingerprint Disabled!", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Toast.makeText(LogIn.this, "Fingeerprint enabled", Toast.LENGTH_LONG).show();




                        storepassword();
                        startActivity(intent);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                    }
                });


        alertDialog.show();

    }


    private  void storepassword() {
        try {
            String passwordString = password_in.getText().toString();
            SecretKey secretKey = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                secretKey = createKey();
            }
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);


            byte[] encryptionIV = cipher.getIV();
            byte[] passwordBytes = passwordString.getBytes("UTF-8");
            byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
            String encryptedPassword = Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT);

            Utils.saveStringInSp(this, "password",encryptedPassword);
            Utils.saveStringInSp(this, "encryptionIV",Base64.encodeToString(encryptionIV,Base64.DEFAULT));




        } catch(NoSuchAlgorithmException |NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException  e)
        {
            throw new RuntimeException( e);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey createKey() throws InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder("Key", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(false)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            return keyGenerator.generateKey();
        } catch(NoSuchAlgorithmException |NoSuchProviderException |InvalidAlgorithmParameterException e)
        {
            throw new RuntimeException("Failed to create a key", e);
        }


    }






    public String getpassword()
    {
        try{
            String base64EncryptedPassword = Utils.getStringFromSp(this,"password");
            String base64EncryptionIv = Utils.getStringFromSp(this, "encryptionIV");

            byte[] encryptionIv = Base64.decode(base64EncryptionIv, Base64.DEFAULT);
            byte [] encryptedPassword = Base64.decode(base64EncryptedPassword, Base64.DEFAULT);

            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey("Key",null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptionIv));
            byte [] passwordBytes = cipher.doFinal(encryptedPassword);
            String password1 = new String(passwordBytes, "UTF-8");

            return password1;







        }catch(NoSuchAlgorithmException |NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException
                | InvalidAlgorithmParameterException | KeyStoreException | CertificateException | IOException | UnrecoverableKeyException e)
        {
            throw new RuntimeException( e);
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