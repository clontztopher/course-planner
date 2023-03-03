package com.example.courseplanningtool.Fragments;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;
import com.example.courseplanningtool.R;

import java.util.concurrent.Future;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailsFragment extends Fragment {

    private static final String ARG_COURSE_ID = "courseId";

    private Course mCourse;

    private CourseDetailsFragment.OnCourseDeletedListener mListener;
    public interface OnCourseDeletedListener {
        void onCourseDeleted();
    }

    public CourseDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param courseId Selected course ID.
     * @return A new instance of fragment CourseDetailsFragment.
     */
    public static CourseDetailsFragment newInstance(long courseId) {
        CourseDetailsFragment fragment = new CourseDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long courseId = getArguments().getLong(ARG_COURSE_ID);
            Application application = (Application) getContext().getApplicationContext();
            CourseRepository courseRepo = new CourseRepository(application);
            Future courseFuture = courseRepo.findCourseById(courseId);
            try {
                mCourse = (Course) courseFuture.get();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_details, container, false);

        TextView courseTitleView = view.findViewById(R.id.courseDisplayName);
        TextView courseDatesView = view.findViewById(R.id.courseDatesView);
        Button deleteButton = view.findViewById(R.id.removeCourseBtn);
        Button addAssessmentButton = view.findViewById(R.id.addAssessmentButton);

        courseTitleView.setText(mCourse.getTitle());
        String startDateStr = mCourse.getStartDateString();
        String endDateStr = mCourse.getEndDateString();
        courseDatesView.setText(startDateStr + "-" + endDateStr);

        deleteButton.setOnClickListener(button -> {
            CourseRepository courseRepo = new CourseRepository((Application) getContext().getApplicationContext());
            Future<?> isDeleted = courseRepo.delete(mCourse);
            try {
                isDeleted.get();
                mListener.onCourseDeleted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseDetailsFragment.OnCourseDeletedListener) {
            mListener = (CourseDetailsFragment.OnCourseDeletedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCourseDeletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}