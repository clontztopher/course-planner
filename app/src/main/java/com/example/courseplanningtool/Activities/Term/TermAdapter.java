package com.example.courseplanningtool.Activities.Term;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Term;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermHolder> {
    private final List<Term> mTerms;

    public TermAdapter(List<Term> terms) {
        mTerms = terms;
    }

    @Override
    public TermHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
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
