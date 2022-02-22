package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.repository.ApplicationRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ApplicationServiceImpl implements IApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Override
    public List<ApplicationDto> getUserDetailedApplications(int userID) throws AppException {

        try {
            List<ApplicationDto> allUserApplications = applicationRepository.findApplicationsByUserId(userID);

            if (allUserApplications.isEmpty()) {
                return Collections.emptyList();
            }

            List<ApplicationDto> result = new ArrayList<>();

            for (ApplicationDto application : allUserApplications) {
                result.add(
                        new ApplicationDto(application.getId(),
                                application.getCheckinDate(),
                                application.getCheckoutDate(),
                                application.getRoomTypeBySeats(),
                                application.getRoomClass()
                        ));
            }
            return result;
        } catch (DBException exception) {
            throw new AppException("Can't retrieve client's applications to show in the client's account", exception);
        }
    }
}
