package com.epam.javacourse.hotelapp.service.impl;

import com.epam.javacourse.hotelapp.dto.ApplicationClientDto;
import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.dto.ApplicationManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.Application;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.*;
import com.epam.javacourse.hotelapp.service.interfaces.IApplicationService;
import com.epam.javacourse.hotelapp.utils.mappers.ApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements IApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;


    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDto> getApplicationsByUserId(int userId) throws AppException {
        final List<ApplicationDto> result = new ArrayList<>();
        List<Application> applications = applicationRepository.findApplicationsByUserId(userId);

        applications.forEach(x -> result.add(ApplicationMapper.mapToDto(x)));
        return result;
    }

    @Override
    public List<ApplicationClientDto> getUserDetailedApplications(int userID) throws AppException {

        try {
            List<Application> allUserApplications = applicationRepository.findApplicationsByUserId(userID);

            if (allUserApplications.isEmpty()) {
                return Collections.emptyList();
            }

            ArrayList<ApplicationClientDto> result = new ArrayList<>();

            for (Application application : allUserApplications) {
                result.add(
                         new ApplicationClientDto(application.getId(),
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
    public List<ApplicationManagerDto> getAllDetailedApplications() throws AppException {
        try {
            List<Application> allApplications = applicationRepository.findAll();

            if (allApplications.isEmpty()) {
                return Collections.emptyList();
            }

            List<Integer> userIds = allApplications.stream()
                    .map(Application::getUserId)
                    .map(User::getId)
                    .distinct().collect(Collectors.toList());

            List<User> users = userRepository.findUsersByIds(userIds);

            List<ApplicationManagerDto> result = new ArrayList<>();

            for (Application application : allApplications) {
                Optional<User> optionalUser = users.stream()
                        .filter(u -> u.getId() == application.getUserId().getId())
                        .findFirst();
                if (optionalUser.isEmpty()) {
                    throw new ChangeSetPersister.NotFoundException();
                }
                User user = optionalUser.get();
                result.add(
                        new ApplicationManagerDto(
                                application.getId(),
                                user.getFirstName() + ' ' + user.getLastName(),
                                user.getEmail(),
                                application.getCheckinDate(),
                                application.getCheckoutDate(),
                                application.getRoomTypeBySeats(),
                                application.getRoomClass()
                        ));
            }
            return result;

        } catch (DBException | ChangeSetPersister.NotFoundException exception) {
            throw new AppException("Can't retrieve all applications to show in the manager's account", exception);
        }

    }
}
