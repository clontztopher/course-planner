package com.example.courseplanningtool.Activities.Instructor;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Instructor;

import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorHolder> {
    private final List<Instructor> mInstructors;

    public InstructorAdapter(List<Instructor> instructors) {
        mInstructors = instructors;
    }

    @Override
    public InstructorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new InstructorHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(InstructorHolder instructorHolder, int position) {
        Instructor instructor = mInstructors.get(position);
        instructorHolder.bind(instructor);
    }

    @Override
    public int getItemCount() {
        return mInstructors.size();
    }
}
