package com.Practice.DIACataloge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Account_Activity extends BaseActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    BottomNavigationView navigationView;

    TextView emailText, usernameText;

    Button editProfileButton, newItem;
    ProgressBar progresBarForName;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        newItem = findViewById(R.id.newIt);
        navigationView = findViewById(R.id.navigationBar);

        emailText = findViewById(R.id.emailText);
        usernameText = findViewById(R.id.usernameText);

        progresBarForName = findViewById(R.id.progressBar);
        editProfileButton = findViewById(R.id.button);

        emailText.setVisibility(View.INVISIBLE);
        usernameText.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.rvLastSeen);


        navigationView.setSelectedItemId(R.id.profile);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    startActivity( new Intent( Account_Activity.this, MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(item.getItemId() == R.id.compare){
                    startActivity( new Intent( Account_Activity.this, Compare_Acrivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(item.getItemId() == R.id.favorits){
                    startActivity( new Intent( Account_Activity.this, Favorities_Activity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(item.getItemId() == R.id.profile){

                    return true;
                }
                return false;
            }
        });

        refreshName();
        loadLastSeen();
    }


    private void loadLastSeen(){
        Log.i("INFO","load");
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.i("INFO","userload");
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        List<String> ids = doc.toObject(UserDataClass.class).LastView;
                        List<ItemsModel> list = new ArrayList<>();
                        List<Task> tasks = new ArrayList<>();
                        for(String id : ids){
                            Log.i("INFO","load item " + id);
                            if (!Objects.equals(id, "")) {
                                tasks.add(firebaseFirestore.collection("Items").document(id).get());
                            }

                        }
                        Task mergetask = Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                            @Override
                            public void onSuccess(List<Object> objects) {
                                Log.i("INFO","merging");
                                for(Object ob : objects){
                                    ItemsModel model = ((DocumentSnapshot) ob).toObject(ItemsModel.class);
                                    model.setIditeam(((DocumentSnapshot) ob).getId().toString());
                                    list.add(model);
                                }
                                Collections.reverse(list);
                                RecommendedAdapt adapt = new RecommendedAdapt(list,Account_Activity.this);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Account_Activity.this, LinearLayoutManager.HORIZONTAL, false);
                                recyclerView.setLayoutManager(horizontalLayoutManager);
                                recyclerView.setAdapter(adapt);
                                Log.i("INFO","loadedall");
                            }
                        });


                    }

                }
            }
        });

    }

    private void refreshName(){
        DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid().toString());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        if(doc.get("Status").equals("Admin")){
                            newItem.setVisibility(View.VISIBLE);
                            Log.i("INFO","vas");
                        }else{
                            newItem.setVisibility(View.INVISIBLE);
                            Log.i("INFO","wos");
                        }
                        emailText.setText(doc.get("Email").toString());
                        usernameText.setText(doc.get("Name").toString());
                        emailText.setVisibility(View.VISIBLE);
                        usernameText.setVisibility(View.VISIBLE);
                        progresBarForName.setVisibility(View.INVISIBLE);
                        Log.i("INFO","dsf");


                    }
                }
            }
        });
    }
    public void LogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Account_Activity.this, MainActivity.class));
        finish();
    }

    public void ChangeName(View view){
        startActivity(new Intent(Account_Activity.this, ChangeName.class));
    }

    public void ChangeEmail(View view){
        startActivity(new Intent(Account_Activity.this, ChangeEmail.class));
    }

    public void ChangePassword(View view){
        startActivity(new Intent(Account_Activity.this, ChangePassword.class));
    }
    public void NewItem(View view){
        startActivity(new Intent(Account_Activity.this, ItemNewActivity.class));
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshName();
    }
}