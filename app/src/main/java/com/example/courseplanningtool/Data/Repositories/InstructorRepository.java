package com.example.courseplanningtool.Data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.courseplanningtool.Data.DAOs.InstructorDAO;
import com.example.courseplanningtool.Data.DAOs.TermDAO;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.PlannerDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class InstructorRepository {
    private InstructorDAO instructorDAO;

    public InstructorRepository(Application application) {
        PlannerDatabase db = PlannerDatabase.getInstance(application);
        instructorDAO = db.instructorDAO();
    }

    public Future<List<Instructor>> getAllInstructors() {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> instructorDAO.getInstructors());
    }

    public Future<Instructor> findInstructorById(long instructorId) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> instructorDAO.findInstructorById(instructorId));
    }

    public Future<?> addNewInstructor(Instructor instructor) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> instructorDAO.insertInstructor(instructor));
    }

    public Future<?> updateInstructor(Instructor instructor) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> instructorDAO.updateInstructor(instructor));
    }

    public Future<?> deleteInstructor(Instructor instructor) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> instructorDAO.deleteInstructor(instructor));
    }
}

