package com.example.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

public class ShareStudymemo extends AppCompatActivity {
    //変数
    int studytimed;
    int datemonth;
    int dateday;
    String subjectdata;

    @Override
    public void onBackPressed(){
        gotoTopStudyScreen();
    }

    private void gotoTopStudyScreen(){
        Intent intent = new Intent(this, Top_StudyMemo.class);
        startActivity(intent);
    }

    protected void onStart(){
        super.onStart();
        autoPop();

        //勉強時間を受け取ること
        Intent beforeintent=getIntent();
        studytimed=beforeintent.getIntExtra("STUDYTIME",0);
        //日付を受け取ること
        datemonth=beforeintent.getIntExtra("STUDYMONTH",0);//月
        dateday=beforeintent.getIntExtra("STUDYDAY",0);//日
        //科目を受け取ること
        subjectdata=beforeintent.getStringExtra("STUDYSUBJECT");
        EditText text=findViewById(R.id.detailbox);
        text.setText("継続的な学習を目指す");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_studymemo);

        //ボタン設定
        Button top = findViewById(R.id.gotop_twi);
        Button tweet=findViewById(R.id.tweet);

        //変数設定
        final String[] tweet_detail = new String[1];
        final String[] tweet_str=new String[1];

        //テキストボックス設定
        TextView tweetbox=findViewById(R.id.detailbox);

        //戻るボタン設定
        top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //  トップに戻る
                Intent intent1 = new Intent(getApplication(), Top_StudyMemo.class);
                startActivity(intent1);
            }
        });

        tweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tweet_detail[0] = tweetbox.getText().toString();//チェックボックス内のデータを取り込む
                if (tweet_detail[0].length() > 100) {
                    tweetbox.setText("");
                }
                else {
                    //通知準備
                    int studytimed_min = studytimed / 60;//1度分単位にして(秒を切り捨て)
                    double studytimed_hour = (double) (Math.round(((double) studytimed_min) / 0.6)) / 100;//先に0.6で割って小数第1位で四捨五入し、100で割る(結果的に元の値(単位:分)を少数第3位で四捨五入したことになる)
                    // 共有
                    share(datemonth, dateday, subjectdata, studytimed_hour, tweet_detail[0]);

                }
            }
        });
    }

    // 共有
    private void share(int month, int day, String subjectName, double studytime, String detail){
        //ツイート
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);
        String subject = "件名";
        String bodyText = String.format("%02d/%02d %s %.2f時間\n%s", month, day, subjectName, studytime, detail);
        builder.setSubject(subject) /// 件名
                .setText(bodyText)  /// 本文
                .setType("text/plain");
        Intent intent = builder.createChooserIntent();

        /// 結果を受け取らずに起動
        builder.startChooser();
    }

    //自動通知
    private void autoPop(){
        // 自動通知準備
        if(!Settings.isAutoTweetEnabled()) return;//通知しない場合はここで終了
        double studytimeweekc=0;//今週の勉強時間(全科目)
        double studytimelastweekc=0;//先週の勉強時間(全科目)
        for(int i=0;i<14;i++) {//全科目
            //今週
            double[] thisweek=DataManager.getStudyTime(i,DataManager.getCurrentDateCount()-6,DataManager.getCurrentDateCount());//1科目の1週間の勉強時間
            for(int it=0;it<thisweek.length;it++){
                studytimeweekc=studytimeweekc+thisweek[it];
            }
            //先週
            double[] lastweek=DataManager.getStudyTime(i,DataManager.getCurrentDateCount()-7,DataManager.getCurrentDateCount()-13);//1科目の先週1週間の勉強時間
            for(int il=0;il<lastweek.length;il++){
                    studytimelastweekc=studytimelastweekc+lastweek[il];
            }
        }
        String timecheck;
        if((studytimeweekc-studytimelastweekc)>=0)//今週の方が長い
        {
            timecheck="先週より"+(studytimeweekc-studytimelastweekc)+"時間多いです！";
        }
        else{
            timecheck="先週より"+(studytimelastweekc-studytimeweekc)+"時間少ないです！";
        }
        String autotweet=String.format("今週の総勉強時間\n%.2f時間\n%s",studytimeweekc,timecheck);
        Popup p=new Popup();
        LayoutInflater layoutInflater = getLayoutInflater();
        p.pop(autotweet,layoutInflater);

    }
}
