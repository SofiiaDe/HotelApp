package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

//    @Query("delete from User u where u.role.id = ?1")
//void deleteInBulkByRoleId(long roleId);

    @Query("SELECT a FROM Application a WHERE a.userId = ?1")

//    @Query("SELECT a FROM Application a WHERE a.user.userId = ?1")
    List<ApplicationDto> findApplicationsByUserId(int userId) throws DBException;
}
