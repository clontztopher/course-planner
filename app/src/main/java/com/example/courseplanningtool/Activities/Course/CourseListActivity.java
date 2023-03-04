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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.Course.CourseDetailActivity;
import com.example.courseplanningtool.Activities.Term.TermEditActivity;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CourseListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private List<Course> mCourses = new ArrayList<>();

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
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("All Courses");
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);

        CourseRepository courseRepository = new CourseRepository(getApplication());
        Future<List<Course>> coursesFuture = courseRepository.getAllCourses();
        try {
            List<Course> courses = coursesFuture.get();
            mCourses.clear();
            mCourses.addAll(courses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mCourses.isEmpty()) {
            TextView noCoursesView = findViewById(R.id.noCoursesView);
            noCoursesView.setVisibility(View.VISIBLE);
            return;
        }

        // Set up recycler
        RecyclerView recyclerView = findViewById(R.id.course_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        CourseAdapter courseAdapter = new CourseAdapter(mCourses);
        recyclerView.setAdapter(courseAdapter);

    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Course mCourse;
        private TextView mCourseNameTextView;
        private TextView mCourseStartTextView;
        private TextView mCourseEndTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_course, parent, false));
            itemView.setOnClickListener(this);
            mCourseNameTextView = itemView.findViewById(R.id.courseTitleColumn);
            mCourseStartTextView = itemView.findViewById(R.id.courseStartColumn);
            mCourseEndTextView = itemView.findViewById(R.id.courseEndColumn);
        }

        public void bind(Course course) {
            mCourse = course;
            mCourseNameTextView.setText(mCourse.getTitle());
            mCourseStartTextView.setText(mCourse.getStartDateString());
            mCourseEndTextView.setText(mCourse.getEndDateString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), CourseDetailActivity.class);
            intent.putExtra(CourseDetailActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
            startActivity(intent);
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder courseHolder, int position) {
            Course course = mCourses.get(position);
            courseHolder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }
    }
}