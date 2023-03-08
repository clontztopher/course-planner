package com.example.courseplanningtool.Activities.Assessment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.R;

public class AssessmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Assessment mAssessment;
    private TextView mAssessmentNameTextView;
    private TextView mAssessmentStartTextView;
    private TextView mAssessmentEndTextView;

    public AssessmentHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_assessment, parent, false));
        itemView.setOnClickListener(this);
        mAssessmentNameTextView = itemView.findViewById(R.id.assessmentTitleColumn);
        mAssessmentStartTextView = itemView.findViewById(R.id.assessmentStartColumn);
        mAssessmentEndTextView = itemView.findViewById(R.id.assessmentEndColumn);
    }

    public void bind(Assessment assessment) {
        mAssessment = assessment;
        mAssessmentNameTextView.setText(mAssessment.getAssessmentTitle());
        mAssessmentStartTextView.setText(mAssessment.getStartDate());
        mAssessmentEndTextView.setText(mAssessment.getStartDate());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), AssessmentDetailsActivity.class);
        intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_ID, mAssessment.getAssessmentId());
        view.getContext().startActivity(intent);
    }
}