package com.example.courseplanningtool.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseListFragment extends Fragment {
    private List<Course> mCourses = new ArrayList<>();
    private CourseListFragment.OnCourseSelectedListener mListener;

    public interface OnCourseSelectedListener {
        void onCourseSelected(long courseId);
    }

    public CourseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CourseListFragment.
     */
    public static CourseListFragment newInstance() {
        CourseListFragment fragment = new CourseListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CourseRepository courseRepository = new CourseRepository((Application) getContext().getApplicationContext());
        Future<List<Course>> coursesFuture = courseRepository.getAllCourses();
        try {
            List<Course> courses = (List<Course>) coursesFuture.get();
            mCourses.clear();
            mCourses.addAll(courses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        if (mCourses.isEmpty()) {
            TextView noCoursesView = view.findViewById(R.id.noCoursesView);
            noCoursesView.setVisibility(View.VISIBLE);
            return view;
        }

        // Set up recycler
        RecyclerView recyclerView = view.findViewById(R.id.course_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Add divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        // Add adapter
        CourseListFragment.CourseAdapter courseAdapter = new CourseListFragment.CourseAdapter(mCourses);
        recyclerView.setAdapter(courseAdapter);

        return view;
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Course mCourse;
        private TextView mCourseNameTextView;
        private TextView mCourseStartTextView;
        private TextView mCourseEndTextView;

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_course, parent, false));
            itemView.setOnClickListener(this);
            mCourseNameTextView = itemView.findViewById(R.id.courseTitleColumn);
            mCourseStartTextView = itemView.findViewById(R.id.courseStartColumn);
            mCourseEndTextView = itemView.findViewById(R.id.courseEndColumn);
        }

        public void bind(Course course) {
            mCourse = course;
            mCourseNameTextView.setText(mCourse.getTitle());
            mCourseStartTextView.setText(mCourse.getStartDateString());
            mCourseEndTextView.setText(mCourse.getEndDateString());
        }

        @Override
        public void onClick(View view) {
            mListener.onCourseSelected(mCourse.getCourseId());
        }
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseListFragment.CourseHolder> {
        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseListFragment.CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseListFragment.CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseListFragment.CourseHolder courseHolder, int position) {
            Course course = mCourses.get(position);
            courseHolder.bind(course);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseListFragment.OnCourseSelectedListener) {
            mListener = (CourseListFragment.OnCourseSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCourseSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}