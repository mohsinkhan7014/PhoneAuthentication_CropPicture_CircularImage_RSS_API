package com.example.haqdarshak_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText otp;
    Button verify;
    String PhoneNumber;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String otpId;
    String mpass;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        otp=findViewById(R.id.edit_text);
        verify=findViewById(R.id.btn);
        PhoneNumber=getIntent().getStringExtra("mobile");
        mpass=getIntent().getStringExtra("Pass");
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child(PhoneNumber).push().setValue(mpass);





        otpInitiate();
        verify.setOnClickListener(v -> {
            if(otp.getText().toString().isEmpty())
            {
                Toast.makeText(Verify.this,"Error fill is empty",Toast.LENGTH_LONG).show();
            }
            else if(otp.getText().toString().length()!=6)
            {
                Toast.makeText(Verify.this,"Invalid OtP",Toast.LENGTH_LONG).show();
            }
            else
            {
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpId,otp.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Verify.this,MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Verify.this,"Sigin COde Error",Toast.LENGTH_LONG).show();
                    }
                });
    }

   private void  otpInitiate()
   {
       mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
           @Override
           public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
               otpId=s;
           }

           @Override
           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
               signInWithPhoneAuthCredential(phoneAuthCredential);
           }

           @Override
           public void onVerificationFailed(@NonNull FirebaseException e) {
               Toast.makeText(Verify.this,e.getMessage(),Toast.LENGTH_LONG).show();
           }
       };
       PhoneAuthOptions options =
               PhoneAuthOptions.newBuilder(mAuth)
                       .setPhoneNumber(String.valueOf(PhoneNumber))       // Phone number to verify
                       .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                       .setActivity(this)                 // Acti vity (for callback binding)
                       .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                       .build();
       PhoneAuthProvider.verifyPhoneNumber(options);

   }

}