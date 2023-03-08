package com.example.courseplanningtool.Data.Entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"instructorId", "courseId"})
public class CourseInstructorCrossRef {
    public long instructorId;
    public long courseId;
}
