//Toast出力
package com.example.sampleapp;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public  class Popup extends AppCompatActivity {
    protected  void pop(String text,LayoutInflater l){
        View customToastView = l.inflate(R.layout.toast, null);
        // TextViewのテキストを変更する
        ((TextView)customToastView.findViewById(R.id.msg)).setText(text);
        Toast toast = Toast.makeText(customToastView.getContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(customToastView);
        toast.show();
    }

}
