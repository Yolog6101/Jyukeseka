package com.example.sampleapp;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Enter_Setting extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_setting);
        findViewById(R.id.ets_button2).setOnClickListener(this);
    }

    public void onClick(View view){
        if(view.getId()==R.id.ets_button2){
            Intent intent = new Intent(this, Top_Setting.class);
            startActivity(intent);
        }
    }

    // 開始
    @Override
    public void onStart(){
        super.onStart();
        // 自動通知
        RadioButton radio = findViewById(Settings.isAutoTweetEnabled() ? R.id.ets_radio1 : R.id.ets_radio2);
        radio.setChecked(true);
    }

    // 終了
    @Override
    public void onPause(){
        super.onPause();
        // 自動通知機能をON・OFF
        RadioGroup rg = findViewById(R.id.ets_radiogroup);
        if(rg.getCheckedRadioButtonId()==R.id.ets_radio1){
            Settings.enableAutoTweet(true);
        }
        if(rg.getCheckedRadioButtonId()==R.id.ets_radio2){
            Settings.enableAutoTweet(false);
        }
    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Top_Setting.class);
        startActivity(intent);
    }
}
