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
    private long assocCourseId;

    @ColumnInfo(name = "start_alert")
    private boolean startAlert;

    @ColumnInfo(name = "end_alert")
    private boolean endAlert;

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

    public long getAssocCourseId() {
        return assocCourseId;
    }

    public void setAssocCourseId(long assocCourseId) {
        this.assocCourseId = assocCourseId;
    }

    public boolean hasStartAlert() {
        return startAlert;
    }

    public void setStartAlert(boolean startAlert) {
        this.startAlert = startAlert;
    }

    public boolean hasEndAlert() {
        return endAlert;
    }

    public void setEndAlert(boolean endAlert) {
        this.endAlert = endAlert;
    }
}
