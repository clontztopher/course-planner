package com.example.courseplanningtool.Data.Repositories;

import android.app.Application;

import com.example.courseplanningtool.Data.DAOs.AssessmentDAO;
import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.PlannerDatabase;

import java.util.List;
import java.util.concurrent.Future;

public class AssessmentRepository {
    private AssessmentDAO assessmentDAO;

    public AssessmentRepository(Application application) {
        PlannerDatabase db = PlannerDatabase.getInstance(application);
        assessmentDAO = db.assessmentDAO();
    }

    public Future<List<Assessment>> getAllAssessments() {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> assessmentDAO.getAllAssessments());
    }

    public Future<List<Assessment>> getAssessmentsForCourse(long courseId) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> assessmentDAO.getAssessmentsForCourse(courseId));
    }

    public Future<Assessment> findAssessmentById(long id) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> assessmentDAO.findAssessmentById(id));
    }

    public Future<?> insert(Assessment assessment) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> assessmentDAO.insertAssessment(assessment));
    }

    public Future<?> update(Assessment assessment) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> assessmentDAO.updateAssessment(assessment));
    }

    public Future<?> delete(Assessment assessment) {
        return PlannerDatabase.databaseWriteExecutor.submit(() -> assessmentDAO.deleteAssessment(assessment));
    }
}
