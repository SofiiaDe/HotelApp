package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {


    @Query("SELECT a FROM Application a WHERE a.userId.id = :id")
    List<Application> findApplicationsByUserId(@Param("id") int userId) throws DBException;




}
