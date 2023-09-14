package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TopHealth extends AppCompatActivity implements View.OnClickListener {

    // 作成
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_healthmemo);

        findViewById(R.id.thm_button1).setOnClickListener(this);
        findViewById(R.id.thm_button2).setOnClickListener(this);
        findViewById(R.id.thm_button3).setOnClickListener(this);
    }
    // ボタン入力
    @Override
    public void onClick(View view){
        if(view.getId()==R.id.thm_button1){
            gotoEnterHealthScreen();
        }
        if(view.getId()==R.id.thm_button2){
            gotoGraphHealthScreen();
        }
        if(view.getId()==R.id.thm_button3){
            gotoTopScreen();
        }
    }
    // 戻るボタン入力
    @Override
    public void onBackPressed(){
        gotoTopScreen();
    }
    // トップへ戻る
    private void gotoTopScreen(){
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
    }
    // データ入力画面へ
    private void gotoEnterHealthScreen(){
        Intent intent = new Intent(this, EnterHealth.class);
        startActivity(intent);
    }
    // グラフ表示画面へ
    private void gotoGraphHealthScreen(){
        Intent intent = new Intent(this, GraphHealth.class);
        startActivity(intent);
    }
}
