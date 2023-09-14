package com.example.sampleapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context.*;

public class DataManager {
    // 定数
    private static final String DIRECTORY = "";
    private static final String APPNAME = "OurApp";
    private static final String EXT = ".data";
    private static final String FILENAME_MISC = DIRECTORY + APPNAME + "_Data_Misc" + EXT; // 雑情報
    private static final String FILENAME_GOALS = DIRECTORY + APPNAME + "_Data_Goals" + EXT; // 長期・短期目標
    private static final String FILENAME_EXAMS = DIRECTORY + APPNAME + "_Data_Exams" + EXT; // 模試結果
    private static final String FILENAME_STUDY_TIME = DIRECTORY + APPNAME + "_Data_StudyTime" + EXT; // 勉強時間
    private static final String FILENAME_EXERCISE_TIME = DIRECTORY + APPNAME + "_Data_ExerciseTime" + EXT; // 運動時間
    private static final String FILENAME_SLEEP_TIME = DIRECTORY + APPNAME + "_Data_SleepTime" + EXT; // 睡眠時間
    private static final String FILENAME_WEIGHT = DIRECTORY + APPNAME + "_Data_Weight" + EXT; // 体重
    private static final int NUM_SUBJECTS = 15;
    private static final int NUM_LONGTERM_GOALS = 6;
    private static final int NUM_SHORTTERM_GOALS = 3;

    // 内部クラス
    // 長期・短期目標
    static private class SubjectGoals implements Serializable{
        public String[] longTermGoals;
        public String[] shortTermGoals;
        public boolean[] longTermAchievements;
        public boolean[] shortTermAchievements;
        public int[] longTermDate;
        public int[] shortTermDate;

        // コンストラクタ
        public SubjectGoals() {
            longTermGoals = new String[NUM_LONGTERM_GOALS];
            longTermAchievements = new boolean[NUM_LONGTERM_GOALS];
            longTermDate=new int[NUM_LONGTERM_GOALS];
            for(int i = 0; i < longTermGoals.length; i++){
                longTermGoals[i] = "";
                longTermAchievements[i] = false;
                longTermDate[i]=0;
            }
            shortTermGoals = new String[NUM_SHORTTERM_GOALS];
            shortTermAchievements = new boolean[NUM_SHORTTERM_GOALS];
            shortTermDate=new int[NUM_LONGTERM_GOALS];
            for(int i = 0; i < shortTermGoals.length; i++) {
                shortTermGoals[i] = "";
                shortTermAchievements[i] = false;
                shortTermDate[i]=0;
            }
        }
    }

    // 模試結果
    static private class ExamResults implements Serializable{
        public class Subject implements Serializable{
            String name;
            int score;
            double deviation;

            public Subject(String name, int score, double deviation) {
                this.name = name;
                this.score = score;
                this.deviation = deviation;
            }
        }

        public String name;
        public Subject[] subjects;

        // コンストラクタ
        public ExamResults(String examName, String[] subjects, int[] scores, double[] deviations) {
            // 模試名
            this.name = examName;
            // 配列長
            int size = Math.max(Math.max(subjects.length, scores.length), deviations.length);
            // 要素を確保
            this.subjects = new Subject[size];
            // 要素ごとに生成
            for(int i = 0; i < size; i++) {
                String name = i < subjects.length ? subjects[i] : "-";
                int score = i < scores.length ? scores[i] : -1;
                double dev = i < deviations.length ? deviations[i] : -1.0;
                this.subjects[i] = new Subject(name, score, dev);
            }
        }
        // 結果を文字列で返す
        public String toString() {
            StringBuffer sb = new StringBuffer("");
            sb.append(name);
            for(Subject sbj : subjects) {
                sb.append("\r\n");
                sb.append(" ");
                sb.append(String.format("%s　　　%3d点　　　%3.1f",sbj.name, sbj.score, sbj.deviation));
            }
            return sb.toString();
        }
    }

    // 小数値配列(Array)（勉強時間・運動時間・睡眠時間・体重に使用）
    static private class DecimalsArray implements Serializable{
        public ArrayList<Double> list;

        // コンストラクタ
        public DecimalsArray(){
            list = new ArrayList<Double>();
        }
    }

    // 変数
    private static Context appContext;

