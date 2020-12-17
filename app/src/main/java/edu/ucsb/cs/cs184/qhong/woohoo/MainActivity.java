package edu.ucsb.cs.cs184.qhong.woohoo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.qhong.woohoo.utils.User;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private MainViewModel mainViewModel;

    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton button = findViewById(R.id.fab);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_friend, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //----------------------    Nov,28--  Jiajun Li     ------------------------
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if(mainViewModel.isSignedIn()){
//            Log.e("Tag","Signed in");
            setAuthorizedUser();
        }else{
//            Log.e("Tag","Not Signed in");
            setUnauthorizedUser();
        }
        TextView t = navigationView.getHeaderView(0).findViewById(R.id.nickname);
//        Log.e("Tag", (String) t.getText());
        //add firebase for testing
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        //----------------------    Nov,28--  Jiajun Li     ------------------------
    }

    public void setUnauthorizedUser(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView nickname = navigationView.getHeaderView(0).findViewById(R.id.nickname);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.textView);
        Button gotoLogin = navigationView.getHeaderView(0).findViewById(R.id.gotoLogin);
        Button signout = navigationView.getHeaderView(0).findViewById(R.id.signOut);
        signout.setVisibility(View.GONE);
        nickname.setText("Please login to your account");
        email.setVisibility(View.GONE);
        gotoLogin.setVisibility(View.VISIBLE);
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void setAuthorizedUser(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        final TextView nickname = navigationView.getHeaderView(0).findViewById(R.id.nickname);
        Button gotoLogin = navigationView.getHeaderView(0).findViewById(R.id.gotoLogin);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.textView);
        Button signout = navigationView.getHeaderView(0).findViewById(R.id.signOut);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference t = myRef.child(currentUser.getUid()).child("name");
        t.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nickname.setText(snapshot.getValue().toString());
                mainViewModel.setmNickname(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference pendingList = myRef.child(currentUser.getUid()).child("pending friends");
        pendingList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> temp = new ArrayList<>();
                for(DataSnapshot shot:snapshot.getChildren()){
                    User curUser = new User();
                    if(shot.child("icon").getValue() != null){
                        curUser.setIcon(shot.child("icon").getValue().toString());
                        curUser.setEmail(shot.child("email").getValue().toString());
                        curUser.setName(shot.child("name").getValue().toString());
                        curUser.setUid(shot.getKey());
                        temp.add(curUser);
                    }
                }
                mainViewModel.setFriendsPendingList(temp);
//                Log.e("Tag","Finished loading Pending Friend List" + temp.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        signout.setVisibility(View.VISIBLE);
        email.setText(mainViewModel.getmEmail().getValue());
        email.setVisibility(View.VISIBLE);
        gotoLogin.setVisibility(View.GONE);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.signOut();
                setUnauthorizedUser();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("Tag","onResume");
        mainViewModel.update();
        if(mainViewModel.isSignedIn()){
//            Log.e("Tag","Signed in");
            setAuthorizedUser();
            mDialog = ProgressDialog.show(MainActivity.this,"Load user data","Loading...",true);

            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            mainViewModel.updateFriendsList(currentUser);
            new Thread(){
                public void run(){
                    try{
                        mainViewModel.fetchCurUsers(currentUser.getUid());
                        while(mainViewModel.fetched){
                            sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally{
                        mDialog.dismiss();
                        Log.e("Tag","finish loading");
                    }
                }
            }.start();
        }else{
//            Log.e("Tag","Not Signed in");
            setUnauthorizedUser();
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.e("Tag","onRestart");
//        mainViewModel.update();
//        Log.e("Tag",mainViewModel.getmEmail().getValue()+"asd");
//        if(mainViewModel.isSignedIn()){
//            Log.e("Tag","Signed in");
//            setAuthorizedUser();
//        }else{
//            Log.e("Tag","Not Signed in");
//            setUnauthorizedUser();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}