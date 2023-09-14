package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import android.os.SystemClock;

//import com.example.test0615.DataManager;
//import com.example.test0615.R;
//import com.example.test0615.TopStudyMemo;

import java.util.Calendar;

public class timer_studymemo extends AppCompatActivity implements View.OnClickListener{

    Chronometer timeset = null;
    @Override
    public void onBackPressed(){
        gotoTopStudyScreen();
    }

    private void gotoTopStudyScreen(){
        Intent intent = new Intent(this, Top_StudyMemo.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_study_memo);

        //日付取得
        Calendar cdata=Calendar.getInstance();
        int year=cdata.get(Calendar.YEAR);//getを付けないとミリ秒表記？か何かからの抜粋になる
        int month=cdata.get(Calendar.MONTH)+1;
        int day=cdata.get(Calendar.DATE);
        int weeknum=cdata.get(Calendar.DAY_OF_WEEK);
        String weekdata[]={"","日","月","火","水","木","金","土"};
        String week=weekdata[weeknum];
        String date=year+"年"+month+"月"+day+"日"+"("+week+")";
        final int[] studytime = {0};//勉強時間(秒)

        //テキストの設定
        TextView datedata=findViewById(R.id.date2);
        datedata.setText(date);

        //ボタン設定
        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        Button reset = findViewById(R.id.reset);
        Button fin = findViewById(R.id.fintweet);
        Button re = findViewById(R.id.gotop);

        //戻るボタン設定
        re.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  トップに戻る
                Intent intent1 = new Intent(getApplication(), Top_StudyMemo.class);
                startActivity(intent1);
            }
        });//ここまで

        //科目を受け取ること
        Intent beforeintent=getIntent();
        String subject=beforeintent.getStringExtra("SUBJECT");

        TextView st=findViewById(R.id.subject);
        st.setText(subject);

        timeset=findViewById(R.id.timemove);//タイマー文字設定
        final long[] stopswitch = new long[1];
        final int[] starton = {0};//1でスタートボタンが押されている

        start.setOnClickListener(new View.OnClickListener() {//スタートボタン
            public void onClick(View v) {
                timeset.setBase(SystemClock.elapsedRealtime()-stopswitch[0]);
                timeset.start();
                starton[0] =1;
                start.setClickable(false);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                timeset.stop();
                if(starton[0]==1) {//すでにスタートボタンが押されていれば
                    stopswitch[0] = SystemClock.elapsedRealtime() - timeset.getBase();
                    starton[0]=0;
                    start.setClickable(true);
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timeset.setBase(SystemClock.elapsedRealtime());
                stopswitch[0]=0;
                start.setClickable(true);
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("as:"+(SystemClock.elapsedRealtime()-timeset.getBase()));//1000で割り小数点以下を切り捨てると秒になる
                studytime[0] =(int)((SystemClock.elapsedRealtime()-timeset.getBase())/1000);//intでは小数点以下は自動で切り捨て
                timeset.stop();
                starton[0]=0;

                //秒→時間
                int studytime_min = studytime[0] / 60;//1度分単位にして(秒を切り捨て)
                double studytime_hour = (double) (Math.round(((double) studytime_min) / 0.6)) / 100;//先に0.6で割って小数第1位で四捨五入し、100で割る(結果的に元の値(単位:分)を少数第3位で四捨五入したことになる)
                //データクラスに記録する　現代文=0～その他=14
                int subjectnumber=0;//科目の識別番号
                switch (subject) {
                    case "現代文":
                        subjectnumber=0;
                        break;
                    case "古文":
                        subjectnumber=1;
                        break;
                    case "漢文":
                        subjectnumber=2;
                        break;
                    case "英語":
                        subjectnumber=3;
                        break;
                    case "数学":
                        subjectnumber=4;
                        break;
                    case "化学":
                        subjectnumber=5;
                        break;
                    case "物理":
                        subjectnumber=6;
                        break;
                    case "生物":
                        subjectnumber=7;
                        break;
                    case "地学":
                        subjectnumber=8;
                        break;
                    case "日本史":
                        subjectnumber=9;
                        break;
                    case "世界史":
                        subjectnumber=10;
                        break;
                    case "地理":
                        subjectnumber=11;
                        break;
                    case "政治経済":
                        subjectnumber=12;
                        break;
                    case "倫理":
                        subjectnumber=13;
                        break;
                    case "その他":
                        subjectnumber=14;
                        break;
                    default:
                        break;
                }//intに変える

                DataManager.writeStudyTime(subjectnumber,studytime_hour);//時間(studytime_hour)と科目(subjectdata)をデータクラスに記録
                tweet(month, day, subject, studytime[0]);
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    // 共有画面へ
    private void tweet(int month, int day, String subjectName, int studytime){
        Intent intent1 = new Intent(getApplication(), ShareStudymemo.class);
        intent1.putExtra("STUDYTIME", studytime);
        intent1.putExtra("STUDYMONTH", month);
        intent1.putExtra("STUDYDAY", day);
        intent1.putExtra("STUDYSUBJECT", subjectName);
        startActivity(intent1);
    }

}
