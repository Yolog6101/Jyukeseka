package com.example.sampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;


public class Top_StudyMemo extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_studymemo);
        findViewById(R.id.tsm_button1).setOnClickListener(this);
        findViewById(R.id.tsm_button2).setOnClickListener(this);
        findViewById(R.id.tsm_button3).setOnClickListener(this);
        findViewById(R.id.tsm_button4).setOnClickListener(this);
        findViewById(R.id.tsm_button5).setOnClickListener(this);

        DataManager.notifyRun(getApplicationContext());
        Settings.notifyRun(getApplicationContext());
    }

    public void onClick(View view){
        if(view.getId()==R.id.tsm_button1){
            Intent intent = new Intent(this, EnterScore_StudyMemo.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tsm_button2){
            Intent intent = new Intent(this, ChoiceSubject_StudyMemo.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tsm_button3){
            Intent intent = new Intent(this, ScoreCheck_StudyMemo.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tsm_button4){
            Intent intent = new Intent(this, Graph_StudyMemo.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.tsm_button5){
            Intent intent = new Intent(this, Activity_Main.class);
            startActivity(intent);
        }


    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Activity_Main.class);
        startActivity(intent);
    }
}
