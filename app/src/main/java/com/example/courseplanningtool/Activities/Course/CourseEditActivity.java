package com.example.courseplanningtool.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

public class CourseEditActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    /**
     * Accepts course id extra argument for updating fields with data from course to edit
     */
    public static final String EXTRA_COURSE_ID = "courseId";

    /**
     * Accepts term id extra argument if new course is being created
     */
    public static final String EXTRA_TERM_ID = "termId";
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Flag for whether activity is created to add a new course or edit an existing one
     */
    private boolean addingNewCourse = false;

    /**
     * The course that will eventually be updated or saved
     */
    private Course course;

    /**
     * Below are the views that will hold the data for saving
     */
    private TextView courseTitleField;
    private TextView startDateField;
    private TextView endDateField;
    private Spinner statusSpinner;
    private TextView courseNotesField;
    private Switch startAlertToggle;
    private Switch endAlertToggle;

    /**
     * Saves the current target view of data selector
     */
    private Object activeDateSelection;
    private ArrayAdapter<CharSequence> statusSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);
        long associatedTermId = getIntent().getLongExtra(EXTRA_TERM_ID, -1);

        addToolbar();

        assignViews();

        if (courseId == -1) {
            course = new Course();
            course.setTermId(associatedTermId);
            addingNewCourse = true;
        } else {
            attachCourseData(courseId);
            setCourseViews();
        }
    }

    private void assignViews() {
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
        statusSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.course_status_array, android.R.layout.simple_spinner_item);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        // Alert Toggles
        startAlertToggle = findViewById(R.id.courseStartAlertSwitch);
        endAlertToggle = findViewById(R.id.courseEndAlertSwitch);
    }

    private void attachCourseData(long courseId) {
        CourseRepository courseRepo = new CourseRepository(getApplication());
        Future<Course> courseFuture = courseRepo.findCourseById(courseId);
        try {
            course = courseFuture.get();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setCourseViews() {
        courseTitleField.setText(course.getTitle());
        startDateField.setText(course.getStartDateString());
        endDateField.setText(course.getEndDateString());
        courseNotesField.setText(course.getNotes());
        startAlertToggle.setChecked(course.hasStartAlert());
        endAlertToggle.setChecked(course.hasEndAlert());
    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add/Edit Course");
        setSupportActionBar(toolbar);
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

        if (startDate.isAfter(endDate)) {
            Toast.makeText(this, "Start date must be after end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        course.setTitle(courseTitle);
        course.setStartDateString(startDate.format(dtFormatter));
        course.setEndDateString(endDate.format(dtFormatter));
        course.setStatus(courseStatus);
        course.setNotes(courseNotesString);
        course.setStartAlert(startAlertToggle.isChecked());
        course.setEndAlert(endAlertToggle.isChecked());

        CourseRepository courseRepo = new CourseRepository(getApplication());
        Future<?> courseEditFuture;

        if (addingNewCourse) {
            courseEditFuture = courseRepo.insert(course);
        } else {
            courseEditFuture = courseRepo.update(course);
        }

        try {
            courseEditFuture.get();
            finish();
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

    private void deleteCourse() {
        AssessmentRepository assessmentRepository = new AssessmentRepository(getApplication());
        Future<?> removeAssessmentsFuture = assessmentRepository.deleteCourseAssessments(course.getCourseId());
        CourseRepository courseRepo = new CourseRepository(getApplication());
        Future<?> deleteFuture = courseRepo.delete(course);
        try {
            removeAssessmentsFuture.get();
            deleteFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // Mandatory callback for interface
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if (!addingNewCourse && adapterView.getId() == statusSpinner.getId()) {
            adapterView.setSelection(statusSpinnerAdapter.getPosition(course.getStatus()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            Intent intent = new Intent(this, CourseListActivity.class);
            intent.putExtra(CourseListActivity.ASSOCIATED_TERM_ID, course.getTermId());
            deleteCourse();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}