package com.example.courseplanningtool.Data.DAOs;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.courseplanningtool.Data.Entities.Instructor;

import java.util.List;

@Dao
public interface InstructorDAO {
    @Query("SELECT * FROM instructors")
    public List<Instructor> getInstructors();
}
