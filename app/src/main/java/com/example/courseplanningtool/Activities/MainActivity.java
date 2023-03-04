package com.example.courseplanningtool.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.courseplanningtool.Activities.Assessment.AssessmentListActivity;
import com.example.courseplanningtool.Activities.Course.CourseListActivity;
import com.example.courseplanningtool.Activities.Term.TermListActivity;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
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
        return MainMenuProvider.navItemSelected(item, this);
    }
}