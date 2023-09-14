package com.example.sampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.text.SpannableStringBuilder;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class EnterScore_StudyMemo extends AppCompatActivity implements OnClickListener {
    // 定数
    private static final int[] IDARRAY_EDITTEXT_SUBJECTS = {
            R.id.editTextTextPersonName10,
            R.id.editTextTextPersonName20,
            R.id.editTextTextPersonName23,
            R.id.editTextTextPersonName26,
            R.id.editTextTextPersonName29,
            R.id.editTextTextPersonName32,
            R.id.editTextTextPersonName37,
            R.id.editTextTextPersonName38,
    };
    private static final int[] IDARRAY_EDITTEXT_SCORES = {
            R.id.editTextTextPersonName11,
            R.id.editTextTextPersonName21,
            R.id.editTextTextPersonName24,
            R.id.editTextTextPersonName27,
            R.id.editTextTextPersonName30,
            R.id.editTextTextPersonName33,
            R.id.editTextTextPersonName36,
            R.id.editTextTextPersonName39,
    };
    private static final int[] IDARRAY_EDITTEXT_DEVIATIONS = {
            R.id.editTextTextPersonName12,
            R.id.editTextTextPersonName22,
            R.id.editTextTextPersonName25,
            R.id.editTextTextPersonName28,
            R.id.editTextTextPersonName31,
            R.id.editTextTextPersonName34,
            R.id.editTextTextPersonName35,
            R.id.editTextTextPersonName40,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterscore_studymemo);

        findViewById(R.id.enter_button).setOnClickListener(this);
        findViewById(R.id.essm_gototop).setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId()==R.id.enter_button){
            // 模試名
            String examName = ((EditText)findViewById(R.id.editTextTextPersonName9)).getText().toString();
            if(examName == null || examName.length() == 0) {//模試名が記入されていない場合→再入力を促す
                Toast.makeText(getBaseContext(), "模試名を記入してください", Toast.LENGTH_LONG).show();
            }
            else {
                // 入力項目数をカウント
                int count = 0;
                for (int i = 0; i < 8; i++) {
                    String name = ((EditText) findViewById(IDARRAY_EDITTEXT_SUBJECTS[i])).getText().toString();
                    if (name != null && name.length() > 0) count++;
                }
                // 配列を確保
                String[] subjectsNames = new String[count];
                int[] scores = new int[count];
                double[] deviations = new double[count];
                // 項目ごとに入力
                int j = 0;
                for (int i = 0; i < 8; i++) {
                    String name = ((EditText) findViewById(IDARRAY_EDITTEXT_SUBJECTS[i])).getText().toString();
                    if (name != null && name.length() > 0) {
                        subjectsNames[j] = name;
                        try {
                            scores[j] = Integer.parseInt(((EditText) findViewById(IDARRAY_EDITTEXT_SCORES[i])).getText().toString());
                        } catch (Exception e) {
                            scores[j] = 0;
                        }
                        try {
                            deviations[j] = Double.parseDouble(((EditText) findViewById(IDARRAY_EDITTEXT_DEVIATIONS[i])).getText().toString());
                        } catch (Exception e) {
                            deviations[j] = 0.0f;
                        }
                        j++;
                    }
                }
                // 記録
                DataManager.writeExamResult(examName, subjectsNames, scores, deviations);
                //勉強記録TOPに戻る
                Intent intent = new Intent(this, Top_StudyMemo.class);
                startActivity(intent);
            }
        }

        // 戻るボタン入力
        if(view.getId()==R.id.essm_gototop){
            Intent intent = new Intent(this, Top_StudyMemo.class);
            startActivity(intent);
        }
    }

    //Androidの戻るキーを無効化する
    OnBackPressedCallback c_back=new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            //戻るキーが押されても何もしないように設定
        }
    };


}

