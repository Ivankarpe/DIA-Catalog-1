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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Register_Activity extends AppCompatActivity {

    EditText emailText, passwordText, confirmPasswordText, namwText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        namwText = findViewById(R.id.nameText);
    }

    private boolean doesPasswordSame(String password, String password2){
        return password.equals(password2);
    }

    private boolean CheckPassword(String password){
        if(password.length() < 8){
            passwordText.setError("Password must be 8 characters or longer");
            return false;
        }
        boolean hasSpecialCharacter = false;
        boolean hasCapitalCharacter = false;
        boolean hasNumbersCharacter = false;
        boolean hasSpaceCharacter = false;

        for (char ch : password.toCharArray()){
            if(Character.isUpperCase(ch)){
                hasCapitalCharacter = true;
            }
            if(Character.isDigit(ch)){
                hasNumbersCharacter = true;
            }
            if(Character.isSpaceChar(ch)){
                hasSpaceCharacter = true;
            }
            if("!@#$%^&*()".indexOf(ch) != -1){
                hasSpecialCharacter = true;
            }

        }

        if(!hasCapitalCharacter){
            passwordText.setError("Password must contain capital letters");
            return false;
        }
        if(!hasNumbersCharacter){
            passwordText.setError("Password must contain numbers");
            return false;
        }
        if(!hasSpecialCharacter){
            passwordText.setError("\"Password must contain special characters");
            return false;
        }
        if(hasSpaceCharacter){
            passwordText.setError("Password mustn't contain spaces");
            return false;
        }
        return true;

    }


    public void onRegisterClick(View view){

        if(!CheckPassword(passwordText.getText().toString())){
            return;
        }
        if(!doesPasswordSame(passwordText.getText().toString(), confirmPasswordText.getText().toString())){
            confirmPasswordText.setError("passwords must be the same");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()){
            emailText.setError("email is not correct");
            return;
        }
        if(namwText.getText().toString().isEmpty()){
            namwText.setError("Enter your name");
            return;
        }
        CreateFirebaseAccount(emailText.getText().toString(),passwordText.getText().toString(), namwText.getText().toString());
    }

    private void CreateFirebaseAccount(String email, String password, String name){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register_Activity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register_Activity.this,"Registration successful, please check your email inbox", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            Map<String, Object> user = new HashMap<>();
                            List<String> stringList = new ArrayList<>();
                            user.put("Name", name);
                            user.put("Email", email);
                            user.put("LastView", stringList);
                            user.put("Favorites", stringList);
                            user.put("Status", "User");

                            firebaseFirestore.collection("users")
                                             .document(firebaseAuth.getCurrentUser().getUid().toString())
                                             .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(Register_Activity.this,"Account created", Toast.LENGTH_SHORT).show();
                                                        startActivity( new Intent( Register_Activity.this, MainActivity.class));
                                                        finish();
                                                    }
                                            });

                        }else{
                            Toast.makeText(Register_Activity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void LoginClic(View view){
        startActivity( new Intent( Register_Activity.this, Login_Activity.class));
        finish();
    }
}
