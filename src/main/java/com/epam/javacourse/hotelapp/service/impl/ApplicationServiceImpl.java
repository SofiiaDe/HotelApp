package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.ApplicationRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import com.epam.javacourse.hotelapp.utils.mappers.ApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements IApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<ApplicationDto> getUserDetailedApplications(int userID) throws AppException {

        try {
            List<Application> userApplications = applicationRepository.findApplicationsByUserId(userID);
            List<ApplicationDto> allUserApplications = new ArrayList<>();

            ApplicationMapper mapper = new ApplicationMapper();
            userApplications.forEach(x -> allUserApplications.add(mapper.mapToDto(x)));

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

    @Override
    public List<ApplicationDto> getAllDetailedApplications() throws AppException {
        try {
            List<Application> applications = applicationRepository.findAll();
            List<ApplicationDto> allApplications = new ArrayList<>();

            ApplicationMapper mapper = new ApplicationMapper();
            applications.forEach((x) -> allApplications.add(mapper.mapToDto(x)));


            if (allApplications.isEmpty()) {
                return Collections.emptyList();
            }

            List<Integer> userIds = applications.stream()
                    .map(Application::getUserId)
                    .map(User::getId)
                    .distinct().collect(Collectors.toList());

            List<User> data = userRepository.findUsersByIds(userIds);

            ArrayList<ApplicationDto> result = new ArrayList<>();

            for (ApplicationDto application : allApplications) {
                var user = data.stream().filter(u -> u.getId() == application.getUserId())
                        .findFirst().get();
                result.add(
                        new ApplicationDto(application.getId(),
                                user.getFirstName() + ' ' + user.getLastName(),
                                user.getEmail(),
                                application.getCheckinDate(),
                                application.getCheckoutDate(),
                                application.getRoomTypeBySeats(),
                                application.getRoomClass()
                        ));
            }

            return result;

        } catch (DBException exception) {
            throw new AppException("Can't retrieve all applications to show in the manager's account", exception);
        }
    }
}
