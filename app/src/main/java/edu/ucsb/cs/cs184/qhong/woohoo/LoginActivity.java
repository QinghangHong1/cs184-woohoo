package edu.ucsb.cs.cs184.qhong.woohoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    //firebase authentication
    private FirebaseAuth mAuth;

    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Log.e("TAG","Signed in");
        }

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText nickname = findViewById(R.id.name);
                checkNameAvailability(nickname.getText().toString());
            }
        });
        Button SignInButton = findViewById(R.id.SignInButton);
        SignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithEmail:success");
                                    finish();
                                }else {
                                    // If sign in fails, display a message to the user.
//                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }
        });
    }

    public void checkNameAvailability(final String name){
        if(name.equals("")){
            Toast.makeText(LoginActivity.this, "Please enter a nickname in order to sign up.",
                    Toast.LENGTH_LONG).show();
        }else {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean t = true;
                    for (DataSnapshot sshot : snapshot.getChildren()) {
                        String temp = sshot.child("name").getValue(String.class);
                        Log.e("Tag", temp);
                        if (temp.equals(name)) {
                            Toast.makeText(LoginActivity.this, "Username has already been taken.",
                                    Toast.LENGTH_LONG).show();
                            t = false;
                            break;
                        }
                    }
                    if (t) {
                        signUp(name);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void signUp(final String name){
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            DatabaseReference user = myRef.child(currentUser.getUid());
                            user.child("name").setValue(name);
                            user.child("email").setValue(currentUser.getEmail());
                            user.child("icon").setValue("Default profile photo");

                            finish();
                        }else {
                            // If sign in fails, display a message to the user.
//                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}