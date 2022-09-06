package com.example.managein;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
//This department manages the new user registration page that contains an option to input the user's information and check the correctness of the input and enter the correct data into the database.
public class LoginNewUser extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new_user);
        mAuth = FirebaseAuth.getInstance();
        //SaveDetailes();
    }


//-----------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            System.out.println("the user is null");
        }
    }

//---------------------------------------------------------
//    This function saves the user details entered by the user
    public void SaveDetailes(View view){
        EditText email=findViewById(R.id.userEmailInput);
        EditText password=findViewById(R.id.userPasswordInput);
        EditText id=findViewById(R.id.userIDInput);
        EditText name=findViewById(R.id.userNameInput);
        inputIntegrityCheck(email,password,id,name);
    }
    //    ----------------------------------------------------------
//    This function checking for integrity of the input details - the user details
    private void inputIntegrityCheck(EditText email,EditText password,EditText id,EditText name){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("UserName");
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")&& !id.getText().toString().equals("")&& !name.getText().toString().equals("")) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(LoginNewUser.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if (task.isSuccessful()){
                                DatabaseReference ref = database.getReference("users/"+FirebaseAuth.getInstance().getUid());
                                final User user = Repository.BuildNewUser(email.getText().toString(),name.getText().toString(),ref.getKey(),Integer.parseInt(password.getText().toString()),Integer.parseInt(id.getText().toString()));
                                DatabaseReference refinvoic = database.getReference("users/"+FirebaseAuth.getInstance().getUid()+"/invoices");
                                ref.setValue(user);
                                startActivity(new Intent(LoginNewUser.this, homepage.class));
                            }
                            else{
                                Toast.makeText(LoginNewUser.this, "Registration failed Check email address and password ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(LoginNewUser.this, "the input values can't be empty", Toast.LENGTH_LONG).show();
        }
    }

}