    // アプリケーションの起動を通知
    static public void notifyRun(Context appCont){
        // コンテキストを保存
        appContext = appCont;
        // ファイルの存在を確認
        File file = appContext.getFileStreamPath(FILENAME_MISC);
        if(!file.exists()) {
            // 初めて起動した場合
            ObjectOutputStream oos;
            try {
                // ファイル作成
                oos = new ObjectOutputStream(appContext.openFileOutput(FILENAME_MISC, MODE_PRIVATE));
                // 初起動時の日付を記録
                oos.writeInt(calendarToInt(Calendar.getInstance()));
                // ファイルクローズ
                oos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    // 長期目標のデータを返す
    static public String getLongTermGoal(int subject, int index){
        // ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        // 返す
        return temp != null ? temp[subject].longTermGoals[index] : "";
    }

    // 長期目標の達成フラグを返す
    static public boolean getLongTermAchievement(int subject, int index){
        // ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        // 返す
        return temp != null ? temp[subject].longTermAchievements[index] : false;
    }

    //長期目標の登録日(初起動日からの日数)を返す(初起動などデータがない場合は呼び出した時(初起動日からの日数)を入力)
    static public int getLongTermDate(int subject, int index){
        // ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        // 返す
        return temp != null ? temp[subject].longTermDate[index] : getCurrentDateCount();
    }

    // 短期目標のデータを返す
    static public String getShortTermGoal(int subject, int index){
        // ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        // 返す
        return temp != null ? temp[subject].shortTermGoals[index] : "";
    }

    // 短期目標の達成フラグを返す
    static public boolean getShortTermAchievement(int subject, int index){
        // ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        // 返す
        return temp != null ? temp[subject].shortTermAchievements[index] : false;
    }

    //短期目標の登録日(初起動日からの日数)を返す(初起動などデータがない場合は呼び出した時(初起動日からの日数)を入力)
    static public int getShortTermDate(int subject, int index){
        // ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        // 返す
        return temp != null ? temp[subject].shortTermDate[index] : getCurrentDateCount();
    }

    // 登録された模試の数を返す
    static public int getNumExams(){
        // ファイルをロード
        ExamResults[] temp = loadExamResults();
        // 返す
        return temp != null ? temp.length : 0;
    }

    // 模試成績の文字列を返す
    static public String getExamResult(int index){
        // ファイルをロード
        ExamResults[] temp = loadExamResults();
        // 返す
        return temp != null ? temp[index].toString() : "";
    }

    // 勉強時間を返す
    static public double[] getStudyTime(int subject, int dateBegin, int dateEnd){
        // 勉強時間配列を取得
        DecimalsArray[] subjects = loadStudyTime();
        DecimalsArray arr = subjects == null ? new DecimalsArray() : subjects[subject];
        // 日付範囲を調節
        dateBegin = Math.min(dateBegin, dateEnd);
        // 配列を確保
        int size = dateEnd - dateBegin + 1;//データが登録されている日数
        System.out.println(size);
        double[] temp = new double[size];
        // 配列の要素をコピー
        for(int i = 0; i < size; i++) {
            int index = i + dateBegin;
            temp[i] = (index >= 0 && index < arr.list.size()) ? arr.list.get(index).doubleValue() : 0.0;
        }
        return temp;
    }

    // 運動時間を返す
    static public double[] getExerciseTime(int dateBegin, int dateEnd){
        // 運動時間配列を取得
        DecimalsArray arr = loadDecimalsArray(FILENAME_EXERCISE_TIME);
        if(arr == null) arr = new DecimalsArray();
        // 日付範囲を調節
        dateBegin = Math.min(dateBegin, dateEnd);
        // 配列を確保
        int size = dateEnd - dateBegin + 1;
        double[] temp = new double[size];
        // 配列の要素をコピー
        for(int i = 0; i < size; i++) {
            int index = i + dateBegin;
            temp[i] = (index >= 0 && index < arr.list.size()) ? arr.list.get(index).doubleValue() : 0.0;
        }
        return temp;
    }

    // 睡眠時間を返す
    static public double[] getSleepTime(int dateBegin, int dateEnd){
        // 運動時間配列を取得
        DecimalsArray arr = loadDecimalsArray(FILENAME_SLEEP_TIME);
        if(arr == null) arr = new DecimalsArray();
        // 日付範囲を調節
        dateBegin = Math.min(dateBegin, dateEnd);
        // 配列を確保
        int size = dateEnd - dateBegin + 1;
        double[] temp = new double[size];
        // 配列の要素をコピー
        for(int i = 0; i < size; i++) {
            int index = Math.min(i + dateBegin, arr.list.size() - 1);
            temp[i] = index >= 0 ? arr.list.get(index).doubleValue() : 0.0;
        }
        return temp;
    }

    // 体重を返す
    static public double[] getWeight(int dateBegin, int dateEnd){
        // 運動時間配列を取得
        DecimalsArray arr = loadDecimalsArray(FILENAME_WEIGHT);
        if(arr == null) arr = new DecimalsArray();
        // 日付範囲を調節
        dateBegin = Math.min(dateBegin, dateEnd);
        // 配列を確保
        int size = dateEnd - dateBegin + 1;
        double[] temp = new double[size];
        // 配列の要素をコピー
        for(int i = 0; i < size; i++) {
            int index = Math.min(i + dateBegin, arr.list.size() - 1);
            temp[i] = index >= 0 ? arr.list.get(index).doubleValue() : 0.0;
        }
        return temp;
    }

    // 長期目標を書き込む
    static public void setLongTermGoal(int subject, int index, String str){
        // indexが規定値を超えていないか判定
        if(index >= NUM_LONGTERM_GOALS) return;
        // 目標ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        if(temp == null) {
            temp = new SubjectGoals[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++) temp[i] = new SubjectGoals();
        }
        // 内容を更新
        temp[subject].longTermGoals[index] = str;
        // ファイルをセーブ
        saveSubjectGoals(temp);
    }

    // 長期目標の達成フラグを書き込む
    static public void setLongTermAchievement(int subject, int index, boolean achieve){
        // indexが規定値を超えていないか判定
        if(index >= NUM_LONGTERM_GOALS) return;
        // 目標ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        if(temp == null) {
            temp = new SubjectGoals[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++) temp[i] = new SubjectGoals();
        }
        // 内容を更新
        temp[subject].longTermAchievements[index] = achieve;
        // ファイルをセーブ
        saveSubjectGoals(temp);
    }

    //長期目標の登録日を書き込む
    static  public void setLongTermDate(int subject,int index,int date){
        // indexが規定値を超えていないか判定
        if(index >= NUM_LONGTERM_GOALS) return;
        // 目標ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        if(temp == null) {
            temp = new SubjectGoals[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++) temp[i] = new SubjectGoals();
        }
        // 内容を更新
        temp[subject].longTermDate[index] = date;
        // ファイルをセーブ
        saveSubjectGoals(temp);
    }

