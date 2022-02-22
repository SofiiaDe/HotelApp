package com.epam.javacourse.hotelapp.utils.mappers;

public interface DtoMapper<T, E> {

    T mapToDto(E e);

    E mapFromDto(T t);
}
