package com.example.sampleapp;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


public class Top_Setting extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_setting);
        findViewById(R.id.tps_button1).setOnClickListener(this);
        findViewById(R.id.tps_button2).setOnClickListener(this);
        findViewById(R.id.tps_button3).setOnClickListener(this);
        findViewById(R.id.tps_button4).setOnClickListener(this);
        findViewById(R.id.tps_button5).setOnClickListener(this);
    }

    public void onClick(View view){
        if(view.getId()==R.id.tps_button1){
            Intent intent = new Intent(this, Enter_Setting.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tps_button2){
            Intent intent = new Intent(this, kiyaku_setting.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tps_button3){
            Intent intent = new Intent(this, policy_setting.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tps_button4){
            Intent intent = new Intent(this, Activity_Main.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tps_button5){
            gotoResetPasswordScreen();
        }

    }

    // パスワード変更画面へ
    private void gotoResetPasswordScreen(){
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Activity_Main.class);
        startActivity(intent);
    }
}
