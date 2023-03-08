package com.example.courseplanningtool.Activities.Assessment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.example.courseplanningtool.Data.Entities.Assessment;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentHolder> {
    private List<Assessment> mAssessments;

    public AssessmentAdapter(List<Assessment> assessments) {
        mAssessments = assessments;
    }

    @Override
    public AssessmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new AssessmentHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(AssessmentHolder assessmentHolder, int position) {
        Assessment assessment = mAssessments.get(position);
        assessmentHolder.bind(assessment);
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }
}