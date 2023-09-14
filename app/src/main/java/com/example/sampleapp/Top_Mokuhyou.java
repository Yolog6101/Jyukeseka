package com.example.sampleapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Top_Mokuhyou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_mokuhyou);

        //ボタンの設定
        Button gendai = findViewById(R.id.tpm_JAN);
        Button kobun = findViewById(R.id.tpm_OLD);
        Button kanbun = findViewById(R.id.tpm_CHI);
        Button english = findViewById(R.id.tpm_ENG);
        Button math = findViewById(R.id.tpm_MAT);
        Button chemistry = findViewById(R.id.tpm_CHE);
        Button physics = findViewById(R.id.tpm_PHYS);
        Button biology = findViewById(R.id.tpm_BIO);
        Button groundsci = findViewById(R.id.tpm_GRO);
        Button japanhistory = findViewById(R.id.tpm_JANH);
        Button worldhistory = findViewById(R.id.tpm_WORH);
        Button geography = findViewById(R.id.tpm_WOR);
        Button govermenteconomy = findViewById(R.id.tpm_ECO);
        Button philosophy = findViewById(R.id.tpm_PHI);
        Button other = findViewById(R.id.tpm_OTH);
        Button gotoTop = findViewById(R.id.tps_button4);

        //現代文を選択
        gendai.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //  System.out.println("現代文を選択");
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","現代文");
                startActivity(intent);
            }
        });
        //古文を選択
        kobun.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","古文");
                startActivity(intent);
            }
        });
        //漢文を選択
        kanbun.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","漢文");
                startActivity(intent);
            }
        });
        //英語を選択
        english.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","英語");
                startActivity(intent);
            }
        });
        //数学を選択
        math.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","数学");
                startActivity(intent);
            }
        });
        //化学を選択
        chemistry.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","化学");
                startActivity(intent);
            }
        });
        //物理を選択
        physics.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","物理");
                startActivity(intent);
            }
        });
        //生物を選択
        biology.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","生物");
                startActivity(intent);
            }
        });
        //地学を選択
        groundsci.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","地学");
                startActivity(intent);
            }
        });
        //日本史を選択
        japanhistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","日本史");
                startActivity(intent);
            }
        });
        //世界史を選択
        worldhistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","世界史");
                startActivity(intent);
            }
        });
        //地理を選択
        geography.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","地理");
                startActivity(intent);
            }
        });
        //政治経済を選択
        govermenteconomy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","政治経済");
                startActivity(intent);
            }
        });
        //倫理を選択
        philosophy.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","倫理");
                startActivity(intent);
            }
        });
        //その他を選択
        other.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Enter_Mokuhyou.class);
                intent.putExtra("SUBJECT","その他");
                startActivity(intent);
            }
        });
        //トップへを選択
        gotoTop.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Activity_Main.class);
                startActivity(intent);
            }
        });
    }

    // 戻るボタン入力
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), Activity_Main.class);
        startActivity(intent);
    }
}
