package com.example.courseplanningtool.Activities.Instructor;

import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Repositories.CourseInstructorRepository;
import com.example.courseplanningtool.R;

public class InstructorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private boolean mCourseView;
    private Instructor mInstructor;
    private final TextView mInstructorNameTextView;
    private final TextView mInstructorEmailTextView;
    private final TextView mInstructorPhoneTextView;
    private final Button removeInstructorBtn;

    private ItemTouchListener listener;

    public interface ItemTouchListener {
        public void handleItemTouch(Instructor instructor, View view);
    }

    public InstructorHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_instructor, parent, false));
        itemView.setOnClickListener(this);
        mInstructorNameTextView = itemView.findViewById(R.id.instructorListName);
        mInstructorEmailTextView = itemView.findViewById(R.id.instructorListEmail);
        mInstructorPhoneTextView = itemView.findViewById(R.id.instructorListPhone);
        removeInstructorBtn = itemView.findViewById(R.id.removeInstructorBtn);
    }

    public void bind(Instructor instructor, boolean courseView, ItemTouchListener listener) {
        mInstructor = instructor;
        mCourseView = courseView;
        this.listener = listener;
        mInstructorNameTextView.setText(mInstructor.getName());
        mInstructorEmailTextView.setText(mInstructor.getEmail());
        mInstructorPhoneTextView.setText(mInstructor.getPhone());
        if (!mCourseView) {
            removeInstructorBtn.setVisibility(View.GONE);
        } else {
            removeInstructorBtn.setOnClickListener(view -> listener.handleItemTouch(mInstructor, view));
        }
    }

    @Override
    public void onClick(View view) {
        listener.handleItemTouch(mInstructor, view);
    }
}
