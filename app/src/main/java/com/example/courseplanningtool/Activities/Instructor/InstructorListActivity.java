package com.example.courseplanningtool.Activities.Instructor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.CourseInstructorCrossRef;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Repositories.CourseInstructorRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.Fragments.InstructorPickerFragment;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.example.courseplanningtool.Utility.CancelRecyclerClick;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class InstructorListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, InstructorPickerFragment.InstructorPickerDialogListener, InstructorHolder.ItemTouchListener {
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

        attachInstructorData(courseId);

        setupRecyclerView();
        showHideInstructors();

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
            CourseRepository courseRepo = new CourseRepository(getApplication());
            Future<Course> courseFuture = courseRepo.findCourseById(courseId);

            try {
                mCourse = courseFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            CourseInstructorRepository courseInstructorRepository = new CourseInstructorRepository(getApplication());
            Future<List<Instructor>> courseInstructorsFuture = courseInstructorRepository.getInstructorsForCourse(mCourse.getCourseId());
            try {
                instructors = courseInstructorsFuture.get();
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
        if (mCourse != null) {
            adapter.setCourseView(true);
        }
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private void showHideInstructors() {
        if (mInstructors.isEmpty()) {
            noDataView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInstructorSelection(Instructor instructor) {
        CourseInstructorCrossRef courseInstructorCrossRef = new CourseInstructorCrossRef();
        courseInstructorCrossRef.setInstructorId(instructor.getInstructorId());
        courseInstructorCrossRef.setCourseId(mCourse.getCourseId());

        CourseInstructorRepository courseInstructorRepository = new CourseInstructorRepository(getApplication());
        Future<?> addInstructorFuture = courseInstructorRepository.insert(courseInstructorCrossRef);
        try {
            addInstructorFuture.get();
            attachInstructorData(mCourse.getCourseId());
            showHideInstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        attachInstructorData(mCourse == null ? -1 : mCourse.getCourseId());
        showHideInstructors();
    }

    @Override
    public void handleItemTouch(Instructor instructor, View view) {
        if (mCourse == null) {
            Intent intent = new Intent(view.getContext(), InstructorDetailsActivity.class);
            intent.putExtra(InstructorDetailsActivity.EXTRA_INSTRUCTOR_ID, instructor.getInstructorId());
            view.getContext().startActivity(intent);
            return;
        }
        if (view.getTag() != null && view.getTag().equals("removeBtn")) {
            CourseInstructorCrossRef courseInstructorCrossRef = new CourseInstructorCrossRef();
            courseInstructorCrossRef.setInstructorId(instructor.getInstructorId());
            courseInstructorCrossRef.setCourseId(mCourse.getCourseId());

            CourseInstructorRepository courseInstructorRepository = new CourseInstructorRepository(getApplication());
            Future<?> courseInstructorCrossRefFuture = courseInstructorRepository.delete(courseInstructorCrossRef);
            try {
                courseInstructorCrossRefFuture.get();
            } catch(Exception e) {
                e.printStackTrace();
            }
            attachInstructorData(mCourse.getCourseId());
            showHideInstructors();
        }
    }
}