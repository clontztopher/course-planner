package com.example.courseplanningtool.Activities.Assessment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.R;

import java.util.concurrent.Future;

public class AssessmentDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ASSESSMENT_ID = "assessmentId";
    private Assessment mAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        long assessmentId = getIntent().getLongExtra(EXTRA_ASSESSMENT_ID, -1);
        AssessmentRepository assessmentRepo = new AssessmentRepository(getApplication());
        Future<Assessment> assessmentFuture = assessmentRepo.findAssessmentById(assessmentId);
        try {
            mAssessment = assessmentFuture.get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        layoutViews();
    }

    private void layoutViews() {
        TextView assessmentDisplayView = findViewById(R.id.assessmentDisplayName);
        TextView assessmentDatesView = findViewById(R.id.assessmentDatesView);
        TextView assessmentTypeView = findViewById(R.id.assessmentTypeView);

        assessmentDisplayView.setText(mAssessment.getAssessmentTitle());
        String startDateStr = mAssessment.getStartDate();
        String endDateStr = mAssessment.getEndDate();
        assessmentDatesView.setText(startDateStr + "-" + endDateStr);
        assessmentTypeView.setText(mAssessment.getType());
    }
}