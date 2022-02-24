package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ApplicationClientDto;
import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.dto.ApplicationManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;

import java.sql.SQLException;
import java.util.List;

public interface IApplicationService {

    List<ApplicationDto> getApplicationsByUserId(int userId) throws AppException;

//    void create(Application application) throws AppException;
//
//    List<ApplicationDto> getAllApplications() throws AppException;
//
    List<ApplicationManagerDto> getAllDetailedApplications() throws AppException;
//
//
//    boolean updateApplication(Application application) throws AppException;
//
//    void removeApplication(int id) throws AppException;
//
//    Application getApplicationById(int applicationId) throws AppException;
//

    List<ApplicationClientDto> getUserDetailedApplications(int userID) throws AppException;

}
