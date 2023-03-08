package com.example.courseplanningtool.Activities.Term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Activities.Course.CourseAdapter;
import com.example.courseplanningtool.Activities.Course.CourseEditActivity;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.example.courseplanningtool.Utility.CancelRecyclerClick;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Add/Edit Term
 *
 * 1. Inputs: Term Name, Start Date, End Date, Course Add
 * 2. Course add goes to CourseEdit passing in termId for association
 * 3. Flag for edit/add determination. Accepts termId extra if editing.
 */
public class TermEditActivity extends AppCompatActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {
    public static final String ARG_TERM_ID = "termId";
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private boolean addingNewTerm = false;
    private Term mTerm;
    private TextView displayNameField;
    private TextView startDateField;
    private TextView endDateField;
    private Object activeDateSelectionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        long termId = getIntent().getLongExtra(ARG_TERM_ID, -1);

        addToolbar();

        displayNameField = findViewById(R.id.termNameField);
        startDateField = findViewById(R.id.termStartDateField);
        endDateField = findViewById(R.id.termEndDateField);

        startDateField.setOnFocusChangeListener(this);
        endDateField.setOnFocusChangeListener(this);

        // Adding new
        if (termId == -1) {
            mTerm = new Term();
            addingNewTerm = true;
            return;
        }

        attachTermData(termId);

        displayNameField.setText(mTerm.getDisplayName());
        startDateField.setText(mTerm.getStartDateString());
        endDateField.setText(mTerm.getStartDateString());
    }

    private void addToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add/Edit Course Term");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteTerm();
            Intent intent = new Intent(this, TermListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachTermData(long termId) {
        TermRepository termRepo = new TermRepository(getApplication());
        Future<Term> termFuture = termRepo.getTermById(termId);
        try {
            mTerm = termFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSaveTermClick(View view) {
        String displayName = displayNameField.getText().toString();
        String startDateString = startDateField.getText().toString();
        String endDateString = endDateField.getText().toString();

        if (displayName.equals("") || startDateString.equals("") || endDateString.equals("")) {
            Toast.makeText(this, "* All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(startDateString, dtFormatter);
            endDate = LocalDate.parse(endDateString, dtFormatter);
        } catch(Exception e) {
            Toast.makeText(this, "* Please use the correct date format: MM/DD/YYYY", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        mTerm.setDisplayName(displayName);
        mTerm.setStartDateString(startDate.format(dtFormatter));
        mTerm.setEndDateString(endDate.format(dtFormatter));

        TermRepository termRepo = new TermRepository(getApplication());
        Future<?> termEditFuture;
        if (addingNewTerm) {
            termEditFuture = termRepo.insert(mTerm);
        } else {
            termEditFuture = termRepo.update(mTerm);
        }

        try {
            termEditFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    private void deleteTerm() {
        TermRepository termRepo = new TermRepository(getApplication());
        Future<?> deleteFuture = termRepo.delete(mTerm);
        try {
            deleteFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFocusChange(View view, boolean inFocus) {
        if (inFocus) {
            activeDateSelectionView = view.getTag();
            DialogFragment dialogFragment = new DatePickerFragment();
            dialogFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month + 1, day);
        String dateString = date.format(dtFormatter);

        if (activeDateSelectionView.equals(startDateField.getTag())) {
            startDateField.setText(dateString);
            startDateField.clearFocus();
        }

        if (activeDateSelectionView.equals(endDateField.getTag())) {
            endDateField.setText(dateString);
            endDateField.clearFocus();
        }

        activeDateSelectionView = null;
    }
}