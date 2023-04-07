package com.example.biogas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://biogas-c4c2b-default-rtdb.firebaseio.com/");
    ImageView google_img;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    RadioGroup radioGroup;
    private Button button4;
    private ImageView Gallery;
    private ImageView image;
    private EditText shopname;
    private EditText shopownername;
    private EditText mobile;
    private EditText address1;
    private EditText address2;
    private Button nextpgbtn;
    private RadioButton hot;
    private RadioButton bio;
    private static final int IMAGE_PICK_CODE=1000;
    private static final int PERMISSION_CODE=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        button4 = findViewById(R.id.button4);
        Gallery = findViewById(R.id.imageView);
        image=findViewById(R.id.profile_image);
        radioGroup=findViewById(R.id.radioGroup);
        shopname=findViewById(R.id.editTextTextPersonName3);
        shopownername=findViewById(R.id.editTextTextPersonName6);
        mobile=findViewById(R.id.editTextTextPersonName5);
        address1=findViewById(R.id.editTextTextPersonName);
        nextpgbtn=(Button) findViewById(R.id.button2);
        address2=findViewById(R.id.editTextTextPersonName2);
       // hot=findViewById(R.id.radioButton);
        //bio=findViewById(R.id.radioButton2);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){

        }
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });

        nextpgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String son = shopownername.getText().toString();
                final String sn = shopname.getText().toString();
                final String mob = mobile.getText().toString();
                final String add1 = address1.getText().toString();
                final String add2 = address2.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                hot = findViewById(radioId);
                bio  = findViewById(radioId);
                final String hotel = hot.getText().toString();
                final String biogas = bio.getText().toString();
                if(son.isEmpty() || sn.isEmpty() || mob.isEmpty() || add1.isEmpty() || add2.isEmpty()){
                    Toast.makeText(HomeActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                else{
                    databaseReference.child("ProfileDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(sn)){
                                Toast.makeText(HomeActivity.this, "Shop name already available", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                databaseReference.child("ProfileDetails").child(sn).child("ShopOwnerName").setValue(son);
                                databaseReference.child("ProfileDetails").child(sn).child("MobileNo").setValue(mob);
                                databaseReference.child("ProfileDetails").child(sn).child("Addressl1").setValue(add1);
                                databaseReference.child("ProfileDetails").child(sn).child("Addressl2").setValue(add2);
                                if(!hotel.isEmpty()){
                                    databaseReference.child("ProfileDetails").child(sn).child("Users").setValue(hotel);
                                }
                                if(!biogas.isEmpty()){
                                    databaseReference.child("ProfileDetails").child(sn).child("Users").setValue(biogas);
                                }
                                Toast.makeText(HomeActivity.this, "Details Updated", Toast.LENGTH_SHORT).show();
                                MainActivity23();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }
        });

    }
    public void checkButton(View V){
        int radioId = radioGroup.getCheckedRadioButtonId();
        hot = findViewById(radioId);
        bio = findViewById(radioId);



    }

    private void MainActivity23() {
        finish();
        Intent intent = new Intent(this,MainPage.class);
        startActivity(intent);
    }
    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission Denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==IMAGE_PICK_CODE){
            image.setImageURI(data.getData());
        }
    }


    private void SignOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}