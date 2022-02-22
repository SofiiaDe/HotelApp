package com.epam.javacourse.hotelapp.service.interfaces;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.exception.AppException;

import java.util.List;

public interface IApplicationService {

//    void create(Application application) throws AppException;
//
//    List<Application> getAllApplications() throws AppException;
//
//    List<ApplicationDetailed> getAllDetailedApplications() throws AppException;
//
//    List<Application> getApplicationsByUserId(int userId) throws AppException;
//
//    boolean updateApplication(Application application) throws AppException;
//
//    void removeApplication(int id) throws AppException;
//
//    Application getApplicationById(int applicationId) throws AppException;
//

    List<ApplicationDto> getUserDetailedApplications(int userID) throws AppException;

}
