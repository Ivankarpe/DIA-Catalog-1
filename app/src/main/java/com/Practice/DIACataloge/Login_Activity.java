package com.Practice.DIACataloge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    EditText emailText, passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
    }
    public void Login(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();


        if(password.isEmpty()){
            passwordText.setError("Password must not be empty");
            return;
        }
        if(email.isEmpty()){
            emailText.setError("Email must not be empty");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("email is not correct");
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                            startActivity( new Intent( Login_Activity.this, Account_Activity.class));

                            finish();
                        } else {
                            Toast.makeText(Login_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void registerClic(View view){
        startActivity( new Intent( Login_Activity.this, Register_Activity.class));
        finish();
    }
}