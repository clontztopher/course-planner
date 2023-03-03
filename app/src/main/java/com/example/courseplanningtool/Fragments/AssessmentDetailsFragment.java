package com.example.courseplanningtool.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

import java.util.List;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentDetailsFragment extends Fragment {

    private static final String ARG_ASSESSMENT_ID = "assessmentId";

    private Assessment mAssessment;

    private OnAssessmentDeletedListener mListener;
    public interface OnAssessmentDeletedListener {
        void onAssessmentDeleted();
    }

    public AssessmentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param assessmentId Selected assessment ID.
     * @return A new instance of fragment AssessmentDetailsFragment.
     */
    public static AssessmentDetailsFragment newInstance(long assessmentId) {
        AssessmentDetailsFragment fragment = new AssessmentDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ASSESSMENT_ID, assessmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long mAssessmentId = getArguments().getLong(ARG_ASSESSMENT_ID);
            Application application = (Application) getContext().getApplicationContext();
            AssessmentRepository assessmentRepo = new AssessmentRepository(application);
            Future assessmentFuture = assessmentRepo.findAssessmentById(mAssessmentId);
            try {
                mAssessment = (Assessment) assessmentFuture.get();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment_details, container, false);

        TextView assessmentDisplayView = view.findViewById(R.id.assessmentDisplayName);
        TextView assessmentDatesView = view.findViewById(R.id.assessmentDatesView);
        Button deleteButton = view.findViewById(R.id.removeAssessmentBtn);

        assessmentDisplayView.setText(mAssessment.getAssessmentTitle());
        String startDateStr = mAssessment.getStartDate();
        String endDateStr = mAssessment.getEndDate();
        assessmentDatesView.setText(startDateStr + "-" + endDateStr);

        deleteButton.setOnClickListener(button -> {
            AssessmentRepository assessmentRepo = new AssessmentRepository((Application) getContext().getApplicationContext());
            Future<?> isDeleted = assessmentRepo.delete(mAssessment);
            try {
                isDeleted.get();
                mListener.onAssessmentDeleted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AssessmentDetailsFragment.OnAssessmentDeletedListener) {
            mListener = (AssessmentDetailsFragment.OnAssessmentDeletedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAssessmentDeletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}