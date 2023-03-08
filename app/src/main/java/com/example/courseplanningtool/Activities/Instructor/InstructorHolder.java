package com.example.courseplanningtool.Activities.Instructor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.R;

public class InstructorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Instructor mInstructor;
    private final TextView mInstructorNameTextView;
    private final TextView mInstructorEmailTextView;
    private final TextView mInstructorPhoneTextView;

    public InstructorHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_instructor, parent, false));
        itemView.setOnClickListener(this);
        mInstructorNameTextView = itemView.findViewById(R.id.instructorListName);
        mInstructorEmailTextView = itemView.findViewById(R.id.instructorListEmail);
        mInstructorPhoneTextView = itemView.findViewById(R.id.instructorListPhone);
    }

    public void bind(Instructor instructor) {
        mInstructor = instructor;
        mInstructorNameTextView.setText(mInstructor.getName());
        mInstructorEmailTextView.setText(mInstructor.getEmail());
        mInstructorPhoneTextView.setText(mInstructor.getPhone());
    }

    @Override
    public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), InstructorDetailsActivity.class);
            intent.putExtra(InstructorDetailsActivity.EXTRA_INSTRUCTOR_ID, mInstructor.getInstructorId());
            view.getContext().startActivity(intent);
    }
}
