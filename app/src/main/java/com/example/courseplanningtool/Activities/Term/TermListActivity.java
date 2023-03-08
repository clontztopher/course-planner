package com.example.courseplanningtool.Activities.Term;

import androidx.annotation.NonNull;
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

import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Term List View
 *
 * 1. Lists all terms
 * 2. Shows alternate view if no terms
 * 3. Has "Add Term" button that goes to TermEdit with no extra
 * 4. Terms in list are clickable and go to TermDetail with termId extra
 */
public class TermListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private List<Term> mTerms = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noTermsView;

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
            Intent intent = new Intent(this, TermEditActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        addNavigation();

        noTermsView = findViewById(R.id.noTermsView);
        recyclerView = findViewById(R.id.term_list_recycler);
        setupRecyclerView();
        showHideTerms();
    }

    private void addNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Course Terms");
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);
    }

    private boolean attachTermData() {
        TermRepository termRepository = new TermRepository(getApplication());
        Future<List<Term>> termsFuture = termRepository.getAllTerms();
        try {
            List<Term> terms = termsFuture.get();
            mTerms.clear();
            mTerms.addAll(terms);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !mTerms.isEmpty();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        TermAdapter termAdapter = new TermAdapter(mTerms);
        recyclerView.setAdapter(termAdapter);
    }

    private void showHideTerms() {
        boolean hasTermData = attachTermData();

        if (!hasTermData) {
            noTermsView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noTermsView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHideTerms();
    }
}