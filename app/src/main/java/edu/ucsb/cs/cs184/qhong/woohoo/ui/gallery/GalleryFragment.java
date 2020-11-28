package edu.ucsb.cs.cs184.qhong.woohoo.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.ucsb.cs.cs184.qhong.woohoo.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    //firebase authentication
    private FirebaseAuth mAuth;
    private boolean signedIn = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    //----------------------    Nov,28--  Jiajun Li     ------------------------
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //authentication
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            signedIn = true;
            Log.e("TAG","Signed in");
        }
        if(!signedIn){
            Button signUpButton = getActivity().findViewById(R.id.signUpButton);
            signUpButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    EditText email = getActivity().findViewById(R.id.email);
                    EditText password = getActivity().findViewById(R.id.password);
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if(currentUser!=null){
                                            signedIn = true;
                                            Log.e("TAG","Signed in");
                                        }
                                    }else {
                                        // If sign in fails, display a message to the user.
//                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            });
            Button SignInButton = getActivity().findViewById(R.id.SignInButton);
            SignInButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    EditText email = getActivity().findViewById(R.id.email);
                    EditText password = getActivity().findViewById(R.id.password);
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        signedIn = true;
                                    }else {
                                        // If sign in fails, display a message to the user.
//                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }

                                    // ...
                                }
                            });
                }
            });
        }else{
            EditText email = getActivity().findViewById(R.id.email);
            EditText password = getActivity().findViewById(R.id.password);
            Button signUpButton = getActivity().findViewById(R.id.signUpButton);
            Button signInButton = getActivity().findViewById(R.id.SignInButton);
            Button signOutButton = getActivity().findViewById(R.id.signOutButton);
            TextView text = getActivity().findViewById(R.id.signedInText);
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);
            signInButton.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            signOutButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    signedIn = false;
                }
            });

        }
    }
    //check if the user is currently signed in
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            signedIn = true;
            Log.e("TAG","Signed in");
        }
    }
    //----------------------    Nov,28--  Jiajun Li     ------------------------
}