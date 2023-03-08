package com.example.courseplanningtool.Activities.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Activities.Assessment.AssessmentListActivity;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

public class InstructorEditActivity extends AppCompatActivity {
    public static final String EXTRA_INSTRUCTOR_ID = "instructorId";

    private Instructor mInstructor;

    private boolean addingNewInstructor;
    private TextView nameField;
    private TextView emailField;
    private TextView phoneField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_edit);
        addToolBar();

        long instructorId = getIntent().getLongExtra(EXTRA_INSTRUCTOR_ID, -1);

        nameField = findViewById(R.id.instructorNameField);
        emailField = findViewById(R.id.instructorEmailField);
        phoneField = findViewById(R.id.instructorPhoneField);

        if (instructorId == -1) {
            mInstructor = new Instructor();
            addingNewInstructor = true;
            return;
        }

        InstructorRepository instructorRepository = new InstructorRepository(getApplication());
        Future<Instructor> futureInstructor = instructorRepository.findInstructorById(instructorId);
        try {
            mInstructor = futureInstructor.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        nameField.setText(mInstructor.getName());
        emailField.setText(mInstructor.getEmail());
        phoneField.setText(mInstructor.getPhone());
    }

    public void addToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add/Edit Instructor");
        setSupportActionBar(toolbar);
    }

    public void handleSaveInstructorClick(View view) {
        String instName = nameField.getText().toString();
        String instEmail = emailField.getText().toString();
        String instPhone = phoneField.getText().toString();

        if (instName.equals("") || instEmail.equals("") || instPhone.equals("")) {
            Toast.makeText(this, "* All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        mInstructor.setName(instName);
        mInstructor.setEmail(instEmail);
        mInstructor.setPhone(instPhone);

        InstructorRepository instructorRepository = new InstructorRepository(getApplication());
        Future<?> repoFuture;

        if (addingNewInstructor) {
            repoFuture = instructorRepository.addNewInstructor(mInstructor);
        } else {
            repoFuture = instructorRepository.updateInstructor(mInstructor);
        }

        try {
            repoFuture.get();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteInstructor() {
        InstructorRepository instructorRepository = new InstructorRepository(getApplication());
        Future<?> deletedFuture = instructorRepository.deleteInstructor(mInstructor);
        try {
            deletedFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
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
            deleteInstructor();
            Intent intent = new Intent(this, InstructorListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}