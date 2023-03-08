package com.example.courseplanningtool.Data.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.Instructor;

import java.util.List;

@Dao
public interface InstructorDAO {
    @Query("SELECT * FROM instructors")
    public List<Instructor> getInstructors();

    @Query(("SELECT * FROM instructors WHERE instructorId = :instructorId"))
    public Instructor findInstructorById(long instructorId);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insertInstructor(Instructor instructor);

    @Update
    public void updateInstructor(Instructor instructor);

    @Delete
    public void deleteInstructor(Instructor instructor);
}
