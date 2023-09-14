package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class ChoiceSubject_StudyMemo extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.choisesubject_studymemo);

        //日付取得
        Calendar cdata=Calendar.getInstance();
        int year=cdata.get(Calendar.YEAR);//getを付けないとミリ秒表記？か何かからの抜粋になる
        int month=cdata.get(Calendar.MONTH)+1;
        int day=cdata.get(Calendar.DATE);
        int weeknum=cdata.get(Calendar.DAY_OF_WEEK);
        String weekdata[]={"","日","月","火","水","木","金","土"};
        String week=weekdata[weeknum];
        String date=year+"年"+month+"月"+day+"日"+"("+week+")";

        //テキストの設定
        TextView datedata=findViewById(R.id.date);
        datedata.setText(date);

        //ボタンの設定
        Button gendai = findViewById(R.id.JAN);
        Button kobun = findViewById(R.id.OLD);
        Button kanbun = findViewById(R.id.CHI);
        Button english = findViewById(R.id.ENG);
        Button math = findViewById(R.id.MAT);
        Button chemistry = findViewById(R.id.CHE);
        Button physics = findViewById(R.id.PHIS);
        Button biology = findViewById(R.id.BIO);
        Button groundsci = findViewById(R.id.GRO);
        Button japanhistory = findViewById(R.id.JANH);
        Button worldhistory = findViewById(R.id.WORH);
        Button world = findViewById(R.id.WOR);
        Button govermenteconomy = findViewById(R.id.ECO);
        Button philosophy = findViewById(R.id.PHI);
        Button other = findViewById(R.id.OTH);

        //反応では画面遷移時に科目も送信している
        //反応(現代文)
        gendai.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  System.out.println("現代文を選択");
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","現代文");
                startActivity(intent1);
            }
        });//ここまで
        //反応(古文)
        kobun.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","古文");
                startActivity(intent1);
            }
        });//ここまで
        //反応(漢文)
        kanbun.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","漢文");
                startActivity(intent1);
            }
        });//ここまで
        //反応(英語)
        english.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","英語");
                startActivity(intent1);
            }
        });//ここまで
        //反応(数学)
        math.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","数学");
                startActivity(intent1);
            }
        });//ここまで
        //反応(化学)
        chemistry.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","化学");
                startActivity(intent1);
            }
        });//ここまで
        //反応(物理)
        physics.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","物理");
                startActivity(intent1);
            }
        });//ここまで
        //反応(生物)
        biology.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","生物");
                startActivity(intent1);
            }
        });//ここまで
        //反応(地学)
        groundsci.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","地学");
                startActivity(intent1);
            }
        });//ここまで
        //反応(日本史)
        japanhistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","日本史");
                startActivity(intent1);
            }
        });//ここまで
        //反応(世界史)
        worldhistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","世界史");
                startActivity(intent1);
            }
        });//ここまで
        //反応(地理)
        world.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","地理");
                startActivity(intent1);
            }
        });//ここまで
        //反応(政治経済)
        govermenteconomy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","政治経済");
                startActivity(intent1);
            }
        });//ここまで
        //反応(倫理)
        philosophy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","倫理");
                startActivity(intent1);
            }
        });//ここまで
        //反応(その他)
        other.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), timer_studymemo.class);
                intent1.putExtra("SUBJECT","その他");
                startActivity(intent1);
            }
        });//ここまで

    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Top_StudyMemo.class);
        startActivity(intent);
    }
}



