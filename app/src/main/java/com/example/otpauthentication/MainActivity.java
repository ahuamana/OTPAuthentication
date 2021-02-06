package com.example.otpauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumberEntered;
    Button verify_btn;
    private String OTP;  /*son lo mismo  */ String verificationCodeBySystem;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();

        verify_btn= findViewById(R.id.verify_btn);
        phoneNumberEntered=findViewById(R.id.verification_code);
        OTP = getIntent().getStringExtra("auth");

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code = phoneNumberEntered.getText().toString();
                if(!verification_code.isEmpty()){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP,verification_code);
                    signIn(credential);
                }else {
                    Toast.makeText(MainActivity.this, "Porfavor ingrese OTP", Toast.LENGTH_SHORT).show();
                }
                //fin if

            }
        });
        //fin de la accion del boton


    }

    private void signIn(PhoneAuthCredential credential){

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    sendToMain();
                }else {
                    Toast.makeText(MainActivity.this, "Verificacion fallo", Toast.LENGTH_SHORT).show();
                }
                //fin if else
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null){
            sendToMain();
        }
    }

    private void sendToMain() {
        startActivity(new Intent(MainActivity.this,menuprincipal.class));
        finish();
    }


}