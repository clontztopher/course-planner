package com.example.courseplanningtool.Activities.Assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.Term.TermEditActivity;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class AssessmentListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private List<Assessment> mAssessments = new ArrayList<>();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(this, AssessmentEditActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Assessments");
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);

        AssessmentRepository assessmentRepository = new AssessmentRepository(getApplication());
        Future<List<Assessment>> assessmentsFuture = assessmentRepository.getAllAssessments();
        try {
            List<Assessment> assessments = assessmentsFuture.get();
            mAssessments.clear();
            mAssessments.addAll(assessments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mAssessments.isEmpty()) {
            TextView noAssessmentsView = findViewById(R.id.noAssessmentsView);
            noAssessmentsView.setVisibility(View.VISIBLE);
            return;
        }

        // Set up recycler
        RecyclerView recyclerView = findViewById(R.id.course_assessment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(mAssessments);
        recyclerView.setAdapter(assessmentAdapter);

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
            Intent intent = new Intent(getParent(), AssessmentDetailsActivity.class);
            intent.putExtra(AssessmentDetailsActivity.EXTRA_ASSESSMENT_ID, mAssessment.getAssessmentId());
            startActivity(intent);
        }
    }

    private class AssessmentAdapter extends RecyclerView.Adapter<AssessmentHolder> {
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

}