package com.example.courseplanningtool.Data.Repositories;

import android.app.Application;

import com.example.courseplanningtool.Data.DAOs.CourseDAO;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.PlannerDatabase;

import java.util.List;
import java.util.concurrent.Future;

public class CourseRepository {
    private CourseDAO courseDAO;

    public CourseRepository(Application application) {
        PlannerDatabase db = PlannerDatabase.getInstance(application);
        courseDAO = db.courseDAO();
    }

    public Future<List<Course>> getAllCourses() {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseDAO.getAllCourses());
    }

    public Future<Course> findCourseById(long id) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseDAO.findCourseById(id));
    }
    
    public Future<List<Course>> getCoursesByTerm(long termId) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseDAO.getCoursesByTerm(termId));
    }

    public Future<?> insert(Course course) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseDAO.insertCourse(course));
    }

    public Future<?> update(Course course) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseDAO.updateCourse(course));
    }

    public Future<?> delete(Course course) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> courseDAO.deleteCourse(course));
    }
}
