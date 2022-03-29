package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.repository.ClaimRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.impl.ClaimServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceImplTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreate_whenCalled_callsRepo() {
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepository, userRepository);

        claimService.createClaim(new ClaimDto());
        verify(claimRepository, times(1)).save(any(Claim.class));
    }
}
