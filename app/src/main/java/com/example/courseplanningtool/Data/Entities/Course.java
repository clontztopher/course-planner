package com.example.courseplanningtool.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private long courseId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "start_date")
    private String mStartDateString;

    @ColumnInfo(name = "end_date")
    private String mEndDateString;

    @ColumnInfo(name = "status")
    private String mStatus;

    @ColumnInfo(name = "term_id")
    private long mTermId;

    @ColumnInfo(name = "notes")
    private String mNotes;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long mId) {
        this.courseId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getStartDateString() {
        return mStartDateString;
    }

    public void setStartDateString(String mStartDateString) {
        this.mStartDateString = mStartDateString;
    }

    public String getEndDateString() {
        return mEndDateString;
    }

    public void setEndDateString(String mEndDateString) {
        this.mEndDateString = mEndDateString;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public long getTermId() {
        return mTermId;
    }

    public void setTermId(long mTermId) {
        this.mTermId = mTermId;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String mNotes) {
        this.mNotes = mNotes;
    }
}
