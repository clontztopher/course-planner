package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.courseplanningtool.Activities.TermListActivity;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

public class TermAddActivity extends AppCompatActivity {
    private TextView displayNameField;
    private TextView startDateField;
    private TextView endDateField;
    private TextView fieldErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_term_add);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        displayNameField = findViewById(R.id.termNameField);
        startDateField = findViewById(R.id.termStartDateField);
        endDateField = findViewById(R.id.termEndDateField);
        fieldErrorText = findViewById(R.id.termFieldsError);
        fieldErrorText.setVisibility(View.INVISIBLE);
    }

    public void handleSaveButtonClick(View view) {
        String displayName = displayNameField.getText().toString();
        String startDateString = startDateField.getText().toString();
        String endDateString = endDateField.getText().toString();

        if (displayName.equals("") || startDateString.equals("") || endDateString.equals("")) {
            fieldErrorText.setVisibility(View.VISIBLE);
            return;
        }

        if (fieldErrorText.getVisibility() == View.VISIBLE) {
            fieldErrorText.setVisibility(View.INVISIBLE);
        }

        Term term = new Term(displayName, startDateString, endDateString);

        TermRepository termRepo = new TermRepository((Application) this.getApplicationContext());
        termRepo.insert(term);

        Intent intent = new Intent(this, TermListActivity.class);
        startActivity(intent);
    }
}