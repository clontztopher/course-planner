package com.example.courseplanningtool.Activities.Course;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.R;

public class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        view.getContext().startActivity(intent);
    }
}
