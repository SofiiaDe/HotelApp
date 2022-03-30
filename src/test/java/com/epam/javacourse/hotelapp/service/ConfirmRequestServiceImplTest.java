//package com.epam.javacourse.hotelapp.service;
//
//import com.epam.javacourse.hotelapp.dto.ConfirmationRequestDto;
//import com.epam.javacourse.hotelapp.dto.ConfirmationRequestManagerDto;
//import com.epam.javacourse.hotelapp.exception.AppException;
//import com.epam.javacourse.hotelapp.model.ConfirmationRequest;
//import com.epam.javacourse.hotelapp.model.User;
//import com.epam.javacourse.hotelapp.repository.ConfirmRequestRepository;
//import com.epam.javacourse.hotelapp.repository.UserRepository;
//import com.epam.javacourse.hotelapp.service.impl.ConfirmRequestServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//@ExtendWith(MockitoExtension.class)
//class ConfirmRequestServiceImplTest {
//
//    @Mock
//    private ConfirmRequestRepository confirmRequestRepoMock;
//
//    @Mock
//    private UserRepository userRepositoryMock;
//
//    @Test
//    void testCreate_whenRepoThrows_throwsException() {
//        when(confirmRequestRepoMock.save(any(ConfirmationRequest.class))).thenThrow(new Exception());
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
//                null, null);
//
//        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.createConfirmRequest(
//                new ConfirmationRequestDto()));
//    }
//
//    @Test
//    void testCreate_whenRepoThrows_doesNotShowDbExceptionMessage() {
//        String messageNotToGet = "aaaaa";
//        when(confirmRequestRepoMock.save(any(ConfirmationRequest.class))).thenThrow(new Exception(messageNotToGet));
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
//                null, null);
//
//        try {
//            confirmRequestService.createConfirmRequest(new ConfirmationRequestDto());
//        } catch (Exception ex) {
//            Assertions.assertEquals("Can't create confirmation request", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testCreate_whenCalled_callsRepo() {
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock,
//                null, null);
//
//        confirmRequestService.createConfirmRequest(new ConfirmationRequestDto());
//        verify(confirmRequestRepoMock, times(1)).save(any(ConfirmationRequest.class));
//    }
//
//    @Test
//    void testGetAllDetailedConfirmRequests_returnsCorrectData() throws AppException {
//        // confirmation request Repo
//        List<ConfirmationRequest> confirmationRequestDb = getConfirmRequests();
//        when(confirmRequestRepoMock.findAll()).thenReturn(confirmationRequestDb);
//
//
//        // user Repo
//        User user1 = new User();
//        user1.setId(1);
//        user1.setFirstName("UserFirstName");
//        user1.setLastName("UserLastName");
//        user1.setEmail("aaa@bbb.ccc");
//        User user2 = new User();
//        user2.setId(2);
//        user2.setFirstName("AAAAA");
//        user2.setLastName("BBBB");
//        user2.setEmail("writing.tests@is.timeconsuming");
//
//        List<User> userDb = new ArrayList<>();
//        userDb.add(user1);
//        userDb.add(user2);
//        when(userRepositoryMock.findUsersByIds(anyList())).thenReturn(userDb);
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(
//                confirmRequestRepoMock, userRepositoryMock, null);
//        List<ConfirmationRequestManagerDto> result = confirmRequestService.getAllDetailedConfirmRequests();
//
//        for (ConfirmationRequestManagerDto confirmRequestDetails : result) {
//            ConfirmationRequest expectedConfirmRequest = confirmationRequestDb.stream()
//                    .filter(cr -> cr.getId() == confirmRequestDetails.getId()).findFirst().get();
//            Assertions.assertEquals(confirmRequestDetails.getConfirmRequestDate(), expectedConfirmRequest.getConfirmRequestDate());
//            Assertions.assertEquals(confirmRequestDetails.getApplicationId(), expectedConfirmRequest.getApplicationId());
//            Assertions.assertEquals(confirmRequestDetails.getRoomId(), expectedConfirmRequest.getRoomId());
//
//            User expectedUser = userDb.stream().filter(u -> Objects.equals(u.getEmail(), confirmRequestDetails.getBookedByUserEmail())).findFirst().get();
//            Assertions.assertEquals(confirmRequestDetails.getBookedByUser(), expectedUser.getFirstName() + ' ' + expectedUser.getLastName());
//        }
//    }
//
//    @Test
//    void testGetAllDetailedConfirmRequests_whenNoConfirmRequests_returnsEmptyCollections() throws AppException {
//        when(confirmRequestRepoMock.findAll()).thenReturn(Collections.emptyList());
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//        List<ConfirmationRequestManagerDto> result = confirmRequestService.getAllDetailedConfirmRequests();
//
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testGetAllDetailedConfirmRequests_whenNoConfirmRequests_userRepoNotCalled() throws AppException {
//        when(confirmRequestRepoMock.findAll()).thenReturn(Collections.emptyList());
//        verify(userRepositoryMock, times(0)).findUsersByIds(anyList());
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, userRepositoryMock);
//        List<ConfirmationRequestManagerDto> result = confirmRequestService.getAllDetailedConfirmRequests();
//
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testGetAllDetailedConfirmRequests_whenRepoThrows_throwsException() {
//        when(confirmRequestRepoMock.findAll()).thenReturn(null);
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(
//                confirmRequestRepoMock, null, null);
//
//        Assertions.assertThrowsExactly(AppException.class, confirmRequestService::getAllDetailedConfirmRequests);
//    }
//
//    @Test
//    void testGetAllDetailedConfirmRequests_whenRepoThrows_doesNotShowDbExceptionMessage() {
//        String messageNotToGet = "aaaaa";
//        when(confirmRequestRepoMock.findAll()).thenReturn(null);
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(
//                confirmRequestRepoMock, null, null);
//
//        try {
//            confirmRequestService.getAllDetailedConfirmRequests();
//        } catch (AppException ex) {
//            Assertions.assertEquals("Can't retrieve all confirmation requests to show in the manager's account", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testGetUserDetailedConfirmRequests_whenNoConfirmRequests_returnsEmptyCollections() throws AppException {
//        int userId = 1;
//        when(confirmRequestRepoMock.findConfirmRequestsByUserId(userId)).thenReturn(Collections.emptyList());
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, userRepositoryMock);
//        List<UserConfirmationRequestDetailed> result = confirmRequestService.getUserDetailedConfirmRequests(userId);
//
//        Assertions.assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testGetUserDetailedConfirmRequests_whenRepoThrows_throwsException() throws AppException {
//        int userId = 1;
//        when(confirmRequestRepoMock.findConfirmRequestsByUserId(userId)).thenThrow(new DBException());
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.getUserDetailedConfirmRequests(userId));
//    }
//
//    @Test
//    void testGetUserDetailedConfirmRequests_whenRepoThrows_doesNotShowDbExceptionMessage() throws DBException {
//        String messageNotToGet = "aaaaa";
//        int userId = 1;
//        when(confirmRequestRepoMock.findConfirmRequestsByUserId(userId)).thenThrow(new DBException(messageNotToGet));
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        try {
//            confirmRequestService.getUserDetailedConfirmRequests(userId);
//        } catch (AppException ex) {
//            Assertions.assertEquals("Can't retrieve client's confirmation requests to show in the client's account", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testGetConfirmRequestsByUserId_whenCalled_RepoCalled() throws AppException {
//        int userId = 7890;
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        confirmRequestService.getConfirmRequestsByUserId(userId);
//
//        verify(confirmRequestRepoMock, times(1)).findConfirmRequestsByUserId(userId);
//    }
//
//    @Test
//    void testGetConfirmRequestsByUserId_whenRepoThrows_throwsException() throws AppException {
//        int userId = 7890;
//        when(confirmRequestRepoMock.findConfirmRequestsByUserId(userId)).thenThrow(new DBException());
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.getConfirmRequestsByUserId(userId));
//    }
//
//    @Test
//    void testGetConfirmRequestsByUserId_whenRepoThrows_doesNotShowDbExceptionMessage() throws DBException {
//        String messageNotToGet = "aaaaa";
//        int userId = 0;
//        when(confirmRequestRepoMock.findConfirmRequestsByUserId(userId)).thenThrow(new DBException(messageNotToGet));
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        try {
//            confirmRequestService.getConfirmRequestsByUserId(userId);
//        } catch (AppException ex) {
//            Assertions.assertEquals("Can't retrieve client's confirmation requests", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testDeleteConfirmRequestById_whenCalled_RepoCalled() throws AppException {
//        int confirmRequestId = 123;
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        confirmRequestService.deleteConfirmRequestById(confirmRequestId);
//
//        verify(confirmRequestRepoMock, times(1)).deleteConfirmRequestById(confirmRequestId);
//    }
//
//    @Test
//    void testDeleteConfirmRequestById_whenRepoThrows_throwsException() throws AppException {
//        int confirmRequestId = 123;
//        doThrow(new DBException()).when(confirmRequestRepoMock).deleteConfirmRequestById(confirmRequestId);
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.deleteConfirmRequestById(confirmRequestId));
//    }
//
//    @Test
//    void testDeleteConfirmRequestById_whenRepoThrows_doesNotShowDbExceptionMessage() throws DBException {
//        String messageNotToGet = "aaaaa";
//        int confirmRequestId = 12321311;
//        doThrow(new DBException(messageNotToGet)).when(confirmRequestRepoMock).deleteConfirmRequestById(confirmRequestId);
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        try {
//            confirmRequestService.deleteConfirmRequestById(confirmRequestId);
//        } catch (AppException ex) {
//            Assertions.assertEquals("Can't remove confirmation request by id", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testGetConfirmRequestById_whenCalled_RepoCalled() throws AppException {
//        int confirmRequestId = 112567890;
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        confirmRequestService.getConfirmRequestById(confirmRequestId);
//
//        verify(confirmRequestRepoMock, times(1)).findConfirmRequestById(confirmRequestId);
//    }
//
//    @Test
//    void testGetConfirmRequestById_whenRepoThrows_throwsException() throws AppException {
//        int confirmRequestId = 112567890;
//
//        when(confirmRequestRepoMock.findConfirmRequestById(confirmRequestId)).thenThrow(new DBException());
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.getConfirmRequestById(confirmRequestId));
//    }
//
//    @Test
//    void testGetConfirmRequestById_whenRepoThrows_doesNotShowDbExceptionMessage() throws DBException {
//        String messageNotToGet = "aaaaa";
//        int confirmRequestId = 112567890;
//
//        when(confirmRequestRepoMock.findConfirmRequestById(confirmRequestId)).thenThrow(new DBException(messageNotToGet));
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        try {
//            confirmRequestService.getConfirmRequestById(confirmRequestId);
//        } catch (AppException ex) {
//            Assertions.assertEquals("Can't retrieve confirmation request by id", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testGetConfirmRequestById_whenCalled_returnsCorrectConfirmRequest() throws AppException {
//        int confirmRequestId = 112567890;
//        ConfirmationRequest expectedConfirmRequest = new ConfirmationRequest();
//        expectedConfirmRequest.setApplicationId(2409);
//        expectedConfirmRequest.setStatus("new");
//        expectedConfirmRequest.setId(confirmRequestId);
//        when(confirmRequestRepoMock.findConfirmRequestById(confirmRequestId)).thenReturn(expectedConfirmRequest);
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        ConfirmationRequest result = confirmRequestService.getConfirmRequestById(confirmRequestId);
//
//        Assertions.assertEquals(result, expectedConfirmRequest);
//    }
//
//    @Test
//    void testGetConfirmRequestDueDate_returnsCorrectData() {
//        ConfirmationRequest confirmRequest = new ConfirmationRequest();
//        confirmRequest.setConfirmRequestDate(LocalDate.of(2022, 3, 10));
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//        LocalDate result = confirmRequestService.getConfirmRequestDueDate(confirmRequest);
//        Assertions.assertEquals(LocalDate.of(2022, 3, 12), result);
//    }
//
//    @Test
//    void testConfirmRequestByClient_whenRepoThrows_throwsException() throws DBException {
//
//        List<ConfirmationRequest> confirmationRequestDb = getConfirmRequests();
//        ConfirmationRequest confirmRequest = confirmationRequestDb.stream().findFirst().get();
//        when(confirmRequestRepoMock.findConfirmRequestById(confirmRequest.getId())).thenThrow(new DBException());
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//        Assertions.assertThrowsExactly(AppException.class, () -> confirmRequestService.confirmRequestByClient(
//                confirmRequest.getId()));
//    }
//
//    @Test
//    void testConfirmRequestByClient_whenRepoThrows_doesNotShowDbExceptionMessage() throws DBException {
//        String messageNotToGet = "aaaaa";
//        when(confirmRequestRepoMock.findConfirmRequestById(any(Integer.class))).thenThrow(new DBException(messageNotToGet));
//        List<ConfirmationRequest> confirmationRequestDb = getConfirmRequests();
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//        ConfirmationRequest confirmRequest = confirmationRequestDb.stream().findFirst().get();
//
//        try {
//            confirmRequestService.confirmRequestByClient(confirmRequest.getId());
//        } catch (AppException ex) {
//            Assertions.assertEquals("Can't update confirmation request's status to 'confirmed'", ex.getMessage());
//            Assertions.assertNotEquals(messageNotToGet, ex.getMessage());
//            return;
//        }
//
//        Assertions.fail("Should have thrown AppException");
//    }
//
//    @Test
//    void testConfirmRequestByClient_whenCalled_callsRepo() throws AppException {
//
//        List<ConfirmationRequest> confirmationRequestDb = getConfirmRequests();
//        ConfirmationRequest confirmRequest = confirmationRequestDb.stream().findFirst().get();
//        when(confirmRequestRepoMock.findConfirmRequestById(confirmRequest.getId())).thenReturn(confirmRequest);
//
//        ConfirmRequestServiceImpl confirmRequestService = new ConfirmRequestServiceImpl(confirmRequestRepoMock, null);
//
//        confirmRequestService.confirmRequestByClient(confirmRequest.getId());
//        verify(confirmRequestRepoMock, times(1)).updateConfirmRequestStatus(confirmRequest);
//    }
//
//    private List<ConfirmationRequest> getConfirmRequests() {
//        // ConfirmationRequest Repo
//        List<ConfirmationRequest> confirmationRequestDb = new ArrayList<>();
//        ConfirmationRequest confirmRequest = new ConfirmationRequest();
//        confirmRequest.setId(111);
//        confirmRequest.setConfirmRequestDate(LocalDate.MIN);
//        confirmRequest.setConfirmRequestDueDate(LocalDate.MAX);
//        confirmRequest.setRoomId(155);
//        confirmRequest.setApplicationId(444);
//        confirmRequest.setUserId(1);
//        confirmRequest.setStatus("someStatus");
//
//        ConfirmationRequest confirmRequest2 = new ConfirmationRequest();
//        confirmRequest2.setId(222);
//        confirmRequest2.setConfirmRequestDate(LocalDate.now().plusDays(1));
//        confirmRequest2.setConfirmRequestDueDate(LocalDate.now().plusDays(3));
//        confirmRequest.setRoomId(166);
//        confirmRequest.setApplicationId(555);
//        confirmRequest2.setUserId(2);
//        confirmRequest2.setStatus("does not matter");
//
//        confirmationRequestDb.add(confirmRequest);
//        confirmationRequestDb.add(confirmRequest2);
//
//        return confirmationRequestDb;
//    }
//}
