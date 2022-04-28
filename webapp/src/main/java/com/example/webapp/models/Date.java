package com.example.webapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Date {
    @Id
    private Long dateId;
    private String month;

    public Date(Long dateId) {
        this.dateId = dateId;
    }

    public Long getDateId() {
        return dateId;
    }

    public void setDateId(Long dateId) {
        this.dateId = dateId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWorkHours() {
        return workHours;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    private int year,workHours;
    @OneToMany
    private List<TimeSheet> timeSheets;
    public Date() {
    }
    public Date(Long dateId, String month, int year, int workHours) {
        this.dateId = dateId;
        this.month = month;
        this.year = year;
        this.workHours = workHours;
    }
}
