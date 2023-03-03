package com.example.courseplanningtool.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private long assessmentId;

    @ColumnInfo(name = "title")
    private String assessmentTitle;

    @ColumnInfo(name = "assessment_type")
    private String type;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "end_date")
    private String endDate;

    @ColumnInfo(name = "course_id")
    private String assocCourseId;

    public long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAssocCourseId() {
        return assocCourseId;
    }

    public void setAssocCourseId(String assocCourseId) {
        this.assocCourseId = assocCourseId;
    }
}
