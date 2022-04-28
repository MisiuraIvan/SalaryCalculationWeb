package com.example.webapp.models;

import javax.persistence.*;

@Entity
public class Salary {
    public Long getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getNetsalary() {
        return netsalary;
    }

    public void setNetsalary(double netsalary) {
        this.netsalary = netsalary;
    }

    public double getPension() {
        return pension;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getFszn() {
        return fszn;
    }

    public void setFszn(double fszn) {
        this.fszn = fszn;
    }

    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Salary(Long id,int award, double salary, double netsalary, double pension, double tax, double fszn, TimeSheet timeSheet) {
        this.salaryId=id;
        this.award = award;
        this.salary = salary;
        this.netsalary = netsalary;
        this.pension = pension;
        this.tax = tax;
        this.fszn = fszn;
        this.timeSheet = timeSheet;
    }
    public Salary(){

    }
    @Id
    private Long salaryId;
    private int award;
    private double salary,netsalary,pension,tax,fszn;
    @ManyToOne
    @JoinColumn(name="timeSheetId", referencedColumnName = "timeSheetId", nullable = false)
    private TimeSheet timeSheet;
}
