package com.example.courseplanningtool.Activities.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.Term.TermDetailsActivity;
import com.example.courseplanningtool.Activities.Term.TermEditActivity;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.CourseWithInstructors;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.Fragments.InstructorPickerFragment;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class InstructorListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, InstructorPickerFragment.InstructorPickerDialogListener {
    public static final String EXTRA_COURSE_ID = "courseId";
    private List<Instructor> mInstructors = new ArrayList<>();
    private Course mCourse;

    private RecyclerView recyclerView;
    private TextView noDataView;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);

        recyclerView = findViewById(R.id.instructor_list_recycler);
        noDataView = findViewById(R.id.noInstructorsListView);

        setupRecyclerView();

        showHideInstructors(courseId);

        addNavigation();
    }

    private void addNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (mCourse == null) {
            toolbar.setTitle("Instructors");
        } else {
            toolbar.setTitle("Course: " + mCourse.getTitle());
        }

        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
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
            if (mCourse == null) {
                Intent intent = new Intent(this, InstructorEditActivity.class);
                startActivity(intent);
            } else {
                DialogFragment addInstructorDialog = new InstructorPickerFragment();
                addInstructorDialog.show(getSupportFragmentManager(), "instructorPicker");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean attachInstructorData(long courseId) {
        List<Instructor> instructors;
        if (courseId == -1) {
            InstructorRepository instructorRepo = new InstructorRepository(getApplication());
            Future<List<Instructor>> listFuture = instructorRepo.getAllInstructors();
            try {
                instructors = listFuture.get();
                mInstructors.clear();
                mInstructors.addAll(instructors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CourseRepository courseRepository = new CourseRepository(getApplication());
            Future<CourseWithInstructors> courseWithInstructorsFuture = courseRepository.getCourseWithInstructors(courseId);
            try {
                CourseWithInstructors course = courseWithInstructorsFuture.get();
                mCourse = course.getCourse();
                instructors = course.getInstructors();
                mInstructors.clear();
                mInstructors.addAll(instructors);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return !mInstructors.isEmpty();
    }

    private RecyclerView setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        InstructorAdapter adapter = new InstructorAdapter(mInstructors);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private void showHideInstructors(long courseId) {
        boolean hasInstructors = attachInstructorData(courseId);

        if (!hasInstructors) {
            noDataView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInstructorSelection(Instructor instructor) {
        Log.d("TEST", instructor.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        showHideInstructors(mCourse == null ? -1 : mCourse.getCourseId());
    }
}