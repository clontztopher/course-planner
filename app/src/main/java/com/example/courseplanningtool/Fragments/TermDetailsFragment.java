package com.example.courseplanningtool.Fragments;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermDetailsFragment extends Fragment {

    private static final String ARG_TERM_ID = "termId";

    private LiveData<Term> mTermLive;

    public TermDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param termId Selected term ID.
     * @return A new instance of fragment TermDetailsFragment.
     */
    public static TermDetailsFragment newInstance(long termId) {
        TermDetailsFragment fragment = new TermDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TERM_ID, termId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long mTermId = getArguments().getLong(ARG_TERM_ID);

            TermRepository termRepo = new TermRepository((Application) getContext().getApplicationContext());
            mTermLive = termRepo.getTermById(mTermId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term_details, container, false);

        TextView termDisplayView = view.findViewById(R.id.termDisplayName);
        TextView termDatesView = view.findViewById(R.id.termDatesView);

        mTermLive.observe(getViewLifecycleOwner(), term -> {
            if (term != null) {
                termDisplayView.setText(term.getDisplayName());
                String startDateStr = term.getStartDateTimeStampString();
                String endDateStr = term.getEndDateTimeStampString();
                termDatesView.setText(startDateStr + "-" + endDateStr);
            }
        });
        return view;
    }
}