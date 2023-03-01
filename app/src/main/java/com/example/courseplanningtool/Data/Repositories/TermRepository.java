package com.example.courseplanningtool.Data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.courseplanningtool.Data.DAOs.TermDAO;
import com.example.courseplanningtool.Data.Entities.Term;
import com.example.courseplanningtool.Data.PlannerDatabase;

import java.util.List;

public class TermRepository {
    private TermDAO termDAO;
    private LiveData<List<Term>> mAllTerms;
    private LiveData<Term> mSingleTerm;

    public TermRepository(Application application) {
        PlannerDatabase db = PlannerDatabase.getInstance(application);
        termDAO = db.termDAO();
        mAllTerms = termDAO.getTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return mAllTerms;
    }

    public LiveData<Term> getTermById(long id) {
        return termDAO.findTermById(id);
    }

    public void insert(Term term) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
           termDAO.insertTerm(term);
        });
    }
}
