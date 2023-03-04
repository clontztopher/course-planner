package com.example.courseplanningtool.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.Assessment.AssessmentEditActivity;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class CourseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_COURSE_ID = "courseId";
    private Course mCourse;
    private List<Assessment> mAssessments = new ArrayList<>();
    private List<Instructor> mInstructors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = getIntent();
        long courseId = intent.getLongExtra(EXTRA_COURSE_ID, -1);

        addToolbar();
        attachData(courseId);
        layoutView();
    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Course Details");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_navigation_items_course, menu);
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
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            intent.putExtra(AssessmentEditActivity.EXTRA_COURSE_ID, mCourse.getCourseId());
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_delete) {
            deleteCourse();
            Intent intent = new Intent(this, CourseListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachData(long courseId) {
        CourseRepository courseRepo = new CourseRepository(getApplication());
        AssessmentRepository assessmentRepo = new AssessmentRepository(getApplication());
        InstructorRepository instructorRepo = new InstructorRepository(getApplication());

        Future<Course> courseFuture = courseRepo.findCourseById(courseId);
        Future<List<Assessment>> assessmentsFuture = assessmentRepo.getAssessmentsForCourse(courseId);

        try {
            mCourse = courseFuture.get();
            mAssessments = assessmentsFuture.get();
        } catch(Exception e) {
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

        TextView noAssessmentsView = findViewById(R.id.noAssessmentsView);
        LinearLayout assessmentLayout = findViewById(R.id.assessmentListHolder);
        if (!mAssessments.isEmpty()) {
            noAssessmentsView.setVisibility(View.GONE);
            for(Assessment assessment : mAssessments) {
                LinearLayout assessmentItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_assessment, null);
                TextView titleView = assessmentItemView.findViewById(R.id.assessmentTitleColumn);
                TextView startDateView = assessmentItemView.findViewById(R.id.assessmentStartColumn);
                TextView endDateView = assessmentItemView.findViewById(R.id.assessmentEndColumn);
                titleView.setText(assessment.getAssessmentTitle());
                startDateView.setText(assessment.getStartDate());
                endDateView.setText(assessment.getEndDate());
                assessmentLayout.addView(assessmentItemView);
            }
        }

        TextView noInstructorsView = findViewById(R.id.noInstructorsView);
        LinearLayout instructorLayout = findViewById(R.id.courseInstructorsListHolder);
        if (!mInstructors.isEmpty()) {
            noInstructorsView.setVisibility(View.GONE);
            for(Instructor instructor : mInstructors) {
                LinearLayout instructorItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_item_instructor, null);
                TextView titleView = instructorItemView.findViewById(R.id.instructorListName);
                TextView emailView = instructorItemView.findViewById(R.id.instructorListEmail);
                TextView phoneView = instructorItemView.findViewById(R.id.instructorListPhone);
                titleView.setText(instructor.getName());
                emailView.setText(instructor.getEmail());
                phoneView.setText(instructor.getPhone());
                instructorLayout.addView(instructorItemView);
            }
        }
    }

    private void deleteCourse() {
        /**
         * TODO: Implement
         */
    }
}