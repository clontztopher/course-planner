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

//    public Future<Term> getTermById(long id) {
//        return PlannerDatabase.databaseWriteExecutor.submit(() -> termDAO.findTermById(id));
//    }
//
//    public Future<?> insert(Term term) {
//        return PlannerDatabase.databaseWriteExecutor.submit(() -> termDAO.insertTerm(term));
//    }
//
//    public Future<?> delete(Term term) {
//        return PlannerDatabase.databaseWriteExecutor.submit(() -> termDAO.deleteTerm(term));
//    }
}

