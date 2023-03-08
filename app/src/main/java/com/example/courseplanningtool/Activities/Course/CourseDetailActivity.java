package com.example.courseplanningtool.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.Assessment.AssessmentAdapter;
import com.example.courseplanningtool.Activities.Assessment.AssessmentEditActivity;
import com.example.courseplanningtool.Activities.Assessment.AssessmentListActivity;
import com.example.courseplanningtool.Activities.Instructor.InstructorAdapter;
import com.example.courseplanningtool.Activities.Instructor.InstructorListActivity;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.example.courseplanningtool.Utility.CancelRecyclerClick;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CourseDetailActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String EXTRA_COURSE_ID = "courseId";
    private Course mCourse;
    private Term mTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        long courseId = intent.getLongExtra(EXTRA_COURSE_ID, -1);

        attachData(courseId);
        addToolbar();
        layoutView();
    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Term: " + mTerm.getDisplayName());
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(CourseEditActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    private void attachData(long courseId) {
        CourseRepository courseRepo = new CourseRepository(getApplication());
        Future<Course> courseFuture = courseRepo.findCourseById(courseId);
        try {
            mCourse = courseFuture.get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        TermRepository termRepo = new TermRepository(getApplication());
        Future<Term> termFuture = termRepo.getTermById(mCourse.getTermId());
        try {
            mTerm = termFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void layoutView() {
        TextView courseTitleView = findViewById(R.id.courseDisplayName);
        TextView courseDatesView = findViewById(R.id.courseDatesView);
        TextView courseStatusView = findViewById(R.id.courseStatusView);
        TextView courseNotesView = findViewById(R.id.courseNotesView);

        courseTitleView.setText(mCourse.getTitle());
        String startDateStr = mCourse.getStartDateString();
        String endDateStr = mCourse.getEndDateString();
        courseDatesView.setText(startDateStr + "-" + endDateStr);
        courseStatusView.setText(mCourse.getStatus());
        courseNotesView.setText(mCourse.getNotes());
    }

    public void handleAssessmentButtonClick(View view) {
        Intent intent = new Intent(this, AssessmentListActivity.class);
        intent.putExtra(AssessmentListActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
        startActivity(intent);
    }

    public void handleInstructorButtonClick(View view) {
        Intent intent = new Intent(this, InstructorListActivity.class);
        intent.putExtra(InstructorListActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachData(mCourse.getCourseId());
        layoutView();
    }
}