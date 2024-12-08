package com.Practice.DIACataloge;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    EditText emailText, passwordText, confirmNewPasswordText, newPasswordText;
    FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Auth = FirebaseAuth.getInstance();
        passwordText = findViewById(R.id.passwordText);
        confirmNewPasswordText = findViewById(R.id.confirmNewPasswordText);
        newPasswordText = findViewById(R.id.newPasswordText);


    }


    public void Confirm(View view){

        if(!CheckPassword(newPasswordText.getText().toString())){
            return;
        }
        if(!doesPasswordSame(newPasswordText.getText().toString(), confirmNewPasswordText.getText().toString())){
            confirmNewPasswordText.setError("passwords must be the same");
            return;
        }

        FirebaseUser user = Auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail().toString(), passwordText.getText().toString());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("INFO", "reauth");
                    FirebaseUser user = Auth.getCurrentUser();
                    user.updatePassword(newPasswordText.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ChangePassword.this,"Password changed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePassword.this,"Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(ChangePassword.this,"Password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean doesPasswordSame(String password, String password2){
        return password.equals(password2);
    }
    public void ForgotPassword(View view){
        FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
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

}