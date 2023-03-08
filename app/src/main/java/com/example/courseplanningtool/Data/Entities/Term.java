package com.example.courseplanningtool.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mDisplayName;

    @ColumnInfo(name = "start_date")
    private String mStartDateString;

    @ColumnInfo(name = "end_date")
    private String mEndDateString;

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

    @Override
    public String toString() {
        return getDisplayName();
    }
}
