package com.sun.librarymanagement.domain.scheduler;

import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.UserRepository;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.sun.librarymanagement.data.TestDataProvider.defaultUserEntity;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnverifiedAccountCleanupSchedulerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UnverifiedAccountCleanupScheduler scheduler;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(scheduler, "activationExpiryTime", 24);
    }

    @Test
    void testDeleteUnverifiedAccounts_withUsers_shouldDelete() {
        LocalDateTime expectedTime = LocalDateTime.now().minusHours(24);
        UserEntity user = defaultUserEntity();
        when(userRepository.findByIsVerifiedFalseAndCreatedAtBefore(any())).thenReturn(List.of(user));
        scheduler.deleteUnverifiedAccounts();
        verify(userRepository).findByIsVerifiedFalseAndCreatedAtBefore(argThat(
            time -> time.isBefore(LocalDateTime.now()) && time.isAfter(expectedTime.minusMinutes(1))
        ));
        verify(userRepository).deleteAll(List.of(user));
    }

    @Test
    void testDeleteUnverifiedAccounts_noUsers_shouldNotDelete() {
        when(userRepository.findByIsVerifiedFalseAndCreatedAtBefore(any()))
            .thenReturn(Collections.emptyList());
        scheduler.deleteUnverifiedAccounts();
        verify(userRepository, never()).deleteAll(any());
    }

    @Test
    void testDeleteUnverifiedAccounts_deleteThrowsException_shouldLogError() {
        UserEntity user = new UserEntity();
        when(userRepository.findByIsVerifiedFalseAndCreatedAtBefore(any()))
            .thenReturn(List.of(user));
        doThrow(new AppException(AppError.USER_NOT_FOUND)).when(userRepository).deleteAll(any());
        scheduler.deleteUnverifiedAccounts();
        verify(userRepository).deleteAll(List.of(user));
    }
}
