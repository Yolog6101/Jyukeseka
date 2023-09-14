package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;

public class GraphHealth extends AppCompatActivity implements View.OnClickListener {

    // 定数
    private static final int LINE_COLOR = Color.rgb(240, 160, 10);//各項目に対するグラフの色
    //spanに対する定数
    private static final int SPAN_DAY = 0;
    private static final int SPAN_WEEK = 1;
    private static final int SPAN_MONTH = 2;
    //「日(SPAN_DAY)/週(SPAN_WEEK)/月(SPAN_MONTH)」に対する日数「1日(SPAN_DAY)/7日(SPAN_WEEK)/30日(SPAN_MONTH)」
    private static final int[] SUM_DAYS = { 1, 7, 30 };
    //表示範囲変更に伴い１目盛につき追加・減少する日数=「日(SPAN_DAY)/週(SPAN_WEEK)/月(SPAN_MONTH)」に対する日数「1日(SPAN_DAY)/7日(SPAN_WEEK)/30日(SPAN_MONTH)」
    private static final int[] INC_DAYS = { 1, 7, 30 };
    //最大取得日数「7日(SPAN_DAY)/35日=5週(SPAN_WEEK)/150日≒21週≒4月(SPAN_MONTH)」
    private static final int[] RANGE_DAYS = { 7, 35, 150 };

    // 内部クラス
    private static class DateFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (String)entry.getData();
        }
    }

    // 変数
    private static int mode = EnterHealth.MODE_EXERCISE;
    private static int span = SPAN_DAY;
    private static int latestDateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_healthmemo);

        // ボタン有効化
        findViewById(R.id.ghm_button1).setOnClickListener(this);
        findViewById(R.id.ghm_button2).setOnClickListener(this);
        findViewById(R.id.ghm_button3).setOnClickListener(this);
        findViewById(R.id.ghm_radio1).setOnClickListener(this);
        findViewById(R.id.ghm_radio2).setOnClickListener(this);
        findViewById(R.id.ghm_radio3).setOnClickListener(this);
        findViewById(R.id.ghm_radio4).setOnClickListener(this);
        findViewById(R.id.ghm_radio5).setOnClickListener(this);
        findViewById(R.id.ghm_radio6).setOnClickListener(this);
        // グラフ設定を初期化
        initGraphSettings();
    }
    @Override
    protected void onStart(){
        super.onStart();
        // 変数を初期化
        mode = EnterHealth.MODE_EXERCISE;
        span = SPAN_DAY;
        // 今日を最右点に設定
        latestDateCount = DataManager.getCurrentDateCount();
        //ラジオボタン初期化
        RadioButton radio1 = findViewById(R.id.ghm_radio1);
        radio1.setChecked(true);
        RadioButton radio4 = findViewById(R.id.ghm_radio4);
        radio4.setChecked(true);
        // テキスト初期化
        ((TextView)findViewById(R.id.textView21)).setText("日");
        // 描画
        updateGraph();
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.ghm_button1){
            gotoTopHealthScreen();
        }
        if(view.getId()==R.id.ghm_button2){//←
            latestDateCount -= INC_DAYS[span];
            if(latestDateCount < 0) latestDateCount = 0;
            updateGraph();
        }
        if(view.getId()==R.id.ghm_button3){//→
            latestDateCount += INC_DAYS[span];
            int cToday = DataManager.getCurrentDateCount();
            if(latestDateCount > cToday) latestDateCount = cToday;
            updateGraph();
        }
        if(view.getId()==R.id.ghm_radio1){//運動時間
            mode=EnterHealth.MODE_EXERCISE;
            updateGraph();
        }
        if(view.getId()==R.id.ghm_radio2){//睡眠時間
            mode = EnterHealth.MODE_SLEEP;
            updateGraph();
        }
        if(view.getId()==R.id.ghm_radio3){//体重
            mode = EnterHealth.MODE_WEIGHT;
            updateGraph();
        }
        if(view.getId()==R.id.ghm_radio4){//日
            span = SPAN_DAY;
            ((TextView)findViewById(R.id.textView21)).setText("日");
            updateGraph();
        }
        if(view.getId()==R.id.ghm_radio5){//週
            span = SPAN_WEEK;
            ((TextView)findViewById(R.id.textView21)).setText("週");
            updateGraph();
        }
        if(view.getId()==R.id.ghm_radio6){//月
            span = SPAN_MONTH;
            ((TextView)findViewById(R.id.textView21)).setText("月");
            updateGraph();
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
    // グラフを描画
    private void updateGraph(){
        // グラフインスタンス
        LineChart chart = (LineChart)findViewById(R.id.ghm_line_chart);
        // グラフデータ
        ArrayList<Entry> entries = generateEntries();
        // 線の表示設定
        String[] labels = {"運動時間(分)", "睡眠時間(時間)", "体重(kg)"};
        LineDataSet dataSet = new LineDataSet(entries, labels[mode]);
        dataSet.setColor(LINE_COLOR);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(2f);
        // 日付の表示設定
        LineData data = new LineData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(10);
        data.setValueFormatter(new DateFormatter());
        // 縦軸範囲
        chart.getAxisLeft().setAxisMinimum(0.0f);
        chart.getAxisLeft().setAxisMaximum(Math.max(1.0f, data.getYMax() * 1.2f));
        chart.getAxisLeft().setLabelCount(6, true);
        // データを適用
        chart.setData(data);
        // 描画
        chart.invalidate();
    }
    // エントリリストを生成
    private ArrayList<Entry> generateEntries(){
        // リスト
        ArrayList<Entry> entries = new ArrayList<Entry>();
        // データを取得
        int begin = latestDateCount - RANGE_DAYS[span] + 1;
        int end = latestDateCount;
        double[] arr = null;
        switch(mode){
            case EnterHealth.MODE_EXERCISE: arr = DataManager.getExerciseTime(begin, end); break;
            case EnterHealth.MODE_SLEEP: arr = DataManager.getSleepTime(begin, end); break;
            case EnterHealth.MODE_WEIGHT: arr = DataManager.getWeight(begin, end); break;
        }
        // 今日の日付カウント
        int cToday = DataManager.getCurrentDateCount();
        for(int i = 0; i < arr.length / SUM_DAYS[span]; i++){
            // 期間の平均をとる
            float avr = 0.0f;
            for(int j = 0; j < SUM_DAYS[span]; j++) avr += arr[i * SUM_DAYS[span] + j];
            avr /= (float)Math.min(SUM_DAYS[span], cToday + 1);
            // エントリを生成
            Entry entry = new Entry((float)i / (arr.length / SUM_DAYS[span] - 1), avr, "");
            // 日付表示
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, begin + i * SUM_DAYS[span] - cToday);
            switch(span) {
                case SPAN_DAY: {
                    entry.setData(String.format("%02d/%02d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
                    break;
                }
                case SPAN_WEEK: {
                    entry.setData(String.format("%02d/%02d～", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
                }
                case SPAN_MONTH: {
                    entry.setData(String.format("%02d/%02d～", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
                    break;
                }
            }
            // 追加
            entries.add(entry);
        }
        System.out.println(arr);
        return entries;
    }
    // グラフの諸設定を初期化
    private void initGraphSettings(){
        // グラフインスタンス
        LineChart chart = (LineChart)findViewById(R.id.ghm_line_chart);
        // 背景色
        chart.setBackgroundColor(Color.WHITE);
        // 説明を空に
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        // 縦軸
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);
        // 横軸
        chart.getXAxis().setDrawLabels(false);
        chart.getXAxis().setAxisMinimum(0.0f);
        chart.getXAxis().setAxisMaximum(1.0f);
    }
}
