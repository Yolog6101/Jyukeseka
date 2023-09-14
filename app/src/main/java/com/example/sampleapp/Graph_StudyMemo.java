package com.example.sampleapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class Graph_StudyMemo extends AppCompatActivity implements OnClickListener{
    // 定数
    private static final int NUM_SUBJECTS = 15;
    private static final String[] SUBJECTS = {
            "現代文", "古文", "漢文",
            "英語", "数学", "化学", "物理", "生物", "地学",
            "日本史", "世界史", "地理",
            "政治経済", "倫理", "その他",
    };
    private static final int[] SUBJECT_COLORS = {
            Color.rgb(255, 75, 75), Color.rgb(255, 175, 200), Color.rgb(255, 128, 128),
            Color.rgb(180, 125, 255), Color.rgb(125, 200, 255),
            Color.rgb(50, 220, 180), Color.rgb(120, 150, 100), Color.rgb(170, 225, 0), Color.rgb(160, 130, 80),
            Color.rgb(180, 60, 60), Color.rgb(160, 160, 60), Color.rgb(60, 160, 160),
            Color.rgb(210, 170, 100), Color.rgb(180, 150, 190), Color.rgb(170, 170, 170),
    };
    private static final int LINE_COLOR = Color.rgb(240, 160, 10);

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
    private static int span = SPAN_DAY;//表示グラフ「日(SPAN_DAY)/週(SPAN_WEEK)/月(SPAN_MONTH)」
    private static int latestDateCount = 0;//日数

    // 内部クラス
    //private static class DateFormatter implements IValueFormatter{
    private static class DateFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (String)entry.getData();
        }
    }

    // 教科選択ダイアログ
    static public class DialogFragmentSubjects extends DialogFragment {
        static private boolean[] choise = null;

        static public boolean isChosen(int subjectID){
            if(choise == null)
                initChosenArray();
            return choise[subjectID];
        }

        static private void initChosenArray(){
            choise = new boolean[SUBJECTS.length];
            for(int i = 0; i < choise.length; i++){
                choise[i] = false;
            }
            choise[0] = true;
        }

        // 変数
        Graph_StudyMemo activity;

        // コンストラクタ
        public DialogFragmentSubjects(Graph_StudyMemo activity){
            this.activity = activity;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if(choise == null)
                initChosenArray();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("表示する教科を選択")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // グラフに反映
                            activity.updateGraph();
                        }
                    })
                    .setMultiChoiceItems(SUBJECTS, choise, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            choise[which] = isChecked;
                        }
                    });

            return builder.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_studymemo);

        // ボタン入力を受け付け
        findViewById(R.id.button10).setOnClickListener(this);
        findViewById(R.id.button11).setOnClickListener(this);
        findViewById(R.id.button12).setOnClickListener(this);
        findViewById(R.id.gsm_button_back).setOnClickListener(this);
        findViewById(R.id.radioButton1).setOnClickListener(this);
        findViewById(R.id.radioButton2).setOnClickListener(this);
        findViewById(R.id.radioButton3).setOnClickListener(this);

        //グラフ設定を初期化
        initGraphSettings();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //ラジオボタン初期化
        RadioButton radio1 = findViewById(R.id.radioButton1);
        radio1.setChecked(true);

        // 変数を初期化
        span = SPAN_DAY;
        // 今日を最右点に設定
        latestDateCount = DataManager.getCurrentDateCount();
        // 描画
        updateGraph();
    }

    public void onClick(View view) {
        if(view.getId()==R.id.button10){//←
            latestDateCount -= INC_DAYS[span];
            if(latestDateCount < 0) latestDateCount = 0;
            updateGraph();
        }
        if(view.getId()==R.id.button12){//→
            latestDateCount += INC_DAYS[span];
            int cToday = DataManager.getCurrentDateCount();
            if(latestDateCount > cToday) latestDateCount = cToday;
            updateGraph();
        }
        if(view.getId()==R.id.button11){//教科選択
            DialogFragment df = new DialogFragmentSubjects(this);
            df.show(getSupportFragmentManager(), "subjects");
        }
        if(view.getId()==R.id.gsm_button_back){
            gotoTopStudyScreen();
        }
        if(view.getId()==R.id.radioButton1){//日
            span = SPAN_DAY; updateGraph();

        }
        if(view.getId()==R.id.radioButton2){//週
            span = SPAN_WEEK; updateGraph();

        }
        if(view.getId()==R.id.radioButton3){//月
            span = SPAN_MONTH; updateGraph();

        }

    }
    @Override
    public void onBackPressed(){
        gotoTopStudyScreen();
    }

    private void gotoTopStudyScreen(){
        Intent intent = new Intent(this, Top_StudyMemo.class);
        startActivity(intent);
    }

    // グラフを描画
    private void updateGraph(){
        // グラフインスタンス
        LineChart chart = (LineChart)findViewById(R.id.bar_chart);
        // 今日の日付カウント
        int cToday = DataManager.getCurrentDateCount();
        // 教科の数のデータセット
        LineDataSet[] dataSets = new LineDataSet[NUM_SUBJECTS];
        // 日付表示フラグ
        boolean showDate = true;
        for(int i = 0; i < NUM_SUBJECTS; i++){
            // 表示しないならスキップ
            if(!DialogFragmentSubjects.isChosen(i)) continue;
            // グラフデータ
            ArrayList<Entry> entries = generateEntries(i, cToday, showDate);
            // 線の表示設定
            LineDataSet dataSet = new LineDataSet(entries, SUBJECTS[i]);
            dataSet.setColor(SUBJECT_COLORS[i]);
            dataSet.setDrawCircles(false);
            dataSet.setLineWidth(2f);
            dataSets[i] = dataSet;
            // 日付は最初の折れ線のみ表示
            showDate = false;
        }
        // リスト
        ArrayList<ILineDataSet> dataSetsArray = new ArrayList<>();
        for(int i = 0; i < dataSets.length; i++) if(DialogFragmentSubjects.isChosen(i)) dataSetsArray.add(dataSets[i]);
        // 日付の表示設定
        LineData data = new LineData(dataSetsArray);
        data.setDrawValues(true);
        data.setValueTextSize(10);
        data.setValueFormatter(new DateFormatter());
        // 縦軸範囲
        chart.getAxisLeft().setAxisMinimum(0.0f);
        chart.getAxisLeft().setAxisMaximum(Math.max(0.5f, data.getYMax() * 1.2f));
        chart.getAxisLeft().setLabelCount(6, true);
        // データを適用
        chart.setData(data);
        // 描画
        chart.invalidate();
    }

    // エントリリストを生成
    private ArrayList<Entry> generateEntries(int subjectID, int cToday, boolean showDate){
        // リスト
        ArrayList<Entry> entries = new ArrayList<Entry>();
        // データを取得
        int begin = latestDateCount - RANGE_DAYS[span] + 1;
        int end = latestDateCount;
        double[] arr = DataManager.getStudyTime(subjectID, begin, end);
        for(int i = 0; i < arr.length / SUM_DAYS[span]; i++){
            // 期間の累積をとる
            float sum = 0.0f;
            for(int j = 0; j < SUM_DAYS[span]; j++) sum += arr[i * SUM_DAYS[span] + j];
            // エントリを生成
            Entry entry = new Entry((float)i / (float)(arr.length / SUM_DAYS[span] - 1), sum, "");
            // 日付表示
            if(showDate){
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, begin + i * SUM_DAYS[span] - cToday);
                switch(span) {
                    case SPAN_DAY: {
                        entry.setData(String.format("%02d/%02d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
                        break;
                    }
                    case SPAN_WEEK:{
                        entry.setData(String.format("%02d/%02d～", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
                    }
                    case SPAN_MONTH: {
                        entry.setData(String.format("%02d/%02d～", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
                        break;
                    }
                }
            }
            // 追加
            entries.add(entry);
        }
        return entries;
    }

    // グラフの諸設定を初期化
    private void initGraphSettings(){
        // グラフインスタンス
        LineChart chart = (LineChart)findViewById(R.id.bar_chart);
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
