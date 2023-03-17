package com.example.courseplanningtool.Data.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.courseplanningtool.Data.Entities.CourseInstructorCrossRef;
import com.example.courseplanningtool.Data.Entities.Instructor;

import java.util.List;

@Dao
public interface CourseInstructorDAO {
    @Query("SELECT * FROM course_instructor " +
            "INNER JOIN instructors ON instructors.instructorId = course_instructor.instructorId " +
            "WHERE course_instructor.courseId = :courseId")
    public List<Instructor> findInstructorsForCourse(long courseId);

    @Query("SELECT * FROM course_instructor " +
            "INNER JOIN instructors ON instructors.instructorId = course_instructor.instructorId " +
            "WHERE course_instructor.courseId != :courseId")
    public List<Instructor> getInstructorsNotAssigned(long courseId);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void insert(CourseInstructorCrossRef courseInstructorCrossRef);

    @Update
    public void update(CourseInstructorCrossRef courseInstructorCrossRef);

    @Delete
    public void delete(CourseInstructorCrossRef courseInstructorCrossRef);

    @Query("DELETE FROM course_instructor WHERE instructorId = :instructorId")
    public void removeFromCourses(long instructorId);
}
