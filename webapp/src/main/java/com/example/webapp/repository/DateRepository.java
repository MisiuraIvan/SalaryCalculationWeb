package com.example.webapp.repository;

import com.example.webapp.models.Date;
import com.example.webapp.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DateRepository extends CrudRepository<Date,Long> {
    @Query("select u.dateId from Date u where u.month = :#{#month} and u.year=:#{#year}")
    Optional<Date> findByMonthAndYear(String month,int year);
}
