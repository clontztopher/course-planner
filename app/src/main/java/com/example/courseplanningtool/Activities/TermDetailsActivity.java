package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.courseplanningtool.Fragments.TermDetailsFragment;
import com.example.courseplanningtool.R;

public class TermDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TERM_ID = "termId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_term_detail);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.term_details_fragment_container);

        if (fragment == null) {
            long termId = getIntent().getLongExtra(EXTRA_TERM_ID, -1);
            fragment = TermDetailsFragment.newInstance(termId);
            fragmentManager.beginTransaction()
                    .add(R.id.term_details_fragment_container, fragment)
                    .commit();
        }
    }
}