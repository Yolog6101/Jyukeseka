package com.example.sampleapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Enter_Mokuhyou extends AppCompatActivity {
    static private final int[] CHECKBOX_ID = {
            R.id.etm_check1,
            R.id.etm_check2,
            R.id.etm_check3,
            R.id.etm_check4,
            R.id.etm_check5,
            R.id.etm_check6,
            R.id.etm_check7,
            R.id.etm_check8,
    };
    static private final int[] EDIT_ID = {
            R.id.etm_edit1,
            R.id.etm_edit2,
            R.id.etm_edit3,
            R.id.etm_edit4,
            R.id.etm_edit5,
            R.id.etm_edit6,
            R.id.etm_edit7,
            R.id.etm_edit8,
    };

    int subjectnumber = 0;//科目の識別番号：現代文=0～その他=14
    int[] date=new int[8];//登録日(初起動日からの日数)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_mokuhyou);

        //ボタン・チェックボックス・EditTextの設定
        Button gotoTop = findViewById(R.id.topgo2);
        CheckBox[] check = new CheckBox[8];
        check[0] = findViewById(R.id.etm_check1);
        check[1] = findViewById(R.id.etm_check2);
        check[2] = findViewById(R.id.etm_check3);
        check[3] = findViewById(R.id.etm_check4);
        check[4] = findViewById(R.id.etm_check5);
        check[5] = findViewById(R.id.etm_check6);
        check[6] = findViewById(R.id.etm_check7);
        check[7] = findViewById(R.id.etm_check8);
        EditText[] text = new EditText[8];
        text[0] = findViewById(R.id.etm_edit1);
        text[1] = findViewById(R.id.etm_edit2);
        text[2] = findViewById(R.id.etm_edit3);
        text[3] = findViewById(R.id.etm_edit4);
        text[4] = findViewById(R.id.etm_edit5);
        text[5] = findViewById(R.id.etm_edit6);
        text[6] = findViewById(R.id.etm_edit7);
        text[7] = findViewById(R.id.etm_edit8);

        //選択した科目を受け付け
        Intent beforeintent = getIntent();
        String subject = beforeintent.getStringExtra("SUBJECT");

        TextView sub = findViewById(R.id.textView3);
        sub.setText(subject);

        switch (subject) {
            case "現代文":
                subjectnumber = 0;
                break;
            case "古文":
                subjectnumber = 1;
                break;
            case "漢文":
                subjectnumber = 2;
                break;
            case "英語":
                subjectnumber = 3;
                break;
            case "数学":
                subjectnumber = 4;
                break;
            case "化学":
                subjectnumber = 5;
                break;
            case "物理":
                subjectnumber = 6;
                break;
            case "生物":
                subjectnumber = 7;
                break;
            case "地学":
                subjectnumber = 8;
                break;
            case "日本史":
                subjectnumber = 9;
                break;
            case "世界史":
                subjectnumber = 10;
                break;
            case "地理":
                subjectnumber = 11;
                break;
            case "政治経済":
                subjectnumber = 12;
                break;
            case "倫理":
                subjectnumber = 13;
                break;
            case "その他":
                subjectnumber = 14;
                break;
            default:
                break;
        }

        //長期目標と達成フラグ、登録日(初起動日からの日数)をデータクラスから取得
        for(int i = 0; i < 6; i++){
            String str = DataManager.getLongTermGoal(subjectnumber, i);
            boolean status = DataManager.getLongTermAchievement(subjectnumber, i);
            int d=DataManager.getLongTermDate(subjectnumber,i);
            check[i].setChecked(status);
            text[i].setText(str);
            date[i]=d;
        }

        //短期目標と達成フラグ、登録日(初起動日からの日数)をデータクラスから取得
        for(int i = 6; i < 8; i++){
            String str = DataManager.getShortTermGoal(subjectnumber, i - 6);
            boolean status = DataManager.getShortTermAchievement(subjectnumber, i - 6);
            int d=DataManager.getShortTermDate(subjectnumber,i-6);
            date[i]=d;
            int today=DataManager.getCurrentDateCount();//今日(初起動日からの日数)
            if(d==today){
                //当日中のみ表示する(更新は終了処理にて)
                check[i].setChecked(status);
                text[i].setText(str);
            }
            else{
                check[i].setChecked(false);
                text[i].setText("");
            }
        }

        //チェックボックスの処理
        for(int i = 0; i < 8; i++){
            check[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
        }

        //科目選択に戻る
        gotoTop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(getApplication(), Top_Mokuhyou.class);
                startActivity(intent);
            }
        });
    }
    
    // 終了時処理
    @Override
    public void onPause(){
        super.onPause();

        // 長期目標
        for(int i = 0; i < 6; i++){
            CheckBox check = findViewById(CHECKBOX_ID[i]);
            EditText edit = findViewById(EDIT_ID[i]);
            DataManager.setLongTermGoal(subjectnumber, i, edit.getText().toString());
            DataManager.setLongTermAchievement(subjectnumber, i, check.isChecked());
            DataManager.setLongTermDate(subjectnumber,i,DataManager.getCurrentDateCount());
        }
        // 短期目標
        for(int i = 6; i < 8; i++){
            CheckBox check = findViewById(CHECKBOX_ID[i]);
            EditText edit = findViewById(EDIT_ID[i]);
            DataManager.setShortTermGoal(subjectnumber, i - 6, edit.getText().toString());
            DataManager.setShortTermAchievement(subjectnumber, i - 6, check.isChecked());
            DataManager.setShortTermDate(subjectnumber,i-6,DataManager.getCurrentDateCount());
        }
    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Top_Mokuhyou.class);
        startActivity(intent);
    }
}
