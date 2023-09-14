package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        findViewById(R.id.rsp_button1).setOnClickListener(this);
        findViewById(R.id.returnbut).setOnClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    // ボタン入力
    public void onClick(View view){
        if(view.getId()==R.id.rsp_button1){
            // 入力内容を取得
            String passOld = ((EditText)findViewById(R.id.rsp_edit1)).getText().toString();
            String passNew1 = ((EditText)findViewById(R.id.rsp_edit2)).getText().toString();
            String passNew2 = ((EditText)findViewById(R.id.rsp_edit3)).getText().toString();
            // 現在のパスワードが正しいか判定
            if(!Settings.checkPassword(passOld)){
                // メッセージを出す
                Toast.makeText(getBaseContext(), "現在のパスワードが正しくありません。", Toast.LENGTH_LONG).show();
                //break;
            }
            // 入力一致判定
            if(!passNew1.equals(passNew2)){
                // メッセージを出す
                Toast.makeText(getBaseContext(), "新しいパスワードが一致しません。", Toast.LENGTH_LONG).show();
                //break;
            }
            // 長さ不足判定
            if(passNew1.length() < EnterPassword.MIN_PASSWORD_LENGTH){
                // メッセージを出す
                Toast.makeText(getBaseContext(), "新しいパスワードは" + Integer.toString(EnterPassword.MIN_PASSWORD_LENGTH) + "文字以上にしてください。", Toast.LENGTH_LONG).show();
                //break;
            }
            // 登録
            Settings.setPassword(passNew1);
            // 設定入力画面へ
            gotoTopSettingScreen();
            //break;
        }
        if(view.getId()==R.id.returnbut){
            gotoTopSettingScreen();
        }
    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        gotoTopSettingScreen();
    }

    // 設定画面へ
    private void gotoTopSettingScreen(){
        Intent intent = new Intent(getApplication(), Top_Setting.class);
        startActivity(intent);
    }
}
