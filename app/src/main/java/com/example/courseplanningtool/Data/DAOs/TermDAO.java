package com.example.courseplanningtool.Data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.courseplanningtool.Data.Entities.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Query("SELECT * FROM terms")
    public List<Term> getTerms();

    @Query("SELECT * FROM terms WHERE id = :id")
    public Term findTermById(long id);

    @Insert(onConflict =  OnConflictStrategy.FAIL)
    public void insertTerm(Term term);

    @Update
    public void updateTerm(Term term);

    @Delete
    public void deleteTerm(Term term);
}
