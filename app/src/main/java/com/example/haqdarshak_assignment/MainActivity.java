package com.example.haqdarshak_assignment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    DatabaseReference myref;
    EditText mobile,pass;
    Button btn;
    ArrayList<String> list = new ArrayList<String>();
    String mk="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mobile=findViewById(R.id.Rmobile);
        pass=findViewById(R.id.Rpass);
        btn=findViewById(R.id.login);
        textView=findViewById(R.id.txtsign);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Signup.class));
            }
        });

        myref= FirebaseDatabase.getInstance().getReference();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.child("+91"+mobile.getText()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable=dataSnapshot.getChildren();

                        for(DataSnapshot ds: iterable) {
                            mk = ds.getValue().toString();
                        }
                        Toast.makeText(MainActivity.this,""+"+91"+mobile.getText().toString()+mk,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                if (mk.equals(""+pass.getText()))
                {
                    startActivity(new Intent(MainActivity.this,Homepage.class));
                }
            }
        });
    }
}