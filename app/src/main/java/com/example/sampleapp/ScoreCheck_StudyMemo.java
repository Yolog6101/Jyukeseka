package com.example.sampleapp;
import static android.view.ViewGroup.LayoutParams.FILL_PARENT;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ScoreCheck_StudyMemo extends AppCompatActivity implements OnClickListener  {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorecheck_studymemo);

        Button btn_back = findViewById(R.id.scsm_gototop);

        btn_back.setOnClickListener(this);

        LinearLayout layout = findViewById(R.id.LinearLayout);

        String string;
        String allstring="";
        int index = DataManager.getNumExams();
        int i = 0;

        while (i < index) {
            string = DataManager.getExamResult(i);
            i++;
            Button button1 = new Button(this);
            button1.setId(i - 1);
            System.out.println(i-1);
            button1.setText(string);
            button1.setOnClickListener(this);
            layout.addView(button1);
        }

    }

    // ボタン入力
    @Override
    public void onClick(View view){

        LinearLayout layout = findViewById(R.id.LinearLayout);//Linearlayout
        if(view.getId()==R.id.scsm_gototop){
            gotoTopStudyMemoScreen();
        }
        else {
            //タップすると削除できる
            layout.removeView(view);
            DataManager.changeExamResults(view.getId());
        }

    }
    // 戻るボタン入力
    @Override
    public void onBackPressed(){
        System.out.println(DataManager.getNumExams());
        gotoTopStudyMemoScreen();

    }
    // 勉強記録画面へ
    private void gotoTopStudyMemoScreen(){
        Intent intent = new Intent(this, Top_StudyMemo.class);
        startActivity(intent);
    }
}
