package com.example.haqdarshak_assignment;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    private Uri picUri;

    CircleImageView circleImageView;

    EditText UserName,Sex,Age,NewEmail,Mobile,Pass;
    Button submit;
    CountryCodePicker ccp;
    DatabaseReference myref;
    int flag=0,temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        circleImageView=findViewById(R.id.CircularImageView);
        UserName=findViewById(R.id.Name);
        Sex=findViewById(R.id.Sex);
        Age=findViewById(R.id.Age);
        NewEmail=findViewById(R.id.Email);
        Mobile=findViewById(R.id.MobileNumber);
        Pass=findViewById(R.id.Password);

        ccp=findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(Mobile);


        myref= FirebaseDatabase.getInstance().getReference();


        circleImageView.setOnClickListener(v -> {
            if (v.getId() == R.id.CircularImageView) {
                try {
                    // use standard intent to capture an image
                    Intent captureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    // we will handle the returned data in onActivityResult
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (ActivityNotFoundException m) {
                    Toast.makeText(Signup.this, "This device doesn't support the crop action!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

        submit=findViewById(R.id.Submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Uname=UserName.getText().toString();
                String sex=Sex.getText().toString();
                String age=Age.getText().toString();
                String Uemail=NewEmail.getText().toString();
                String Umobile=Mobile.getText().toString();
                String Upass=Pass.getText().toString();
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable=dataSnapshot.getChildren();

                        for(DataSnapshot ds: iterable) {
                            flag=0;
                            if(ds.getKey().toString().equals(Umobile))
                            {
                                flag=1;
                                break;
                            }
                            //   break;
                            //  qwe=qwe+ds.getKey();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                 if(flag==0)
                 {

                     Mobile.setError("Number is Already used");


                 }
                if(TextUtils.isEmpty(Uname)||TextUtils.isEmpty(sex)||TextUtils.isEmpty(age)||TextUtils.isEmpty(Uemail)||TextUtils.isEmpty(Umobile)||TextUtils.isEmpty(Upass))
                {
                    Toast.makeText(Signup.this, "Please Enter ALL the Field", Toast.LENGTH_LONG).show();
                    temp=1;

                }
//                if(Integer.parseInt(age)<18)
//                {
//                    Toast.makeText(Signup.this, "Age Must be 18+", Toast.LENGTH_LONG).show();
//                    finish();
//                }
                if(validateEmail(Uemail)==false)
                {
                    Toast.makeText(Signup.this, "Enter a Valid Email formet", Toast.LENGTH_LONG).show();
                    NewEmail.equals("formet Error");
                    temp=1;


                }
                if(isValidPassword(Upass)==false)
                {
                    Toast.makeText(Signup.this, "Please Enter valid passs", Toast.LENGTH_LONG).show();
                    Pass.equals("pass Mohsin@2019");
                    temp=1;

                }
              if(temp==0) {
                  Intent intent = new Intent(Signup.this, Verify.class);
                  intent.putExtra("mobile", ccp.getFullNumberWithPlus().replace(" ", ""));
                  intent.putExtra("Pass", Upass);

                  startActivity(intent);
              }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image

                picUri = data.getData();
                performCrop();
            }
            // user is returning from cropping the image
            else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                CircleImageView picView = (CircleImageView) findViewById(R.id.CircularImageView);
                picView.setImageBitmap(thePic);
            }
        }
    }



    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 3);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException m) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean validateEmail(String data){
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(data);
        return emailMatcher.matches();
    }
    public static boolean isValidPassword(String pass) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");


        return PASSWORD_PATTERN.matcher(pass).matches();

    }

//    public static boolean isvalidmobile(String mobile1)
//    {
//        if(mobile1.length()>10)
//        {
//            return false;
//        }
//        else
//        {
//            Pattern mobilepattern=Pattern.compile("[0-9]{8,20}");
//            return mobilepattern.matcher(mobile1).matches() ;
//        }
//
//    }
}
