package com.epam.javacourse.hotelapp.service;

import com.epam.javacourse.hotelapp.dto.ConfirmationRequestClientDto;
import com.epam.javacourse.hotelapp.dto.ConfirmationRequestDto;
import com.epam.javacourse.hotelapp.dto.ConfirmationRequestManagerDto;
import com.epam.javacourse.hotelapp.exception.AppException;
import com.epam.javacourse.hotelapp.exception.NoSuchElementFoundException;
import com.epam.javacourse.hotelapp.model.Claim;
import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
import com.epam.javacourse.hotelapp.model.Room;
import com.epam.javacourse.hotelapp.model.User;
import com.epam.javacourse.hotelapp.repository.ConfirmRequestRepository;
import com.epam.javacourse.hotelapp.repository.UserRepository;
import com.epam.javacourse.hotelapp.service.impl.ConfirmRequestServiceImpl;
import com.epam.javacourse.hotelapp.utils.mappers.ConfirmationRequestMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmRequestServiceImplTest {

    @Mock
    private ConfirmRequestRepository confirmRequestRepoMock;

    @Mock
    private UserRepository userRepositoryMock;


    @Test
    void testCreate_whenCalled_callsRepo() {
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        confirmRequestService.createConfirmRequest(new ConfirmationRequestDto());
        verify(confirmRequestRepoMock, times(1)).save(any(ConfirmationRequest.class));
    }

    @Test
    void testGetAllDetailedConfirmRequests_returnsCorrectData() throws AppException {

        List<ConfirmationRequest> confirmRequestDb = EntityHelperForTests.getConfirmRequests();
        confirmRequestDb.get(0).setUserId(EntityHelperForTests.getUsers().get(0));
        confirmRequestDb.get(1).setUserId(EntityHelperForTests.getUsers().get(1));
        confirmRequestDb.get(0).setClaimId(EntityHelperForTests.getClaims().get(0));
        confirmRequestDb.get(1).setClaimId(EntityHelperForTests.getClaims().get(1));
        confirmRequestDb.get(0).setRoomId(EntityHelperForTests.getRooms().get(0));
        confirmRequestDb.get(1).setRoomId(EntityHelperForTests.getRooms().get(1));

        when(confirmRequestRepoMock.findAll()).thenReturn(confirmRequestDb);

        List<User> userDb = EntityHelperForTests.getUsers();

        when(userRepositoryMock.findUsersByIds(anyList())).thenReturn(userDb);

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(
                confirmRequestRepoMock, userRepositoryMock, null);
        List<ConfirmationRequestManagerDto> result = confirmRequestService.getAllDetailedConfirmRequests();

        for (ConfirmationRequestManagerDto confirmRequestDetails : result) {
            ConfirmationRequest expectedConfirmRequest = confirmRequestDb.stream()
                    .filter(cr -> cr.getId() == confirmRequestDetails.getId()).findFirst().get();
            assertEquals(confirmRequestDetails.getConfirmRequestDate(), expectedConfirmRequest.getConfirmRequestDate());
            assertEquals(confirmRequestDetails.getClaimId(), expectedConfirmRequest.getClaimId().getId());
            assertEquals(confirmRequestDetails.getRoomId(), expectedConfirmRequest.getRoomId().getId());

            User expectedUser = userDb.stream().filter(u -> Objects.equals(u.getEmail(), confirmRequestDetails.getBookedByUserEmail())).findFirst().get();
            assertEquals(confirmRequestDetails.getBookedByUser(), expectedUser.getFirstName() + ' ' + expectedUser.getLastName());
        }
    }

    @Test
    void testGetAllDetailedConfirmRequests_whenNoConfirmRequests_returnsEmptyCollections() throws AppException {
        when(confirmRequestRepoMock.findAll()).thenReturn(Collections.emptyList());

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);
        List<ConfirmationRequestManagerDto> result = confirmRequestService.getAllDetailedConfirmRequests();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedConfirmRequests_whenNoConfirmRequests_userRepoNotCalled() throws AppException {
        when(confirmRequestRepoMock.findAll()).thenReturn(Collections.emptyList());
        verify(userRepositoryMock, times(0)).findUsersByIds(anyList());

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                userRepositoryMock, null);
        List<ConfirmationRequestManagerDto> result = confirmRequestService.getAllDetailedConfirmRequests();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllDetailedConfirmRequests_whenRepoThrows_throwsException() {
        when(confirmRequestRepoMock.findAll()).thenReturn(null);
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(
                confirmRequestRepoMock, null, null);

        Assertions.assertThrowsExactly(AppException.class, confirmRequestService::getAllDetailedConfirmRequests);
    }

    @Test
    void testGetAllDetailedConfirmRequests_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        when(confirmRequestRepoMock.findAll()).thenReturn(null);
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(
                confirmRequestRepoMock, null, null);

        try {
            confirmRequestService.getAllDetailedConfirmRequests();
        } catch (AppException ex) {
            assertEquals("Can't retrieve all confirmation requests to show in the manager's account", ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetUserDetailedConfirmRequests_whenNoConfirmRequests_returnsEmptyCollections() throws AppException {
        int userId = 1;
        when(confirmRequestRepoMock.findConfirmationRequestsByUserId(userId)).thenReturn(Collections.emptyList());

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                userRepositoryMock, null);
        List<ConfirmationRequestClientDto> result = confirmRequestService.getUserDetailedConfirmRequests(userId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserDetailedConfirmRequests_whenRepoThrows_throwsException() {
        int userId = 1;
        when(confirmRequestRepoMock.findConfirmationRequestsByUserId(userId)).thenReturn(null);
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.getUserDetailedConfirmRequests(userId));
    }


    @Test
    void testGetUserDetailedConfirmRequests_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int userId = 1;
        when(confirmRequestRepoMock.findConfirmationRequestsByUserId(userId)).thenReturn(null);
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        try {
            confirmRequestService.getUserDetailedConfirmRequests(userId);
        } catch (AppException ex) {
            assertEquals("Can't retrieve client's confirmation requests to show in the client's account", ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetConfirmRequestById_whenCalled_RepoCalled() {
        int confirmRequestId = 112567890;
        ConfirmationRequest confirmRequest = EntityHelperForTests.getConfirmRequests().get(0);
        User user = EntityHelperForTests.getUsers().get(0);
        Claim claim = EntityHelperForTests.getClaims().get(0);
        Room room = EntityHelperForTests.getRooms().get(0);
        confirmRequest.setUserId(user);
        confirmRequest.setClaimId(claim);
        confirmRequest.setRoomId(room);
        when(confirmRequestRepoMock.findById(any())).thenReturn(Optional.of(confirmRequest));

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        confirmRequestService.getConfirmRequestById(confirmRequestId);

        verify(confirmRequestRepoMock, times(1)).findById(confirmRequestId);
    }

    @Test
    void testGetConfirmRequestById_whenRepoThrows_throwsException() {
        int confirmRequestId = 112567890;

        when(confirmRequestRepoMock.findById(confirmRequestId)).thenThrow(new NoSuchElementFoundException("Can't get invoice with id = " + confirmRequestId));
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        Assertions.assertThrowsExactly(NoSuchElementFoundException.class, () -> confirmRequestService.getConfirmRequestById(confirmRequestId));
    }

    @Test
    void testGetConfirmRequestById_whenRepoThrows_ShowExceptionMessage() {
        String messageNotToGet = "aaaaa";
        int confirmRequestId = 112567890;

        when(confirmRequestRepoMock.findById(confirmRequestId)).thenReturn(Optional.empty());
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        try {
            confirmRequestService.getConfirmRequestById(confirmRequestId);
        } catch (Exception ex) {
            assertEquals("Can't retrieve confirmation request with id = " + confirmRequestId, ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testGetConfirmRequestById_whenCalled_returnsCorrectConfirmRequest() {
        int confirmRequestId = 112567890;
        ConfirmationRequestDto expectedConfirmRequest = new ConfirmationRequestDto();
        expectedConfirmRequest.setStatus("new");
        expectedConfirmRequest.setId(confirmRequestId);
        expectedConfirmRequest.setUser(EntityHelperForTests.getUsers().get(0));
        expectedConfirmRequest.setClaim(EntityHelperForTests.getClaims().get(0));
        expectedConfirmRequest.setRoom(EntityHelperForTests.getRooms().get(0));
        when(confirmRequestRepoMock.findById(confirmRequestId))
                .thenReturn(Optional.of(ConfirmationRequestMapper.mapFromDto(expectedConfirmRequest)));
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);

        ConfirmationRequestDto result = confirmRequestService.getConfirmRequestById(confirmRequestId);

        assertEquals(result.getStatus(), expectedConfirmRequest.getStatus());
        assertEquals(result.getId(), expectedConfirmRequest.getId());
    }

    @Test
    void testGetConfirmRequestDueDate_returnsCorrectData() {
        ConfirmationRequestDto confirmRequest = new ConfirmationRequestDto();
        confirmRequest.setConfirmRequestDate(LocalDate.of(2022, 3, 10));
        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);
        LocalDate result = confirmRequestService.getConfirmRequestDueDate(confirmRequest);
        assertEquals(LocalDate.of(2022, 3, 12), result);
    }

    @Test
    void testConfirmRequestByClient_whenRepoThrows_throwsException() {

        List<ConfirmationRequest> confirmationRequestDb = EntityHelperForTests.getConfirmRequests();
        ConfirmationRequest confirmRequest = confirmationRequestDb.get(0);
        when(confirmRequestRepoMock.findById(confirmRequest.getId())).thenReturn(Optional.empty());

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);
        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.confirmRequestByClient(
                confirmRequest.getId()));
    }

    @Test
    void testConfirmRequestByClient_whenRepoThrows_showExceptionMessage() {
        String messageNotToGet = "aaaaa";
        when(confirmRequestRepoMock.findById(any(Integer.class))).thenReturn(null);
        List<ConfirmationRequest> confirmationRequestDb = EntityHelperForTests.getConfirmRequests();

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                null, null);
        ConfirmationRequest confirmRequest = confirmationRequestDb.stream().findFirst().get();

        try {
            confirmRequestService.confirmRequestByClient(confirmRequest.getId());
        } catch (AppException ex) {
            assertEquals("Can't update confirmation request's status to 'confirmed'", ex.getMessage());
            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
            return;
        }

        Assertions.fail("Should have thrown AppException");
    }

    @Test
    void testConfirmRequestByClient_whenCalled_callsRepo() throws AppException {

        ConfirmationRequest confirmRequest = EntityHelperForTests.getConfirmRequests().get(0);
        confirmRequest.setUserId(EntityHelperForTests.getUsers().get(0));
        confirmRequest.setClaimId(EntityHelperForTests.getClaims().get(0));
        confirmRequest.setRoomId((EntityHelperForTests.getRooms().get(0)));
        when(confirmRequestRepoMock.findById(confirmRequest.getId())).thenReturn(Optional.of(confirmRequest));

        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
                userRepositoryMock, null);

        confirmRequestService.confirmRequestByClient(confirmRequest.getId());
        verify(confirmRequestRepoMock, times(1))
                .updateConfirmRequestStatus("confirmed", confirmRequest.getId());
    }

}
