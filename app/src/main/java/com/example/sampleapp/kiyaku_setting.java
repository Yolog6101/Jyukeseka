package com.example.sampleapp;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;


public class kiyaku_setting extends AppCompatActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kiyaku_setting);
        findViewById(R.id.button_totopsetting).setOnClickListener(this);
    }

    public void onClick(View view){
        if(view.getId()==R.id.button_totopsetting){
            Intent intent = new Intent(this, Top_Setting.class);
            startActivity(intent);
        }
    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Top_Setting.class);
        startActivity(intent);
    }
}