    // 短期目標を書き込む
    static public void setShortTermGoal(int subject, int index, String str){
        // indexが規定値を超えていないか判定
        if(index >= NUM_LONGTERM_GOALS) return;
        // 目標ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        if(temp == null) {
            temp = new SubjectGoals[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++) temp[i] = new SubjectGoals();
        }
        // 内容を更新
        temp[subject].shortTermGoals[index] = str;
        // ファイルをセーブ
        saveSubjectGoals(temp);
    }

    // 短期目標の達成フラグを書き込む
    static public void setShortTermAchievement(int subject, int index, boolean achieve){
        // indexが規定値を超えていないか判定
        if(index >= NUM_SHORTTERM_GOALS) return;
        // 目標ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        if(temp == null) {
            temp = new SubjectGoals[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++) temp[i] = new SubjectGoals();
        }
        // 内容を更新
        temp[subject].shortTermAchievements[index] = achieve;
        // ファイルをセーブ
        saveSubjectGoals(temp);
    }

    //短期目標の登録日を書き込む
    static public void setShortTermDate(int subject, int index, int date){
        // indexが規定値を超えていないか判定
        if(index >= NUM_SHORTTERM_GOALS) return;
        // 目標ファイルをロード
        SubjectGoals[] temp = loadSubjectGoals();
        if(temp == null) {
            temp = new SubjectGoals[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++) temp[i] = new SubjectGoals();
        }
        // 内容を更新
        temp[subject].shortTermDate[index] = date;
        // ファイルをセーブ
        saveSubjectGoals(temp);
    }

    // 模試成績を書き込む
    static public void writeExamResult(String examName, String[] subjects, int[] scores, double[] deviations){
        // 新たな要素
        ExamResults temp = new ExamResults(examName, subjects, scores, deviations);
        // 保存
        saveExamResults(temp);
    }

    // 勉強時間を書き込む
    static public void writeStudyTime(int subject, double time){
        // 配列をロード
        DecimalsArray[] temp = loadStudyTime();
        if(temp == null) {
            temp = new DecimalsArray[NUM_SUBJECTS];
            for(int i = 0; i < temp.length; i++)
                temp[i] = new DecimalsArray();
        }
        // 配列
        DecimalsArray sbj = temp[subject];
        // 今日の日付カウント
        int cToday = getCurrentDateCount();
        // カウントと配列サイズを揃える
        while(cToday >= sbj.list.size()) {
            sbj.list.add(new Double(0.0));
        }
        // 今日の分に加算
        sbj.list.set(cToday, new Double(sbj.list.get(cToday).doubleValue() + time));
        // 保存
        saveStudyTime(temp);
    }

    // 運動時間を書き込む
    static public void writeExerciseTime(double time){
        // 配列をロード
        DecimalsArray temp = loadDecimalsArray(FILENAME_EXERCISE_TIME);
        if(temp == null) temp = new DecimalsArray();
        // 今日の日付カウント
        int cToday = getCurrentDateCount();
        // カウントと配列サイズを揃える
        while(cToday >= temp.list.size()) {
            temp.list.add(new Double(0.0));
        }
        // 今日の分に加算
        temp.list.set(cToday, new Double(temp.list.get(cToday).doubleValue() + time));
        // 保存
        saveDecimalsArray(temp, FILENAME_EXERCISE_TIME);
    }

    // 睡眠時間を書き込む
    static public void writeSleepTime(double time){
        // 配列をロード
        DecimalsArray temp = loadDecimalsArray(FILENAME_SLEEP_TIME);
        if(temp == null) temp = new DecimalsArray();
        // 今日の日付カウント
        int cToday = getCurrentDateCount();
        // カウントと配列サイズを揃える
        double lastSleepTime = temp.list.size() > 0 ? temp.list.get(temp.list.size() - 1) : 0.0;
        while(cToday >= temp.list.size()) {
            temp.list.add(new Double(lastSleepTime));
        }
        // 今日の分を更新
        temp.list.set(cToday, new Double(time));
        // 保存
        saveDecimalsArray(temp, FILENAME_SLEEP_TIME);
    }

    // 体重を書き込む
    static public void writeWeight(double weight){
        // 配列をロード
        DecimalsArray temp = loadDecimalsArray(FILENAME_WEIGHT);
        if(temp == null) temp = new DecimalsArray();
        // 今日の日付カウント
        int cToday = getCurrentDateCount();
        // カウントと配列サイズを揃える
        double lastWeight = temp.list.size() > 0 ? temp.list.get(temp.list.size() - 1) : 0.0;
        while(cToday >= temp.list.size()) {
            temp.list.add(new Double(lastWeight));
        }
        // 今日の分を更新
        temp.list.set(cToday, new Double(weight));
        // 保存
        saveDecimalsArray(temp, FILENAME_WEIGHT);
    }

    // 今日が初起動時から何日目かを返す
    static public int getCurrentDateCount() {
        // 初起動時の日付
        Calendar date = intToCalendar(loadOriginalDate());
        // 今日の日付
        Calendar today = Calendar.getInstance();
        // 加算によって確認
        int count = 0;
        while(calendarToInt(date) < calendarToInt(today)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            count++;
        }
        return count;
    }

    // Calendarをintに変換
    static private int calendarToInt(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;//ひと月短く出力されるため
        int date = cal.get(Calendar.DATE);
        return date + month * 100 + year * 10000;
    }

    // intをCalendarに変換
    static private Calendar intToCalendar(int date) {
        Calendar temp = Calendar.getInstance();
        temp.set(date / 10000, (date % 10000) / 100, date % 100, 0, 0, 0);
        return temp;
    }

    // 初起動時の日付をロード
    static private int loadOriginalDate() {
        try {
            // ファイルオープン
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(FILENAME_MISC));
            // ロード
            int date = ois.readInt();
            // ファイルクローズ
            ois.close();
            // 戻り値
            return date;
        } catch (Exception e) {
            return calendarToInt(Calendar.getInstance());
        }
    }

    // 目標ファイルをロード
    static private SubjectGoals[] loadSubjectGoals() {
        try {
            // ファイルオープン
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(FILENAME_GOALS));
            // 科目数だけ確保
            SubjectGoals[] temp = new SubjectGoals[NUM_SUBJECTS];
            // ロード
            for(int i = 0; i < temp.length; i++) {
                temp[i] = (SubjectGoals)ois.readObject();
            }
            // ファイルクローズ
            ois.close();
            // 戻り値
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    // 目標ファイルをセーブ
    static private void saveSubjectGoals(SubjectGoals[] target) {
        try {
            // ファイルオープン
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(FILENAME_GOALS, MODE_PRIVATE));
            // セーブ
            for(int i = 0; i < target.length; i++) {
                oos.writeObject(target[i]);
            }
            // ファイルクローズ
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 模試結果をロード
    static private ExamResults[] loadExamResults() {
        try {
            // ファイルオープン
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(FILENAME_EXAMS));
            // 模試数
            int num = ois.readInt();
            // 模試数だけ確保
            ExamResults[] temp = new ExamResults[num];
            // ロード
            for(int i = 0; i < temp.length; i++) {
                temp[i] = (ExamResults)ois.readObject();
            }
            // ファイルクローズ
            ois.close();
            // 戻り値
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    // 模試結果をセーブ
    static private void saveExamResults(ExamResults target) {
        // 過去の模試結果
        ExamResults[] exams = loadExamResults();
        try {
            // ファイルオープン
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(FILENAME_EXAMS, MODE_PRIVATE));
            // 模試数
            int num = exams != null ? exams.length + 1 : 1;
            oos.writeInt(num);
            // セーブ
            for(int i = 0; i < num - 1; i++)
                oos.writeObject(exams[i]);
            oos.writeObject(target);
            // ファイルクローズ
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //d番目の模試データを削除
    static public void changeExamResults(int d) {
        // 過去の模試結果
        ExamResults[] exams = loadExamResults();
        try {
            // ファイルオープン
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(FILENAME_EXAMS, MODE_PRIVATE));
            //d番目データの削除(Array→Listにして削除→Array)
            List<ExamResults> exams_l=new ArrayList<>(Arrays.asList(exams));
            exams_l.remove(d);
            ExamResults[] exams2=exams_l.toArray(new ExamResults[0]);
            // 模試数
            int num = exams2 != null ? exams2.length : 1;
            oos.writeInt(num);
            // セーブ
            for(int i = 0; i < num; i++) {
                oos.writeObject(exams2[i]);
            }
            // ファイルクローズ
            System.out.println(oos);
            oos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 勉強時間をロード
    static private DecimalsArray[] loadStudyTime() {
        try {
            // ファイルオープン
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(FILENAME_STUDY_TIME));
            // 科目数だけ確保
            DecimalsArray[] temp = new DecimalsArray[NUM_SUBJECTS];
            // ロード
            for(int i = 0; i < temp.length; i++) {
                temp[i] = (DecimalsArray)ois.readObject();
            }
            // ファイルクローズ
            ois.close();
            // 戻り値
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    // 勉強時間をセーブ
    static private void saveStudyTime(DecimalsArray[] target) {
        try {
            // ファイルオープン
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(FILENAME_STUDY_TIME, MODE_PRIVATE));
            // セーブ
            for(int i = 0; i < target.length; i++) {
                oos.writeObject(target[i]);
            }
            // ファイルクローズ
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 運動時間・睡眠時間・体重をロード
    static private DecimalsArray loadDecimalsArray(String filename) {
        try {
            // ファイルオープン
            ObjectInputStream ois = new ObjectInputStream(appContext.openFileInput(filename));
            // ロード
            DecimalsArray temp = (DecimalsArray)ois.readObject();
            // ファイルクローズ
            ois.close();
            // 戻り値
            return temp;
        } catch (Exception e) {
            return null;
        }
    }

    // 運動時間・睡眠時間・体重をセーブ
    static private void saveDecimalsArray(DecimalsArray target, String filename) {
        try {
            // ファイルオープン
            ObjectOutputStream oos = new ObjectOutputStream(appContext.openFileOutput(filename, MODE_PRIVATE));
            // セーブ
            oos.writeObject(target);
            // ファイルクローズ
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
