package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.courseplanningtool.Fragments.AssessmentDetailsFragment;
import com.example.courseplanningtool.Fragments.TermDetailsFragment;
import com.example.courseplanningtool.R;

public class AssessmentDetailsActivity extends AppCompatActivity implements AssessmentDetailsFragment.OnAssessmentDeletedListener {

    public static final String EXTRA_ASSESSMENT_ID = "assessmentId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_assessment_detail);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.assessment_details_fragment_container);

        if (fragment == null) {
            long assessmentId = getIntent().getLongExtra(EXTRA_ASSESSMENT_ID, -1);
            fragment = AssessmentDetailsFragment.newInstance(assessmentId);
            fragmentManager.beginTransaction()
                    .add(R.id.assessment_details_fragment_container, fragment)
                    .commit();
        }
    }

    public void onAssessmentDeleted() {
        Intent intent = new Intent(this, AssessmentListActivity.class);
        startActivity(intent);
    }
}