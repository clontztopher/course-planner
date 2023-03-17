package com.example.courseplanningtool.Data;

import android.app.Application;

import com.example.courseplanningtool.Data.Entities.Assessment;
import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Instructor;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.Repositories.AssessmentRepository;
import com.example.courseplanningtool.Data.Repositories.CourseRepository;
import com.example.courseplanningtool.Data.Repositories.InstructorRepository;
import com.example.courseplanningtool.Data.Repositories.TermRepository;

import java.util.List;
import java.util.concurrent.Future;

public abstract class TestData {
    public static void add(Application app) {
        TermRepository termRepo = new TermRepository(app);
        List<Term> terms = null;
        Long termId = null;

        CourseRepository courseRepo = new CourseRepository(app);
        List<Course> courses = null;

        AssessmentRepository assessmentRepo = new AssessmentRepository(app);
        List<Assessment> assessments = null;

        InstructorRepository instructorRepo = new InstructorRepository(app);
        List<Instructor> instructors = null;

        Future<List<Term>> termsFuture = termRepo.getAllTerms();
        try {
            terms = termsFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (terms == null) {
            Term term1 = new Term();
            term1.setDisplayName("Term One");
            term1.setStartDateString("2023-03-01");
            term1.setEndDateString("2023-06-30");
            Future<Long> termFuture = termRepo.insert(term1);
            try {
                termId = termFuture.get();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }


    }


}
