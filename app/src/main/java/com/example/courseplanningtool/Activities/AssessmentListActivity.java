package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.courseplanningtool.Fragments.AssessmentListFragment;
import com.example.courseplanningtool.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessmentListActivity extends AppCompatActivity implements AssessmentListFragment.OnAssessmentSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_assessment_list);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Add floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButtonAssessments);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            startActivity(intent);
        });

        // Add fragment for term list
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.assessmentlist_fragment_container);

        if (fragment == null) {
            fragment = new AssessmentListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.assessmentlist_fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onAssessmentSelected(long assessmentId) {
        Intent intent = new Intent(this, AssessmentDetailsActivity.class);
        intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_ID, assessmentId);
        startActivity(intent);
    }
}