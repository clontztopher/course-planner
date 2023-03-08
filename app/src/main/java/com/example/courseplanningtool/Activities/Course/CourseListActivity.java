package com.example.courseplanningtool.Activities.Course;

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

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CourseListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    public static final String ASSOCIATED_TERM_ID = "termId";
    private final List<Course> mCourses = new ArrayList<>();
    private Term relatedTerm;

    private RecyclerView recyclerView;
    private TextView noCoursesView;

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
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(CourseEditActivity.EXTRA_TERM_ID, relatedTerm.getId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        long assocTermId = getIntent().getLongExtra(ASSOCIATED_TERM_ID, -1);

        noCoursesView = findViewById(R.id.noCoursesView);
        recyclerView = findViewById(R.id.course_list_recycler);

        setUpRecycler();

        showHideCourses(assocTermId);

        attachTermData(assocTermId);
        addNavigation(relatedTerm.getDisplayName());
    }

    private void addNavigation(String termName) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Term: " + termName);
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    private boolean attachCourseData(long assocTermId) {
        CourseRepository courseRepository = new CourseRepository(getApplication());
        Future<List<Course>> coursesFuture;

        coursesFuture = courseRepository.getCoursesByTerm(assocTermId);

        try {
            List<Course> courses = coursesFuture.get();
            mCourses.clear();
            mCourses.addAll(courses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !mCourses.isEmpty();
    }

    private void attachTermData(long termId) {
        TermRepository termRepository = new TermRepository(getApplication());
        Future<Term> termFuture = termRepository.getTermById(termId);
        try {
            relatedTerm = termFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHideCourses(long assocTermId) {
        boolean hasData = attachCourseData(assocTermId);

        if (!hasData) {
            noCoursesView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noCoursesView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private RecyclerView setUpRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        CourseAdapter courseAdapter = new CourseAdapter(mCourses);
        recyclerView.setAdapter(courseAdapter);
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        showHideCourses(relatedTerm.getId());
    }
}