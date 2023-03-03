package com.example.courseplanningtool.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.courseplanningtool.Data.DAOs.AssessmentDAO;
import com.example.courseplanningtool.Data.DAOs.CourseDAO;
import com.example.courseplanningtool.Data.DAOs.TermDAO;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.CourseInstructor;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Entities.Term;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Term.class,
        Course.class,
        Assessment.class,
//        Instructor.class,
//        CourseInstructor.class
}, version = 1)
public abstract class PlannerDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "course_planner.db";
    private static volatile PlannerDatabase mPlannerDB;
    private static final int NUM_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    public static PlannerDatabase getInstance(final Context context) {
        if (mPlannerDB == null) {
            synchronized (PlannerDatabase.class) {
                if (mPlannerDB == null) {
                    mPlannerDB = Room.databaseBuilder(context, PlannerDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return mPlannerDB;
    }

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
}
