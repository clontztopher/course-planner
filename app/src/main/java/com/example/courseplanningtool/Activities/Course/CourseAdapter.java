package com.example.courseplanningtool.Activities.Course;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
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
