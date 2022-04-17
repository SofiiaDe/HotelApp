package com.epam.javacourse.hotelapp.repository;

import com.epam.javacourse.hotelapp.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

    @Query("SELECT a FROM Claim a WHERE a.userId.id = :id")
    List<Claim> findClaimsByUserId(@Param("id") int userId);

}
