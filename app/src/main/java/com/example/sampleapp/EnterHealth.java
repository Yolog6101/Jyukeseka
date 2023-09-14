package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class EnterHealth extends AppCompatActivity implements View.OnClickListener {
    // 定数
    static public final int MODE_EXERCISE = 0;
    static public final int MODE_SLEEP = 1;
    static public final int MODE_WEIGHT = 2;

    // 変数
    static int inputMode = MODE_EXERCISE;
    static double inputValue = 0.0;

    // 入力項目を取得
    static public int getInputMode(){
        return inputMode;
    }
    // 入力値を取得
    static public double getInputValue(){
        return inputValue;
    }

    // 作成
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_healthmemo);

        // ボタン入力を受け付け
        findViewById(R.id.ehm_button1).setOnClickListener(this);
        findViewById(R.id.ehm_button2).setOnClickListener(this);
        findViewById(R.id.ehm_radio1).setOnClickListener(this);
        findViewById(R.id.ehm_radio2).setOnClickListener(this);
        findViewById(R.id.ehm_radio3).setOnClickListener(this);
    }

    // 開始
    @Override
    protected void onResume() {
        super.onResume();
        // コントロールに値を設定
        RadioButton radio = findViewById(R.id.ehm_radio1);
        RadioGroup rg = findViewById(R.id.ehm_radiogroup1);
        final int[] ids = { R.id.ehm_radio1, R.id.ehm_radio2, R.id.ehm_radio3 };
        rg.check(ids[inputMode]);
        // コントロールの有効化・無効化
        enableControls();
    }

    // ボタン入力
    @Override
    public void onClick(View view){
        if(view.getId()==R.id.ehm_button1) {
            gotoTopHealthScreen();
        }
        if(view.getId()==R.id.ehm_button2){
            if (!isInputValid()) {
                Toast.makeText(getBaseContext(), "数値を入力してください", Toast.LENGTH_LONG).show();
            }
            gotoTweetHealthScreen();
        }
        if(view.getId()==R.id.ehm_radio1){
            changeInputMode(view.getId());
        }
        if(view.getId()==R.id.ehm_radio2){
            changeInputMode(view.getId());
        }
        if(view.getId()==R.id.ehm_radio3){
            changeInputMode(view.getId());
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

    // 内容を共有
    private void gotoTweetHealthScreen(){
        //メイン画面に戻りたい
        Intent intent1 = new Intent(getApplication(), ShareHealth.class);
        startActivity(intent1);
    }

    // 入力項目を切り替え
    private void changeInputMode(int id){
        // モードを記憶
        if(id==R.id.ehm_radio1){
            inputMode = MODE_EXERCISE;
        }
        if(id==R.id.ehm_radio2){
            inputMode = MODE_SLEEP;
        }
        if(id==R.id.ehm_radio3){
            inputMode = MODE_WEIGHT;
        }
        // コントロールの有効化・無効化
        enableControls();
    }

    // 入力内容の有効判定
    private boolean isInputValid(){
        // モードで分岐
        EditText et = null;
        switch(inputMode){
            case MODE_EXERCISE: et = findViewById(R.id.ehm_edit1); break;
            case MODE_SLEEP: et = findViewById(R.id.ehm_edit2); break;
            case MODE_WEIGHT: et = findViewById(R.id.ehm_edit3); break;
        }
        // 文字列取得
        SpannableStringBuilder ssb = (SpannableStringBuilder)et.getText();
        String str = ssb.toString();
        // 実数に変換
        try{
            double value = Double.parseDouble(str);
            inputValue = value;
            System.out.println("Mode: " + inputMode);
            System.out.println("Value: " + inputValue);
        }
        catch(NumberFormatException e){
            // 無効ならばfalseを返す
            return false;
        }
        return true;
    }

    // コントロールの有効化・無効化
    private void enableControls(){
        findViewById(R.id.ehm_edit1).setEnabled(inputMode == MODE_EXERCISE);
        findViewById(R.id.ehm_edit2).setEnabled(inputMode == MODE_SLEEP);
        findViewById(R.id.ehm_edit3).setEnabled(inputMode == MODE_WEIGHT);
    }
}
