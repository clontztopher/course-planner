package com.example.courseplanningtool.Activities.Assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Activities.Course.CourseDetailActivity;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

public class AssessmentEditActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_ASSESSMENT_ID = "assessmentId";
    public static final String EXTRA_COURSE_ID = "courseId";
    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private Assessment mAssessment;
    private boolean addingNewAssessment;
    private TextView displayNameField;
    private TextView startDateField;
    private TextView endDateField;
    private Object activeDateSelectionView;
    private Spinner assessmentTypeSpinner;
    private long mCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_edit);
        addToolBar();

        long assessmentId = getIntent().getLongExtra(EXTRA_ASSESSMENT_ID, -1);
        mCourseId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);

        displayNameField = findViewById(R.id.assessmentNameField);
        startDateField = findViewById(R.id.assessmentStartDateField);
        endDateField = findViewById(R.id.assessmentEndDateField);

        startDateField.setOnFocusChangeListener(this);
        endDateField.setOnFocusChangeListener(this);

        assessmentTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentTypeSpinner.setAdapter(adapter);

        if (assessmentId == -1) {
            mAssessment = new Assessment();
            addingNewAssessment = true;
            return;
        }

        AssessmentRepository assessmentRepo = new AssessmentRepository(getApplication());
        Future<Assessment> futureAssessment = assessmentRepo.findAssessmentById(assessmentId);
        try {
            mAssessment = futureAssessment.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        displayNameField.setText(mAssessment.getAssessmentTitle());
        startDateField.setText(mAssessment.getStartDate());
        endDateField.setText(mAssessment.getEndDate());
        assessmentTypeSpinner.setSelection(adapter.getPosition(mAssessment.getType()));
    }

    public void addToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add/Edit Assessment");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateBack() {
        Intent intent;
        if (addingNewAssessment) {
            intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, mCourseId);
        } else {
            intent = new Intent(this, AssessmentDetailsActivity.class);
            intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_ID, mAssessment.getAssessmentId());
        }
        startActivity(intent);
    }

    public void handleSaveAssessmentClick(View view) {
        String displayName = displayNameField.getText().toString();
        String startDateString = startDateField.getText().toString();
        String endDateString = endDateField.getText().toString();
        String assessmentType = assessmentTypeSpinner.getSelectedItem().toString();

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

        mAssessment.setAssessmentTitle(displayName);
        mAssessment.setStartDate(startDate.format(dtFormatter));
        mAssessment.setEndDate(endDate.format(dtFormatter));
        mAssessment.setType(assessmentType);

        AssessmentRepository assessmentRepo = new AssessmentRepository(getApplication());
        Future assessmentEditFuture;

        if (addingNewAssessment) {
            mAssessment.setAssocCourseId(mCourseId);
            assessmentEditFuture = assessmentRepo.insert(mAssessment);
        } else {
            assessmentEditFuture = assessmentRepo.update(mAssessment);
        }

        try {
            assessmentEditFuture.get();
            navigateBack();
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