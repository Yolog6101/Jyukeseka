package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class ShareHealth extends AppCompatActivity implements View.OnClickListener {

    // 定数
    private static final int MAX_NUM_CHARACTERS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_healthmemo);

        findViewById(R.id.twhm_button1).setOnClickListener(this);
        findViewById(R.id.twhm_button2).setOnClickListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // 記録
        record();
        // 自動通知
        autoPop();
        // メッセージ設定
        EditText et = findViewById(R.id.twhm_edit1);
        et.setText(generateMessage());
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.twhm_button1){
            gotoTopHealthScreen();
        }
        if(view.getId()==R.id.twhm_button2){
            share();
        }
    }
    // 戻るボタン入力
    @Override
    public void onBackPressed(){
        gotoTopHealthScreen();
    }
    // 健康記録画面へ
    private void gotoTopHealthScreen(){
        Intent intent = new Intent(this, TopHealth.class);
        startActivity(intent);
    }
    // 内容を生成
    private String generateMessage(){
        // 文字列
        StringBuffer sb = new StringBuffer("");
        // 日付
        Calendar cal = Calendar.getInstance();
        sb.append(String.format("%d/%02d/%02d ", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
        // 内容
        switch(EnterHealth.getInputMode()){
            case EnterHealth.MODE_EXERCISE:{
                sb.append("運動時間 ");
                sb.append(String.format("%.1f分", EnterHealth.getInputValue()));
                sb.append("\r\n");
                sb.append("運動時間の維持を目指す");
                break;
            }
            case EnterHealth.MODE_SLEEP:{
                sb.append("睡眠時間 ");
                sb.append(String.format("%.1f時間", EnterHealth.getInputValue()));
                sb.append("\r\n");
                sb.append("睡眠時間の維持を目指す");
                break;
            }
            case EnterHealth.MODE_WEIGHT:{
                sb.append("体重 ");
                sb.append(String.format("%.1fkg", EnterHealth.getInputValue()));
                sb.append("\r\n");
                sb.append("健康的な体重の維持を目指す");
                break;
            }
        }
        return sb.toString();
    }
    // 共有
    private void share(){
        // テキストを取得
        EditText et = findViewById(R.id.twhm_edit1);
        SpannableStringBuilder ssb = (SpannableStringBuilder)et.getText();
        String str = ssb.toString();
        // 文字数制限を確認
        if(str.length() > MAX_NUM_CHARACTERS){
            // メッセージを出して中断
            Toast.makeText(getBaseContext(), "文字数が制限を超えています。", Toast.LENGTH_LONG).show();
            return;
        }
        // 共有
        ShareCompat.IntentBuilder builder
                = ShareCompat.IntentBuilder.from(this);
        String subject = "件名";
        String bodyText = generateMessage();
        builder.setSubject(subject) /// 件名
                .setText(bodyText)  /// 本文
                .setType("text/plain");
        Intent intent = builder.createChooserIntent();

        /// 結果を受け取らずに起動
        builder.startChooser();
    }
    // 記録
    private void record(){
        switch(EnterHealth.getInputMode()){
            case EnterHealth.MODE_EXERCISE:{
                DataManager.writeExerciseTime(EnterHealth.getInputValue());
                break;
            }
            case EnterHealth.MODE_SLEEP:{
                DataManager.writeSleepTime(EnterHealth.getInputValue());
                break;
            }
            case EnterHealth.MODE_WEIGHT:{
                DataManager.writeWeight(EnterHealth.getInputValue());
                break;
            }
        }
    }
    // 自動通知
    private void autoPop(){
        // 自動通知有効判定
        if(!Settings.isAutoTweetEnabled()) return;
        // 文字列
        String message = null;
        // 今日の日付カウント
        int cToday = DataManager.getCurrentDateCount();
        // 種類で分岐
        switch(EnterHealth.getInputMode()){
            case EnterHealth.MODE_EXERCISE:{ // 運動時間

                break;
            }
            case EnterHealth.MODE_SLEEP:{ // 睡眠時間

                break;
            }
            case EnterHealth.MODE_WEIGHT:{ // 体重
                // １週間前の体重を取得
                double weights[] = DataManager.getWeight(cToday - 7, cToday);
                if(weights[0]!=0) {//1週間前の体重がある
                    // 差分
                    double var = weights[7] - weights[0];
                    // 2kg以上の増加でToast自動表示
                    if (var < 2.0) return;
                    message = String.format("先週よりも%.1fkg増えてしまいました…\n健康のために生活習慣の改善を目指しましょう！", var);
                    // Toast表示
                    Popup p = new Popup();
                    LayoutInflater layoutInflater = getLayoutInflater();
                    p.pop(message, layoutInflater);
                }
            }
        }
    }
}
