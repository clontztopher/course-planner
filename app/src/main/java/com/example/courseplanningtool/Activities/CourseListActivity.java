package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.courseplanningtool.Fragments.CourseListFragment;
import com.example.courseplanningtool.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseListActivity extends AppCompatActivity implements CourseListFragment.OnCourseSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_course_list);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Add floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButtonCourses);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CourseEditActivity.class);
            startActivity(intent);
        });

        // Add fragment for term list
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.courselist_fragment_container);

        if (fragment == null) {
            fragment = new CourseListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.courselist_fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onCourseSelected(long courseId) {
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, courseId);
        startActivity(intent);
    }
}