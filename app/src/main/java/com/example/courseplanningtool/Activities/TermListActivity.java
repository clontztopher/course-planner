package com.example.courseplanningtool.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.example.courseplanningtool.Fragments.TermListFragment;
import com.example.courseplanningtool.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermListActivity extends AppCompatActivity implements TermListFragment.OnTermSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        // Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_term_list);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Add floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, TermAddActivity.class);
            startActivity(intent);
        });

        // Add fragment for term list
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.term_list_fragment_container);

        if (fragment == null) {
            fragment = new TermListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.term_list_fragment_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onTermSelected(long termId) {
        Intent intent = new Intent(this, TermDetailsActivity.class);
        intent.putExtra(TermDetailsActivity.EXTRA_TERM_ID, termId);
        startActivity(intent);
    }
}