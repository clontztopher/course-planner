package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.courseplanningtool.Activities.TermListActivity;
import com.example.courseplanningtool.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleTermNavigation(View view) {
        Intent intent = new Intent(this, TermListActivity.class);
        startActivity(intent);
    }
}