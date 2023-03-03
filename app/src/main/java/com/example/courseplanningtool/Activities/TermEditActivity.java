package com.example.courseplanningtool.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.Fragments.DatePickerFragment;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Future;

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

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_term_add);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(item -> {
            if (item.getId() != -1) {
                return;
            }
            Intent intent;
            if (addingNewTerm) {
                intent = new Intent(this, TermListActivity.class);
            } else {
                intent = new Intent(this, TermDetailsActivity.class);
                intent.putExtra(TermDetailsActivity.EXTRA_TERM_ID, mTerm.getId());
            }
            startActivity(intent);
        });

        displayNameField = findViewById(R.id.termNameField);
        startDateField = findViewById(R.id.termStartDateField);
        endDateField = findViewById(R.id.termEndDateField);

        startDateField.setOnFocusChangeListener(this);
        endDateField.setOnFocusChangeListener(this);

        long termId = getIntent().getLongExtra(ARG_TERM_ID, -1);

        // Adding new
        if (termId == -1) {
            mTerm = new Term();
            addingNewTerm = true;
            return;
        }

        // Updating existing
        TermRepository termRepo = new TermRepository(getApplication());
        Future<Term> termFuture = termRepo.getTermById(termId);
        try {
            mTerm = termFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        displayNameField.setText(mTerm.getDisplayName());
        startDateField.setText(mTerm.getStartDateString());
        endDateField.setText(mTerm.getStartDateString());
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

        TermRepository termRepo = new TermRepository((Application) this.getApplicationContext());
        Future termAddFuture = termRepo.insert(mTerm);

        try {
            termAddFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, TermListActivity.class);
        startActivity(intent);
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