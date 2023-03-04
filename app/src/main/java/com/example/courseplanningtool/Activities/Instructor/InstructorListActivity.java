package com.example.courseplanningtool.Activities.Instructor;

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

import com.example.courseplanningtool.Activities.Term.TermDetailsActivity;
import com.example.courseplanningtool.Activities.Term.TermEditActivity;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.MainMenuProvider;
import com.example.courseplanningtool.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class InstructorListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private List<Instructor> mInstructors = new ArrayList<>();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return MainMenuProvider.navItemSelected(item, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Instructors");
        setSupportActionBar(toolbar);

        NavigationBarView navBar = findViewById(R.id.main_nav);
        navBar.setOnItemSelectedListener(this);

        boolean hasData = attachData();
        RecyclerView recyclerView = setupRecyclerView();

        if (!hasData) {
            showNoDataView(recyclerView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_navigation_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.action_add) {
//            Intent intent = new Intent(this, InstructorEditActivity.class);
//            startActivity(intent);
//            return true;
////        }
        return super.onOptionsItemSelected(item);
    }

    private boolean attachData() {
        InstructorRepository instructorRepo = new InstructorRepository(getApplication());
        Future<List<Instructor>> listFuture = instructorRepo.getAllInstructors();
        try {
            List<Instructor> instructors = listFuture.get();
            mInstructors.clear();
            mInstructors.addAll(instructors);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mInstructors.isEmpty()) {
            return false;
        }
        return true;
    }

    private RecyclerView setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.instructor_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        InstructorAdapter adapter = new InstructorAdapter(mInstructors);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    private void showNoDataView(RecyclerView recyclerView) {
        TextView noDataView = findViewById(R.id.noInstructorsListView);
        noDataView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private class InstructorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Instructor mInstructor;
        private final TextView mInstructorNameTextView;
        private final TextView mInstructorEmailTextView;
        private final TextView mInstructorPhoneTextView;

        public InstructorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_term, parent, false));
            itemView.setOnClickListener(this);
            mInstructorNameTextView = itemView.findViewById(R.id.instructorListName);
            mInstructorEmailTextView = itemView.findViewById(R.id.instructorListEmail);
            mInstructorPhoneTextView = itemView.findViewById(R.id.instructorListPhone);
        }

        public void bind(Instructor instructor) {
            mInstructor = instructor;
            mInstructorNameTextView.setText(mInstructor.getName());
            mInstructorEmailTextView.setText(mInstructor.getEmail());
            mInstructorPhoneTextView.setText(mInstructor.getPhone());
        }

        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(view.getContext(), InstructorDetailsActivity.class);
//            intent.putExtra(InstructorDetailsActivity.EXTRA_INSTRUCTOR_ID, mInstructor.getInstructorId());
//            startActivity(intent);
        }
    }

    private class InstructorAdapter extends RecyclerView.Adapter<InstructorHolder> {
        private final List<Instructor> mInstructors;

        public InstructorAdapter(List<Instructor> instructors) {
            mInstructors = instructors;
        }

        @Override
        public InstructorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new InstructorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(InstructorHolder instructorHolder, int position) {
            Instructor instructor = mInstructors.get(position);
            instructorHolder.bind(instructor);
        }

        @Override
        public int getItemCount() {
            return mInstructors.size();
        }
    }
}