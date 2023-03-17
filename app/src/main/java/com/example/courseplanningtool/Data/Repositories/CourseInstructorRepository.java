package com.example.courseplanningtool.Data.Repositories;

import android.app.Application;

import com.example.courseplanningtool.Data.DAOs.CourseInstructorDAO;
import com.example.courseplanningtool.Data.Entities.CourseInstructorCrossRef;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.PlannerDatabase;

import java.util.List;
import java.util.concurrent.Future;

public class CourseInstructorRepository {
    private CourseInstructorDAO courseInstructorDAO;

    public CourseInstructorRepository(Application application) {
        PlannerDatabase db = PlannerDatabase.getInstance(application);
        courseInstructorDAO = db.courseInstructorDAO();
    }

    public Future<List<Instructor>> getInstructorsForCourse(long courseId) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseInstructorDAO.findInstructorsForCourse(courseId));
    }

    public Future<List<Instructor>> getInstructorsNotAssigned(long courseId) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseInstructorDAO.getInstructorsNotAssigned(courseId));
    }

    public Future<?> removeFromCourses(long instructorId) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseInstructorDAO.removeFromCourses(instructorId));
    }

    public Future<?> insert(CourseInstructorCrossRef courseInstructorCrossRef) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseInstructorDAO.insert(courseInstructorCrossRef));
    }

    public Future<?> update(CourseInstructorCrossRef courseInstructorCrossRef) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseInstructorDAO.update(courseInstructorCrossRef));
    }

    public Future<?> delete(CourseInstructorCrossRef courseInstructorCrossRef) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseInstructorDAO.delete(courseInstructorCrossRef));
    }
}

