package com.example.courseplanningtool.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentListFragment extends Fragment {
    private List<Assessment> mAssessments = new ArrayList<>();
    private AssessmentListFragment.OnAssessmentSelectedListener mListener;

    public interface OnAssessmentSelectedListener {
        void onAssessmentSelected(long assessmentId);
    }

    public AssessmentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseListFragment.
     */
    public static AssessmentListFragment newInstance() {
        AssessmentListFragment fragment = new AssessmentListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssessmentRepository assessmentRepository = new AssessmentRepository((Application) getContext().getApplicationContext());
        Future<List<Assessment>> assessmentsFuture = assessmentRepository.getAllAssessments();
        try {
            List<Assessment> assessments = (List<Assessment>) assessmentsFuture.get();
            mAssessments.clear();
            mAssessments.addAll(assessments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment_list, container, false);

        if (mAssessments.isEmpty()) {
            TextView noAssessmentsView = view.findViewById(R.id.noAssessmentsView);
            noAssessmentsView.setVisibility(View.VISIBLE);
            return view;
        }

        // Set up recycler
        RecyclerView recyclerView = view.findViewById(R.id.course_assessment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        AssessmentListFragment.AssessmentAdapter assessmentAdapter = new AssessmentListFragment.AssessmentAdapter(mAssessments);
        recyclerView.setAdapter(assessmentAdapter);

        return view;
    }

    private class AssessmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mListener.onAssessmentSelected(mAssessment.getAssessmentId());
        }
    }

    private class AssessmentAdapter extends RecyclerView.Adapter<AssessmentListFragment.AssessmentHolder> {
        private List<Assessment> mAssessments;

        public AssessmentAdapter(List<Assessment> assessments) {
            mAssessments = assessments;
        }

        @Override
        public AssessmentListFragment.AssessmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new AssessmentListFragment.AssessmentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(AssessmentListFragment.AssessmentHolder assessmentHolder, int position) {
            Assessment assessment = mAssessments.get(position);
            assessmentHolder.bind(assessment);
        }

        @Override
        public int getItemCount() {
            return mAssessments.size();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AssessmentListFragment.OnAssessmentSelectedListener) {
            mListener = (AssessmentListFragment.OnAssessmentSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAssessmentSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}