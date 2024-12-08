package com.Practice.DIACataloge;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeEmail extends AppCompatActivity {
    EditText passwordText, emailText, newEmailText;
    FirebaseAuth Auth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_email);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        passwordText =findViewById(R.id.passwordText);
        newEmailText =findViewById(R.id.newEmailText);
    }


    public void Confirm(View view){
        String newEmail, password;


        newEmail = newEmailText.getText().toString();
        password = passwordText.getText().toString();

        if(newEmail.isEmpty() ){
            newEmailText.setError("Enter new email");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            emailText.setError("email is not correct");
            return;
        }
        Log.i("INFO", "auth start");
        FirebaseUser user = Auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail().toString(), password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("INFO", "reauth");
                    FirebaseUser user = Auth.getCurrentUser();
                    user.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.i("INFO", "email");
                                        Toast.makeText(ChangeEmail.this,"email changed", Toast.LENGTH_SHORT).show();
                                        db.collection("users").document(user.getUid().toString()).update("Email", newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.i("INFO", "email2");
                                                    Toast.makeText(ChangeEmail.this,"Info updated", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }

                                            }
                                        });
                                    }
                                    else{
                                        Log.i("INFO", task.getException().getLocalizedMessage().toString());
                                        Toast.makeText(ChangeEmail.this,task.getException().getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(ChangeEmail.this,"Password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}