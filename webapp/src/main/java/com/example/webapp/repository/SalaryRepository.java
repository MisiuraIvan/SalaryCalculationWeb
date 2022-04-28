package com.example.webapp.repository;

import com.example.webapp.models.Salary;
import org.springframework.data.repository.CrudRepository;

public interface SalaryRepository extends CrudRepository<Salary,Long> {

}