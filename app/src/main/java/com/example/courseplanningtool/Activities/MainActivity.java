package com.example.courseplanningtool.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.courseplanningtool.Activities.TermListActivity;
import com.example.courseplanningtool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_terms: {
                Intent intent = new Intent(this, TermListActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_courses: {
                Intent intent = new Intent(this, CourseListActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_assessments: {
                Intent intent = new Intent(this, AssessmentListActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }
}