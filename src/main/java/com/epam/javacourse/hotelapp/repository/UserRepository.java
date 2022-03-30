package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false " +
            "END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u where u.email = :email")
    User findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findUsersByIds(@Param("ids") List<Integer> ids);


}


