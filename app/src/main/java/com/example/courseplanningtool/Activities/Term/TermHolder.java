package com.example.courseplanningtool.Activities.Term;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.R;

public class TermHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Term mTerm;
    private final TextView mTermNameTextView;
    private final TextView mTermStartTextView;
    private final TextView mTermEndTextView;

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
        mTermStartTextView.setText(mTerm.getStartDateString());
        mTermEndTextView.setText(mTerm.getEndDateString());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), TermDetailsActivity.class);
        intent.putExtra(TermDetailsActivity.EXTRA_TERM_ID, mTerm.getId());
        view.getContext().startActivity(intent);
    }
}
