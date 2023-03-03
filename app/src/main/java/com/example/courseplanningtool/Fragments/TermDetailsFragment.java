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

import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

import java.util.List;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermDetailsFragment extends Fragment {

    private static final String ARG_TERM_ID = "termId";

    private Term mTerm;

    private OnTermEventListener mListener;
    public interface OnTermEventListener {
        void onTermDeleted();
        void onTermEditRequest(long id);
    }

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
            Application application = (Application) getContext().getApplicationContext();
            TermRepository termRepo = new TermRepository(application);
            Future term = termRepo.getTermById(mTermId);
            try {
                mTerm = (Term) term.get();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term_details, container, false);

        TextView termDisplayView = view.findViewById(R.id.termDisplayName);
        TextView termDatesView = view.findViewById(R.id.termDatesView);
        Button deleteButton = view.findViewById(R.id.removeTermBtn);
        Button editButton = view.findViewById(R.id.editTermButton);

        termDisplayView.setText(mTerm.getDisplayName());
        String startDateStr = mTerm.getStartDateString();
        String endDateStr = mTerm.getEndDateString();
        termDatesView.setText(startDateStr + "-" + endDateStr);

        deleteButton.setOnClickListener(button -> {
            TermRepository termRepo = new TermRepository((Application) getContext().getApplicationContext());
            Future<?> isDeleted = termRepo.delete(mTerm);
            try {
                isDeleted.get();
                mListener.onTermDeleted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        editButton.setOnClickListener(button -> mListener.onTermEditRequest(mTerm.getId()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TermDetailsFragment.OnTermEventListener) {
            mListener = (TermDetailsFragment.OnTermEventListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTermEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}