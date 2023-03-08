package com.example.courseplanningtool.Activities.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.Future;

public class InstructorDetailsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String EXTRA_INSTRUCTOR_ID = "instructorId";
    private Instructor mInstructor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, InstructorEditActivity.class);
            intent.putExtra(InstructorEditActivity.EXTRA_INSTRUCTOR_ID, mInstructor.getInstructorId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);

        long instructorId = getIntent().getLongExtra(EXTRA_INSTRUCTOR_ID, -1);

        attachInstructorData(instructorId);
        addToolbar();
        layoutViews();
    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Instructor Information");
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    private void attachInstructorData(long instructorId) {
        InstructorRepository instructorRepository = new InstructorRepository(getApplication());
        Future<Instructor> instructorFuture = instructorRepository.findInstructorById(instructorId);
        try {
            mInstructor = instructorFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void layoutViews() {
        TextView instructorNameView = findViewById(R.id.instructorNameView);
        TextView instructorEmailView = findViewById(R.id.instructorEmailView);
        TextView instructorPhoneView = findViewById(R.id.instructorPhoneView);

        instructorNameView.setText(mInstructor.getName());
        instructorEmailView.setText(mInstructor.getEmail());
        instructorPhoneView.setText(mInstructor.getPhone());
    }

    @Override
    public void onResume() {
        super.onResume();
        attachInstructorData(mInstructor.getInstructorId());
        layoutViews();
    }
}