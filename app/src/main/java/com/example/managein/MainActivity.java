package com.example.managein;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.opencv.android.OpenCVLoader;

//This department manages the login page - the login page for the application checks the correctness of the login of an existing user and transfers it to the home page, and also allows the login of a new user by transferring it by clicking on the "New User" button to the registration page
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static String TAG="Main Activity";
//--------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }
//-------------------------------------------------------------------------------------------------------------------
    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
//-------------------------------------------------------------------------------------------------------------------
    public void register(View view){
        Intent intent=new Intent(MainActivity.this,LoginNewUser.class);
        startActivity(intent);
    }
//----------------------------------------------------------------------------------------------------------------
    private void updateUI(FirebaseUser user) {
    }
//-------------------------------------------------------------------------------------------------------------------
    //login exsist user
    public void login(View vie) {
        EditText email=findViewById(R.id.editTextTextEmailAddress2);
        EditText password=findViewById(R.id.editTextTextPassword);
       if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(new Intent(MainActivity.this, homepage.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else {
            // If sign in fails, display a message to the user.
            Toast.makeText(MainActivity.this, "the input values can't be empty", Toast.LENGTH_LONG).show();
        }
    }
}