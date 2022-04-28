package com.example.webapp.repository;

import com.example.webapp.models.Salary;
import com.example.webapp.models.TimeSheet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TimeSheetRepository extends CrudRepository<TimeSheet,Long> {
    @Query("select u from TimeSheet u where u.user.userId = :#{#userId}")
    Iterable<TimeSheet> findByUserId(Long userId);
}
