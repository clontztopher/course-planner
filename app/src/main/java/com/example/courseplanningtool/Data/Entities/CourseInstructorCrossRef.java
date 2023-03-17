package com.example.courseplanningtool.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(
        tableName = "course_instructor",
        primaryKeys = {"instructorId", "courseId"}
)
public class CourseInstructorCrossRef {

    @ColumnInfo(name = "instructorId")
    private long instructorId;

    @ColumnInfo(name = "courseId")
    private long courseId;

    public long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(long instructorId) {
        this.instructorId = instructorId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
