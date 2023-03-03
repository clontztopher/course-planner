package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.courseplanningtool.Fragments.CourseDetailsFragment;
import com.example.courseplanningtool.Fragments.TermDetailsFragment;
import com.example.courseplanningtool.R;

public class CourseDetailActivity extends AppCompatActivity implements CourseDetailsFragment.OnCourseDeletedListener {

    public static final String EXTRA_COURSE_ID = "courseId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_course_detail);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.course_details_fragment_container);

        if (fragment == null) {
            long termId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);
            fragment = CourseDetailsFragment.newInstance(termId);
            fragmentManager.beginTransaction()
                    .add(R.id.course_details_fragment_container, fragment)
                    .commit();
        }
    }

    public void onCourseDeleted() {
        Intent intent = new Intent(this, CourseListActivity.class);
        startActivity(intent);
    }
}