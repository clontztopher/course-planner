package com.example.courseplanningtool.Activities.Assessment;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class AssessmentListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String EXTRA_COURSE_ID = "courseId";
    private List<Assessment> mAssessments = new ArrayList<>();
    private Course mCourse;

    RecyclerView recyclerView;
    TextView noAssessmentsView;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(AssessmentEditActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);

        noAssessmentsView = findViewById(R.id.noAssessmentsView);
        recyclerView = findViewById(R.id.course_assessment_recycler);

        setUpRecycler();

        attachCourse(courseId);
        addNavigation();

        showHideAssessments();
    }

    private void addNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Course: " + mCourse.getTitle());
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    private void setUpRecycler() {
        // Set up recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(mAssessments);
        recyclerView.setAdapter(assessmentAdapter);
    }

    private void attachCourse(long courseId) {
        CourseRepository courseRepository = new CourseRepository(getApplication());
        Future<Course> courseFuture = courseRepository.findCourseById(courseId);
        try {
            mCourse = courseFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean attachAssessmentData(long courseId) {
        AssessmentRepository assessmentRepository = new AssessmentRepository(getApplication());
        Future<List<Assessment>> assessmentsFuture;

        assessmentsFuture = assessmentRepository.getAssessmentsForCourse(courseId);

        try {
            List<Assessment> assessments = assessmentsFuture.get();
            mAssessments.clear();
            mAssessments.addAll(assessments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !mAssessments.isEmpty();
    }

    private void showHideAssessments() {
        boolean hasAssessments = attachAssessmentData(mCourse.getCourseId());

        if (!hasAssessments) {
            noAssessmentsView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noAssessmentsView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHideAssessments();
    }
}