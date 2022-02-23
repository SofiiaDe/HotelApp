package com.epam.javacourse.hotelapp.utils.mappers;

import com.epam.javacourse.hotelapp.dto.ApplicationDto;
import com.epam.javacourse.hotelapp.model.Application;

public class ApplicationMapper implements DtoMapper<ApplicationDto, Application>{

    @Override
    public ApplicationDto mapToDto(Application application){
        ApplicationDto dto = new ApplicationDto();
        dto.setId(application.getId());
        dto.setUserId(application.getUserId().getId());
        dto.setCheckinDate(application.getCheckinDate());
        dto.setCheckoutDate(application.getCheckoutDate());
        dto.setRoomTypeBySeats(application.getRoomTypeBySeats());
        dto.setRoomClass(application.getRoomClass());
        return dto;
    }

    @Override
    public Application mapFromDto(ApplicationDto dto){
        Application application = new Application();
        application.setId(dto.getId());
        application.setCheckinDate(dto.getCheckinDate());
        application.setCheckoutDate(dto.getCheckoutDate());
        application.setRoomTypeBySeats(dto.getRoomTypeBySeats());
        application.setRoomClass(dto.getRoomClass());
        return application;
    }
}
