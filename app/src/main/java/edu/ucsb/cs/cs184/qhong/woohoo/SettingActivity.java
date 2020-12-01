package edu.ucsb.cs.cs184.qhong.woohoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
//YZ: Create(setting) Room Activity
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent=new Intent(SettingActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });



//        Button submitSetting = findViewById(R.id.settingCreateButton);
//        submitSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Code here executes on main thread after user presses button
//                Intent intent=new Intent(SettingActivity.this, WaitRoomActivity.class );
//                startActivity(intent);
//            }
//        });

    }

}