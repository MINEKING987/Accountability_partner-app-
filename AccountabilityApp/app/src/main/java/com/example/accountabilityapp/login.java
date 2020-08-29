package com.example.accountabilityapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class login extends AppCompatActivity {
    Button buttonPhoneAuth;
    SharedPreferences prefs;
    private static final int RC_SIGN_IN = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
         prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

         if(prefs.getString("mobnum",null) != null ){
             Intent inta = new Intent(this,MainActivity.class);
             startActivity(inta);
             finish();
         }
         else{FirebaseApp.initializeApp(this);
        buttonPhoneAuth = findViewById(R.id.buttonPhoneAuth);
        // Set button listen
        buttonPhoneAuth.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                doPhoneLogin();
            }
        });}
    }
    private void doPhoneLogin() {
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                .setLogo(R.mipmap.ic_launcher)
                .build();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                showAlertDialog(user);
            } else {
                /**
                 *   Sign in failed. If response is null the user canceled the
                 *   sign-in flow using the back button. Otherwise check
                 *   response.getError().getErrorCode() and handle the error.
                 */
                Toast.makeText(getBaseContext(), "Phone Auth Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void showAlertDialog(FirebaseUser user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mobnum", user.getPhoneNumber());
        editor.commit();
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(
                login.this);
        // Set Title
        mAlertDialog.setTitle("Successfully Signed In");
        // Set Message
        mAlertDialog.setMessage(" Phone Number is " + user.getPhoneNumber());
        mAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent inta = new Intent(login.this,MainActivity.class);
                startActivity(inta);
                finish();
            }
        });
        mAlertDialog.create();
        // Showing Alert Message
        mAlertDialog.show();
    }
}
//    private void sendVerificationCode(String mobile) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+91" + mobile,
//                60,
//                TimeUnit.SECONDS,
//                this,
//                mCallbacks);
//    }