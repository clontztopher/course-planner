package com.example.courseplanningtool.Activities.Term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.Course.CourseListActivity;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.Future;

/**
 * Term Details
 *
 * 1. Accepts termId extra for retrieving term data
 * 2. Has one nav menu item for edit that goes to TermEdit and passes termId extra
 * 3. Grabs term data and list of courses associated with term for display
 * 4. Has recycler view for courses and alternate view if no courses
 */
public class TermDetailsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String EXTRA_TERM_ID = "termId";
    private Term mTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        long termId = getIntent().getLongExtra(EXTRA_TERM_ID, -1);

        addNavigation();
        attachData(termId);
        render();
    }

    private void addNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Term Details");
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, TermEditActivity.class);
            intent.putExtra(TermEditActivity.ARG_TERM_ID, mTerm.getId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachData(long termId) {
        TermRepository termRepo = new TermRepository(getApplication());
        Future<Term> term = termRepo.getTermById(termId);
        try {
            mTerm = term.get();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void render() {
        TextView termDisplayView = findViewById(R.id.termDisplayName);
        TextView startDateView = findViewById(R.id.termStartView);
        TextView endDateView = findViewById(R.id.termEndDateView);

        termDisplayView.setText(mTerm.getDisplayName());
        String startDateStr = mTerm.getStartDateString();
        String endDateStr = mTerm.getEndDateString();
        startDateView.setText(startDateStr);
        endDateView.setText(endDateStr);
    }

    public void handleViewCoursesClick(View view) {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra(CourseListActivity.ASSOCIATED_TERM_ID, mTerm.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachData(mTerm.getId());
        render();
    }
}