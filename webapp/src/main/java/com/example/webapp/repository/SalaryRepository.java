package com.example.webapp.repository;

import com.example.webapp.models.Post;
import com.example.webapp.models.Salary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SalaryRepository extends CrudRepository<Salary,Long> {
    @Query("select sum(u.salary) from Salary u where u.timeSheet.date.dateId = :#{#id}")
    Integer SumByDateId(Long id);
    @Query("select u.salary from Salary u where u.timeSheet.date.dateId = :#{#id} and u.timeSheet.user.userId = :#{#userId}")
    Integer SumByDateIdAndUserId(Long id,Long userId);
}