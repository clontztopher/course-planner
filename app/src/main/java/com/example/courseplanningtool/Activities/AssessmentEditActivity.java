package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

public class AssessmentEditActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {
    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private TextView displayNameField;
    private TextView startDateField;
    private TextView endDateField;
    private Object activeDateSelectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_assessment_add);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        displayNameField = findViewById(R.id.assessmentNameField);
        startDateField = findViewById(R.id.assessmentStartDateField);
        endDateField = findViewById(R.id.assessmentEndDateField);

        startDateField.setOnFocusChangeListener(this);
        endDateField.setOnFocusChangeListener(this);
    }

    public void handleSaveAssessmentClick(View view) {
        String displayName = displayNameField.getText().toString();
        String startDateString = startDateField.getText().toString();
        String endDateString = endDateField.getText().toString();

        if (displayName.equals("") || startDateString.equals("") || endDateString.equals("")) {
            Toast.makeText(this, "* All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(startDateString, dtFormatter);
            endDate = LocalDate.parse(endDateString, dtFormatter);
        } catch(Exception e) {
            Toast.makeText(this, "* Please use the correct date format: MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        Assessment assessment = new Assessment();
        assessment.setAssessmentTitle(displayName);
        assessment.setStartDate(startDate.format(dtFormatter));
        assessment.setEndDate(endDate.format(dtFormatter));

        AssessmentRepository assessmentRepo = new AssessmentRepository((Application) this.getApplicationContext());
        Future assessmentAddFuture = assessmentRepo.insert(assessment);

        try {
            assessmentAddFuture.get();
            Intent intent = new Intent(this, AssessmentListActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFocusChange(View view, boolean inFocus) {
        if (inFocus) {
            activeDateSelectionView = view.getTag();
            DialogFragment dialogFragment = new DatePickerFragment();
            dialogFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month + 1, day);
        String dateString = date.format(dtFormatter);

        if (activeDateSelectionView.equals(startDateField.getTag())) {
            startDateField.setText(dateString);
            startDateField.clearFocus();
        }

        if (activeDateSelectionView.equals(endDateField.getTag())) {
            endDateField.setText(dateString);
            endDateField.clearFocus();
        }

        activeDateSelectionView = null;
    }
}