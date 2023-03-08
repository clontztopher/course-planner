package com.example.courseplanningtool.Data.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.CourseWithInstructors;

import java.util.List;

@Dao
public interface CourseDAO {
    @Query("SELECT * FROM courses")
    public List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE courseId = :id")
    public Course findCourseById(long id);

    @Query("SELECT * FROM courses WHERE term_id = :termId")
    public List<Course> getCoursesByTerm(long termId);

    @Transaction
    @Query("SELECT * FROM courses WHERE courses.courseId = :courseId")
    public CourseWithInstructors getCourseWithInstructors(long courseId);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insertCourse(Course course);

    @Update
    public void updateCourse(Course course);

    @Delete
    public void deleteCourse(Course course);
}
