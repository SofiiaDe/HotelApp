package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.ClaimClientDto;
import com.epam.javacourse.hotelapp.dto.ClaimDto;
import com.epam.javacourse.hotelapp.dto.ClaimManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.ClaimRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.impl.ClaimServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.ClaimMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceImplTest {

    @Mock
    private ClaimRepository claimRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Test
    void testCreate_whenCalled_callsRepo() {
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, userRepositoryMock);
        claimService.createClaim(new ClaimDto());
        verify(claimRepositoryMock, times(1)).save(any(Claim.class));
    }

    @Test
    void testGetClaimById_whenCalled_callsRepo() {
        int claimId = 112567890;
        Claim claim = getClaims().get(0);
        when(claimRepositoryMock.findById(any())).thenReturn(Optional.of(claim));
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);
        claimService.getClaimById(claimId);

        verify(claimRepositoryMock, times(1)).findById(claimId);
    }

    @Test
    void testGetClaimById_whenRepoThrows_throwsException() {
        int claimId = 112567890;

        when(claimRepositoryMock.findById(claimId)).thenThrow(new NoSuchElementFoundException("Can't get claim with id = " + claimId));
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);

        Assertions.assertThrowsExactly(NoSuchElementFoundException.class, () -> claimService.getClaimById(claimId));
    }

    @Test
    void testGetClaimById_whenRepoThrows_ShowExceptionMessage() {
        int claimId = 112567890;
        when(claimRepositoryMock.findById(claimId)).thenReturn(Optional.empty());

        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);

        try {
            claimService.getClaimById(claimId);
        } catch (NoSuchElementFoundException ex) {
            assertEquals("Can't get claim with id = " + claimId, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown NoSuchElementFoundException");
    }

    @Test
    void testGetClaimById_whenCalled_returnsCorrectClaim() {
        int claimId = 112567890;
        ClaimDto expectedClaim = new ClaimDto();
        expectedClaim.setRoomSeats("have a nice day");
        expectedClaim.setRoomClass("classy room");
        expectedClaim.setId(claimId);

        User user = getUsers().get(0);
        expectedClaim.setUser(user);

        when(claimRepositoryMock.findById(claimId)).thenReturn(Optional.of(ClaimMapper.mapFromDto(expectedClaim)));
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, userRepositoryMock);

        ClaimDto result = claimService.getClaimById(claimId);

        assertEquals(result.getRoomClass(), expectedClaim.getRoomClass());
        assertEquals(result.getRoomClass(), expectedClaim.getRoomClass());
        assertEquals(result.getId(), expectedClaim.getId());
    }

    @Test
    void testGetClaimsByUserId_whenCalled_callsRepo() {
        int userId = 7890;
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);
        claimService.getClaimsByUserId(userId);
        verify(claimRepositoryMock, times(1)).findClaimsByUserId(userId);
    }

    @Test
    void testGetUserDetailedClaims_returnsCorrectData() throws AppException {
        int expectedUserId = 1;
        ArrayList<Claim> claimDb = getClaims();
        when(claimRepositoryMock.findClaimsByUserId(expectedUserId)).thenReturn(claimDb);

        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);
        List<ClaimClientDto> result = claimService.getUserDetailedClaims(expectedUserId);

        for (ClaimClientDto claimDetails : result) {
            Claim expectedClaim = claimDb.stream()
                    .filter(apl -> apl.getId() == claimDetails.getId())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementFoundException("Can't get claim with id = " + claimDetails.getId()));
            assertEquals(claimDetails.getCheckinDate(), expectedClaim.getCheckinDate());
            assertEquals(claimDetails.getCheckoutDate(), expectedClaim.getCheckoutDate());
            assertEquals(claimDetails.getRoomClass(), expectedClaim.getRoomClass());
            assertEquals(claimDetails.getRoomSeats(), expectedClaim.getRoomSeats());
        }
    }

    @Test
    void testGetUserDetailedClaims_whenNoClaims_returnsEmptyCollections() throws AppException {
        int userId = 1;
        when(claimRepositoryMock.findClaimsByUserId(userId)).thenReturn(Collections.emptyList());

        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, userRepositoryMock);
        List<ClaimClientDto> result = claimService.getUserDetailedClaims(userId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserDetailedClaims_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int userId = 1;
        when(claimRepositoryMock.findClaimsByUserId(userId)).thenReturn(null);
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);

        try {
            claimService.getUserDetailedClaims(userId);
        } catch (AppException ex) {
            assertEquals("Can't retrieve client's claims to show in the client's account", ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetAllDetailedClaims_returnsCorrectData() throws AppException {

        List<Claim> claimDb = getClaims();
        when(claimRepositoryMock.findAll()).thenReturn(claimDb);

        List<User> userDb = getUsers();
        when(userRepositoryMock.findUsersByIds(anyList())).thenReturn(userDb);

        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, userRepositoryMock);
        List<ClaimManagerDto> result = claimService.getAllDetailedClaims();

        for (ClaimManagerDto claimDetails : result) {
            Claim expectedClaim = claimDb.stream().filter(c -> c.getId() == claimDetails.getId()).findFirst().get();
            assertEquals(claimDetails.getCheckinDate(), expectedClaim.getCheckinDate());
            assertEquals(claimDetails.getCheckoutDate(), expectedClaim.getCheckoutDate());
            assertEquals(claimDetails.getRoomClass(), expectedClaim.getRoomClass());
            assertEquals(claimDetails.getRoomSeats(), expectedClaim.getRoomSeats());

            User expectedUser = userDb.stream().filter(u -> Objects.equals(u.getEmail(), claimDetails.getBookedByUserEmail())).findFirst().get();
            assertEquals(claimDetails.getBookedByUser(), expectedUser.getFirstName() + ' ' + expectedUser.getLastName());
        }
    }

    @Test
    void testGetAllDetailedClaims_whenNoClaims_returnsEmptyCollections() throws AppException {
        when(claimRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);
        List<ClaimManagerDto> result = claimService.getAllDetailedClaims();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedClaims_whenNoClaims_userRepoNotCalled() throws AppException {
        when(claimRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        verify(userRepositoryMock, times(0)).findUsersByIds(anyList());

        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, userRepositoryMock);
        List<ClaimManagerDto> result = claimService.getAllDetailedClaims();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedClaims_whenRepoThrows_throwsException() {
        when(claimRepositoryMock.findAll()).thenReturn(null);
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);

        Assertions.assertThrowsExactly(AppException.class, claimService::getAllDetailedClaims);
    }

    @Test
    void testGetAllDetailedClaims_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        when(claimRepositoryMock.findAll()).thenReturn(null);
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);

        try {
            claimService.getAllDetailedClaims();
        } catch (AppException ex) {
            assertEquals("Can't retrieve all claims to show in the manager's account", ex.getMessage());
            assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testRemoveClaim_whenCalled_RepoCalled() {
        int claimId = 123;
        ClaimServiceImpl claimService = new ClaimServiceImpl(claimRepositoryMock, null);
        claimService.removeClaim(claimId);
        verify(claimRepositoryMock, times(1)).deleteById(claimId);
    }

    private ArrayList<Claim> getClaims() {

        List<User> users = getUsers();

        ArrayList<Claim> claimDb = new ArrayList<>();
        Claim claim1 = new Claim();
        claim1.setId(111);
        claim1.setCheckinDate(LocalDate.MIN);
        claim1.setCheckoutDate(LocalDate.MAX);
        claim1.setRoomSeats("double");
        claim1.setUserId(users.get(0));
        claim1.setRoomClass("aaaa");

        Claim claim2 = new Claim();
        claim2.setId(222);
        claim2.setCheckinDate(LocalDate.now().plusDays(1));
        claim2.setCheckinDate(LocalDate.now().plusDays(3));
        claim2.setRoomSeats("twin");
        claim2.setUserId(users.get(1));
        claim2.setRoomClass("does not matter");

        claimDb.add(claim1);
        claimDb.add(claim2);

        return claimDb;
    }

    private List<User> getUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setFirstName("UserFirstName");
        user1.setLastName("UserLastName");
        user1.setEmail("aaa@bbb.ccc");

        User user2 = new User();
        user2.setId(2);
        user2.setFirstName("AAAAA");
        user2.setLastName("BBBB");
        user2.setEmail("writing.tests@is.timeconsuming");

        List<User> userDb = new ArrayList<>();
        userDb.add(user1);
        userDb.add(user2);

        return userDb;
    }

}
