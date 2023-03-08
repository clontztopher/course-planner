package com.example.courseplanningtool.Data.Entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CourseWithInstructors {
    @Embedded public Course course;
    @Relation(
            parentColumn = "courseId",
            entityColumn = "instructorId",
            associateBy = @Junction(CourseInstructorCrossRef.class)
    )
    public List<Instructor> instructors;

    public Course getCourse() {
        return course;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }
}
