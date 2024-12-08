package com.Practice.DIACataloge;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeName extends AppCompatActivity {
    EditText usernameText;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_name);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usernameText = findViewById(R.id.usernameText);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void Confirm(View view){
        if(usernameText.getText().toString().isEmpty()){
            usernameText.setError("Enter your new name");
            return;
        }

        if(auth.getCurrentUser()==null){
            finish();
        }
        db.collection("users").document(auth.getCurrentUser().getUid()).update("Name",usernameText.getText().toString() ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ChangeName.this, "Name changed successful.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ChangeName.this, "Name change failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}