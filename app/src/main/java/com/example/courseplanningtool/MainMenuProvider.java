package com.example.courseplanningtool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.courseplanningtool.Activities.Assessment.AssessmentListActivity;
import com.example.courseplanningtool.Activities.Course.CourseListActivity;
import com.example.courseplanningtool.Activities.Instructor.InstructorListActivity;
import com.example.courseplanningtool.Activities.Term.TermListActivity;

public abstract class MainMenuProvider {
    public static boolean navItemSelected(MenuItem item, Context context) {
        switch(item.getItemId()) {
            case R.id.action_terms: {
                Intent intent = new Intent(context, TermListActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.action_courses: {
                Intent intent = new Intent(context, CourseListActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.action_assessments: {
                Intent intent = new Intent(context, AssessmentListActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.action_instructors: {
                Intent intent = new Intent(context, InstructorListActivity.class);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }
}
