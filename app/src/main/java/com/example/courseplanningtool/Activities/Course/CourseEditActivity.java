package com.example.courseplanningtool.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Activities.Term.TermDetailsActivity;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CourseEditActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_COURSE_ID = "courseId";
    public static final String EXTRA_TERM_ID = "termId";
    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private boolean addingNewCourse = false;
    private Course mCourse;
    private List<Assessment> mAssessments = new ArrayList<>();
    private long mTermId;
    private TextView courseTitleField;
    private TextView startDateField;
    private TextView endDateField;
    private Spinner statusSpinner;
    private Spinner instructorSpinner;
    private LinearLayout assessmentLayout;
    private TextView courseNotesField;
    private Object activeDateSelection;
    private LinearLayout assessmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);
        mTermId = getIntent().getLongExtra(EXTRA_TERM_ID, -1);

        addToolbar();

        // Text input fields
        courseTitleField = findViewById(R.id.courseTitleField);
        courseNotesField = findViewById(R.id.courseNotesInput);

        // Date fields
        startDateField = findViewById(R.id.courseStartDateField);
        endDateField = findViewById(R.id.courseEndDateField);
        startDateField.setOnFocusChangeListener(this);
        endDateField.setOnFocusChangeListener(this);

        // Spinners
        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.course_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Layout
        assessmentContainer = findViewById(R.id.assessmentsContainer);

        // Maybe useful for adding instructors and assessments
        // spinner.setOnItemSelectedListener(this);

        if (courseId == -1) {
            mCourse = new Course();
            addingNewCourse = true;
            return;
        }

        CourseRepository courseRepo = new CourseRepository(getApplication());
        Future<Course> courseFuture = courseRepo.findCourseById(courseId);
        try {
            mCourse = courseFuture.get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        courseTitleField.setText(mCourse.getTitle());
        startDateField.setText(mCourse.getStartDateString());
        endDateField.setText(mCourse.getEndDateString());
        statusSpinner.setSelection(adapter.getPosition(mCourse.getStatus()));
        courseNotesField.setText(mCourse.getNotes());

        AssessmentRepository assessmentRepository = new AssessmentRepository(getApplication());
        Future<List<Assessment>> assessmentsFuture = assessmentRepository.getAssessmentsForCourse(mCourse.getCourseId());
        try {
            mAssessments = assessmentsFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mAssessments.isEmpty()) {
            TextView noAssessmentsView = findViewById(R.id.noAssessmentView);
            noAssessmentsView.setVisibility(View.GONE);
            for (Assessment assessment: mAssessments) {
                LinearLayout assessmentItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_assessment, null);
                TextView assessmentTitleView = assessmentItemView.findViewById(R.id.assessmentTitleColumn);
                TextView assessmentStartView = assessmentTitleView.findViewById(R.id.assessmentStartColumn);
                TextView assessmentEndView = assessmentTitleView.findViewById(R.id.assessmentEndColumn);
                assessmentTitleView.setText(assessment.getAssessmentTitle());
                assessmentStartView.setText(assessment.getStartDate());
                assessmentEndView.setText(assessment.getEndDate());
                assessmentContainer.addView(assessmentItemView);
            }
        }

    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add/Edit Course");
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
        if (addingNewCourse) {
            intent = new Intent(this, TermDetailsActivity.class);
            intent.putExtra(TermDetailsActivity.EXTRA_TERM_ID, mTermId);
        } else {
            intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
        }
        startActivity(intent);
    }

    public void handleSaveCourseClick(View view) {
        String courseTitle = courseTitleField.getText().toString();
        String courseStartDateString = startDateField.getText().toString();
        String courseEndDateString = endDateField.getText().toString();
        String courseNotesString = courseNotesField.getText().toString();
        String courseStatus = statusSpinner.getSelectedItem().toString();

        if (courseTitle.equals("") || courseEndDateString.equals("") || courseStartDateString.equals("")) {
            Toast.makeText(this, "Missing required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(courseStartDateString, dtFormatter);
            endDate = LocalDate.parse(courseEndDateString, dtFormatter);
        } catch (Exception e) {
            Toast.makeText(this, "Please use the correct date format: MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        mCourse.setTitle(courseTitle);
        mCourse.setStartDateString(startDate.format(dtFormatter));
        mCourse.setEndDateString(endDate.format(dtFormatter));
        mCourse.setStatus(courseStatus);
        mCourse.setNotes(courseNotesString);

        CourseRepository courseRepo = new CourseRepository((Application) this.getApplicationContext());
        Future<?> courseEditFuture;
        if (addingNewCourse) {
            mCourse.setTermId(mTermId);
            courseEditFuture = courseRepo.insert(mCourse);
        } else {
            courseEditFuture = courseRepo.update(mCourse);
        }

        try {
            courseEditFuture.get();
            navigateBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean inFocus) {
        if(inFocus) {
            activeDateSelection = view.getTag();
            DialogFragment dialogFragment = new DatePickerFragment();
            dialogFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month + 1, day);
        String dateString = date.format(dtFormatter);

        if (activeDateSelection.equals(startDateField.getTag())) {
            startDateField.setText(dateString);
            startDateField.clearFocus();
        }

        if (activeDateSelection.equals(endDateField.getTag())) {
            endDateField.setText(dateString);
            endDateField.clearFocus();
        }

        activeDateSelection = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        Log.d("ID TEST", String.valueOf(adapterView.getId()));
        Log.d("ID TEST", String.valueOf(statusSpinner.getId()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Maybe useful for adding instructors and assessments
    }
}