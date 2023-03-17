package com.example.courseplanningtool.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.courseplanningtool.Activities.Assessment.AssessmentListActivity;
import com.example.courseplanningtool.Activities.Course.CourseListActivity;
import com.example.courseplanningtool.Activities.Term.TermListActivity;
import com.example.courseplanningtool.Data.TestData;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.example.courseplanningtool.Utility.AlertIntentService;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 200);
            return;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0) {
            AlertIntentService.startActionAlerts(getApplicationContext());
        }
    }
}