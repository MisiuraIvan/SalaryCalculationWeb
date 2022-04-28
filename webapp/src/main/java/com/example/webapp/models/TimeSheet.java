package com.example.webapp.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class TimeSheet {
    @Id
    private Long timeSheetId;
    private int worktime,sickleave,holiday,overtime;
    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "userId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name="dateId", referencedColumnName = "dateId", nullable = false)
    private Date date;

    public TimeSheet(Long timeSheetId, int worktime, int sickleave, int holiday, int overtime, User user, Date date) {
        this.timeSheetId = timeSheetId;
        this.worktime = worktime;
        this.sickleave = sickleave;
        this.holiday = holiday;
        this.overtime = overtime;
        this.user = user;
        this.date = date;
    }

    public TimeSheet(Long timeSheetId) {
        this.timeSheetId = timeSheetId;
    }
    public TimeSheet() {
    }

    public Long getTimeSheetId() {
        return timeSheetId;
    }

    public void setTimeSheetId(Long timeSheetId) {
        this.timeSheetId = timeSheetId;
    }

    public int getWorktime() {
        return worktime;
    }

    public void setWorktime(int worktime) {
        this.worktime = worktime;
    }

    public int getSickleave() {
        return sickleave;
    }

    public void setSickleave(int sickleave) {
        this.sickleave = sickleave;
    }

    public int getHoliday() {
        return holiday;
    }

    public void setHoliday(int holiday) {
        this.holiday = holiday;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @OneToMany
    private List<Salary> salarys;
}
