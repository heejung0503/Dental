package com.example.lafamila;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);


        Intent intent = getIntent();

        ArrayList<String> item = intent.getStringArrayListExtra("item");

        TextView tv = findViewById(R.id.MapList);
        String text = "";
        for(int i=0;i<item.size();i++){
            text += item.get(i) + "\n";
        }
        tv.setText(text);

    }
}
