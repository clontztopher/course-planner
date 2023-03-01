package com.example.courseplanningtool.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mDisplayName;

    @ColumnInfo(name = "start_date")
    private String mStartDateTimeStampString;

    @ColumnInfo(name = "end_date")
    private String mEndDateTimeStampString;

    public Term(String mDisplayName, String mStartDateTimeStampString, String mEndDateTimeStampString) {
        this.mDisplayName = mDisplayName;
        this.mStartDateTimeStampString = mStartDateTimeStampString;
        this.mEndDateTimeStampString = mEndDateTimeStampString;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) { mId = id; }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getStartDateTimeStampString() {
        return mStartDateTimeStampString;
    }

    public void setStartDateTimeStampString(String mStartDateTimeStampString) {
        this.mStartDateTimeStampString = mStartDateTimeStampString;
    }

    public String getEndDateTimeStampString() {
        return mEndDateTimeStampString;
    }

    public void setEndDateTimeStampString(String mEndDateTimeStampString) {
        this.mEndDateTimeStampString = mEndDateTimeStampString;
    }
}
