package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {


    @Query("SELECT a FROM Application a WHERE a.userId = ?1")
    List<Application> findApplicationsByUserId(int userId) throws DBException;


}
