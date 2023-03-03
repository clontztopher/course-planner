package com.example.courseplanningtool.Data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.courseplanningtool.Data.Entities.Course;
import com.example.courseplanningtool.Data.Entities.CourseInstructor;

import java.util.List;

@Dao
public interface CourseDAO {
    @Query("SELECT * FROM courses")
    public List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE courseId = :id")
    public Course findCourseById(long id);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insertCourse(Course course);

    @Update
    public void updateCourse(Course course);

    @Delete
    public void deleteCourse(Course course);
}
