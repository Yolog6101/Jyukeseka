package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutionException;

public class EnterPassword extends AppCompatActivity implements View.OnClickListener {
    // 定数
    static public final int MIN_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_password);

        // 初期処理
        DataManager.notifyRun(getApplicationContext());
        Settings.notifyRun(getApplicationContext());

        // ボタン入力を有効に
        findViewById(R.id.eps_button1).setOnClickListener(this);
    }

    // 開始
    @Override
    protected void onStart(){
        super.onStart();

        // パスワード設定済みか判定
        if(Settings.isPasswordSet()){
            ((TextView)findViewById(R.id.eps_text1)).setText("パスワードを入力してください");
            ((EditText)findViewById(R.id.eps_edit1)).setText("");
            ((TextView)findViewById(R.id.eps_text2)).setVisibility(View.INVISIBLE);
            ((EditText)findViewById(R.id.eps_edit2)).setVisibility(View.INVISIBLE);
        }
        else{
            ((TextView)findViewById(R.id.eps_text1)).setText("パスワードを設定してください");
            ((EditText)findViewById(R.id.eps_edit1)).setText("");
            ((TextView)findViewById(R.id.eps_text2)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.eps_edit2)).setVisibility(View.VISIBLE);
            ((EditText)findViewById(R.id.eps_edit2)).setText("");
        }
    }

    // ボタン入力
    @Override
    public void onClick(View view){
        if(view.getId()==R.id.eps_button1){
            // 入力を取得
            String input1 = ((EditText)findViewById(R.id.eps_edit1)).getText().toString();
            String input2 = ((EditText)findViewById(R.id.eps_edit2)).getText().toString();
            // パスワード設定済みか判定
            if(Settings.isPasswordSet()){
                // パスワード有効判定
                if(!Settings.checkPassword(input1)){
                    // メッセージを出す
                    Toast.makeText(getBaseContext(), "パスワードが正しくありません。", Toast.LENGTH_LONG).show();
                    //break;
                }
                else {
                    // 次の画面へ
                    gotoTopScreen();
                }
            }
            else{
                boolean registed=true;
                // 入力一致判定
                if(!input1.equals(input2)){
                    // メッセージを出す
                    Toast.makeText(getBaseContext(), "パスワードが一致しません。", Toast.LENGTH_LONG).show();
                   // break;
                    registed=false;
                }
                // 長さ不足判定
                if(input1.length() < MIN_PASSWORD_LENGTH){
                    // メッセージを出す
                    Toast.makeText(getBaseContext(), "パスワードは" + Integer.toString(MIN_PASSWORD_LENGTH) + "文字以上にしてください。", Toast.LENGTH_LONG).show();
                    //break;
                    registed=false;
                }
                //登録できる状態(registed=true)時
                if(registed) {
                    // 登録
                    Settings.setPassword(input2);
                    // 次の画面へ
                    gotoTopScreen();
                }
            }
        }
    }

    // トップへ移動
    private void gotoTopScreen(){
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
    }

}
