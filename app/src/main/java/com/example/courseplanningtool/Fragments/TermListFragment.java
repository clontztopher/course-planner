package com.example.courseplanningtool.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermListFragment extends Fragment {

    private LiveData<List<Term>> mTermsLive;
    private List<Term> mTerms;

    public interface OnTermSelectedListener {
        void onTermSelected(long termId);
    }

    private OnTermSelectedListener mListener;

    public TermListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TermListFragment.
     */
    public static TermListFragment newInstance() {
        TermListFragment fragment = new TermListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TermRepository termRepository = new TermRepository((Application) getContext().getApplicationContext());
        mTermsLive = termRepository.getAllTerms();
        List<Term> storedTerms = mTermsLive.getValue();

        if (mTerms == null) {
            mTerms = new ArrayList<>();
        }

        if (storedTerms != null) {
            mTerms.addAll(storedTerms);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_term_list, container, false);
        TextView noTermsView = view.findViewById(R.id.noTermsView);
        // Set up recycler
        RecyclerView recyclerView = view.findViewById(R.id.term_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        TermAdapter termAdapter = new TermAdapter(mTerms);
        recyclerView.setAdapter(termAdapter);
        // Show terms if present
        maybeShowTerms(recyclerView, noTermsView);

        mTermsLive.observe(getViewLifecycleOwner(), terms -> {
            if (terms == null) {
                   return;
            }
            mTerms.clear();
            mTerms.addAll(terms);
            maybeShowTerms(recyclerView, noTermsView);
        });

        return view;
    }

    private void maybeShowTerms(RecyclerView recyclerView, TextView noTermsView) {
        if (mTerms.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noTermsView.setVisibility(View.VISIBLE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        noTermsView.setVisibility(View.GONE);
    }

    private class TermHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Term mTerm;
        private TextView mTermNameTextView;
        private TextView mTermStartTextView;
        private TextView mTermEndTextView;

        public TermHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_term, parent, false));
            itemView.setOnClickListener(this);
            mTermNameTextView = itemView.findViewById(R.id.termName);
            mTermStartTextView = itemView.findViewById(R.id.termStart);
            mTermEndTextView = itemView.findViewById(R.id.termEnd);
        }

        public void bind(Term term) {
            mTerm = term;
            mTermNameTextView.setText(mTerm.getDisplayName());
            mTermStartTextView.setText("Start Date");
            mTermEndTextView.setText("End Date");
        }

        @Override
        public void onClick(View view) {
            mListener.onTermSelected(mTerm.getId());
        }
    }

    private class TermAdapter extends RecyclerView.Adapter<TermHolder> {
        private List<Term> mTerms;

        public TermAdapter(List<Term> terms) {
            mTerms = terms;
        }

        @Override
        public TermHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TermHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TermHolder termHolder, int position) {
            Term term = mTerms.get(position);
            termHolder.bind(term);
        }

        @Override
        public int getItemCount() {
            return mTerms.size();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnTermSelectedListener) {
            mListener = (OnTermSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTermSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}