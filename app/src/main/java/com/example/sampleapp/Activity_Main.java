package com.example.sampleapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Activity_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタンの設定
        Button goal = findViewById(R.id.top_button1);
        Button studymemo = findViewById(R.id.top_button2);
        Button healthmemo = findViewById(R.id.top_button3);
        Button setting = findViewById(R.id.top_button4);

        //目標を選択
        goal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  System.out.println("目標を選択");
                Intent intent = new Intent(getApplication(), Top_Mokuhyou.class);
                startActivity(intent);
            }
        });
        //勉強記録を選択
        studymemo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  System.out.println("勉強記録を選択");
                Intent intent = new Intent(getApplication(), Top_StudyMemo.class);
                startActivity(intent);
            }
        });
        //健康記録を選択
        healthmemo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  System.out.println("健康記録を選択");
                Intent intent = new Intent(getApplication(), TopHealth.class);
                startActivity(intent);
            }
        });
        //設定を選択
        setting.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  System.out.println("設定を選択");
                Intent intent = new Intent(getApplication(), Top_Setting.class);
                startActivity(intent);
            }
        });

    }
}
