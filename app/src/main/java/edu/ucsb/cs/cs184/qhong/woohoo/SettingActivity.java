package edu.ucsb.cs.cs184.qhong.woohoo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

//YZ: Create(setting) Room Activity
public class SettingActivity extends AppCompatActivity {

    private SettingViewModel settingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        FloatingActionButton fab = findViewById(R.id.fab1);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Code here executes on main thread after user presses button
//                Intent intent=new Intent(SettingActivity.this, MainActivity.class );
//                startActivity(intent);
//            }
//        });



    }


}