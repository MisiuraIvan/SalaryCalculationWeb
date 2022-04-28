package com.example.webapp.repository;

import com.example.webapp.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    @Query("select u from User u where u.login = :#{#login} and u.password = :#{#password}")
    Optional<User> findUserByLoginAndPassword(String login,String password);
    @Query("select u.userId from User u where u.firstName = :#{#firsntame} and u.lastName = :#{#lastname} and u.middleName = :#{#middlename}")
    Optional<User> findByFIO(String firsntame,String lastname,String middlename);
}
