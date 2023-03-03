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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

public class CourseEditActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private TextView courseTitleField;
    private TextView startDateField;
    private TextView endDateField;
    private Spinner statusSpinner;
    private TextView courseNotesField;
    private Object activeDateSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_course_add);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

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

        // Maybe useful for adding instructors and assessments
        // spinner.setOnItemSelectedListener(this);
    }

    public void handleSaveCourseClick(View view) {
        String courseTitle = courseTitleField.getText().toString();
        String courseStartDateString = startDateField.getText().toString();
        String courseEndDateString = endDateField.getText().toString();

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

        Course course = new Course();
        course.setTitle(courseTitle);
        course.setStartDateString(startDate.format(dtFormatter));
        course.setEndDateString(endDate.format(dtFormatter));

        CourseRepository courseRepo = new CourseRepository((Application) this.getApplicationContext());
        Future courseAddFuture = courseRepo.insert(course);

        try {
            courseAddFuture.get();
            Intent intent = new Intent(this, CourseListActivity.class);
            startActivity(intent);
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
        // Maybe useful for adding instructors and assessments
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Maybe useful for adding instructors and assessments
    }
}