package com.example.otpauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phonenumermain extends AppCompatActivity  {

    EditText edtphonenumber;
    Button btnEnviarMensaje;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumermain);

        edtphonenumber= findViewById(R.id.edttelefono);
        btnEnviarMensaje=findViewById(R.id.btnenviarmensaje);
        auth=FirebaseAuth.getInstance();

        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNO = edtphonenumber.getText().toString();
                String phoneNumber="+51"+phoneNO;

                if(!phoneNO.isEmpty()){
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(phonenumermain.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
                //fin del if
            }
        });

        //fin click listener
        mCallBacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(phonenumermain.this, "Error verificacion fallo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                //Algunas veces el codigo no es detectado automaticamente
                //entonces usuarios tienen que ingresar automaticamente

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(phonenumermain.this,MainActivity.class);
                        i.putExtra("auth", s);
                        startActivity(i);

                    }
                },10000);
                //handler es para que aparesca un ventana de carga

                //Intent i = new Intent(phonenumermain.this,MainActivity.class);
               // i.putExtra("auth", s);
                //startActivity(i);


            }
        };

        //fin metodo mcallbacks

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent i = new Intent(this,  MainActivity.class);
        startActivity(i);
        finish();
    }

    private void signIn(PhoneAuthCredential credential){

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    sendToMain();
                }else {
                    Toast.makeText(phonenumermain.this, "Error en metodo signin task", Toast.LENGTH_SHORT).show();
                }
                //fin if else
            }
        });


    }




}