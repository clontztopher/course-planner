package com.example.courseplanningtool.Activities.Term;

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
import com.example.courseplanningtool.Activities.Course.CourseEditActivity;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

import java.util.List;
import java.util.concurrent.Future;

public class TermDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TERM_ID = "termId";
    private Term mTerm;
    private List<Course> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        long termId = getIntent().getLongExtra(EXTRA_TERM_ID, -1);

        addToolbar("Term Details");
        attachData(termId);
        layoutView();
    }

    private void addToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_navigation_items_term, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, TermEditActivity.class);
            intent.putExtra(TermEditActivity.ARG_TERM_ID, mTerm.getId());
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, CourseEditActivity.class);
            intent.putExtra(CourseEditActivity.EXTRA_TERM_ID, mTerm.getId());
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_delete) {
            deleteTerm();
            Intent intent = new Intent(this, TermListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachData(long termId) {
        TermRepository termRepo = new TermRepository(getApplication());
        CourseRepository courseRepo = new CourseRepository(getApplication());

        try {
            Future<Term> term = termRepo.getTermById(termId);
            mTerm = term.get();

            Future<List<Course>> courses = courseRepo.getCoursesByTerm(mTerm.getId());
            mCourses = courses.get();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void layoutView() {
        TextView termDisplayView = findViewById(R.id.termDisplayName);
        TextView termDatesView = findViewById(R.id.termDatesView);

        termDisplayView.setText(mTerm.getDisplayName());
        String startDateStr = mTerm.getStartDateString();
        String endDateStr = mTerm.getEndDateString();
        termDatesView.setText(startDateStr + "-" + endDateStr);

        if (mCourses.isEmpty()) {
            TextView recyclerLabel = findViewById(R.id.coursesRecyclerLabel);
            recyclerLabel.setText("No Courses Added for this Term");
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.coursesRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        CourseAdapter courseAdapter = new CourseAdapter(mCourses);
        recyclerView.setAdapter(courseAdapter);
    }

    private void deleteTerm() {
        TermRepository termRepo = new TermRepository(getApplication());
        Future<?> deleteFuture = termRepo.delete(mTerm);
        try {
            deleteFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Course mCourse;
        private TextView courseNameTextView;
        private TextView courseStartTextView;
        private TextView courseEndTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_course, parent, false));
            itemView.setOnClickListener(this);
            courseNameTextView = itemView.findViewById(R.id.courseTitleColumn);
            courseStartTextView = itemView.findViewById(R.id.courseStartColumn);
            courseEndTextView = itemView.findViewById(R.id.courseEndColumn);
        }

        public void bind(Course course) {
            mCourse = course;
            courseNameTextView.setText(mCourse.getTitle());
            courseStartTextView.setText(mCourse.getStartDateString());
            courseEndTextView.setText(mCourse.getEndDateString());
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