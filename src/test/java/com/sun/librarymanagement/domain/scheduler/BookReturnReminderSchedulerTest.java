package com.sun.librarymanagement.domain.scheduler;

import com.sun.librarymanagement.data.TestDataProvider;
import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BorrowRequestRepository;
import com.sun.librarymanagement.domain.service.BorrowRequestService;
import com.sun.librarymanagement.exception.AppError;
import com.sun.librarymanagement.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.sun.librarymanagement.data.TestDataProvider.defaultUserEntity;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReturnReminderSchedulerTest {

    @Mock
    private BorrowRequestRepository borrowRequestRepository;

    @Mock
    private BorrowRequestService borrowRequestService;

    @InjectMocks
    private BookReturnReminderScheduler scheduler;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(scheduler, "daysBeforeDue", 1);
    }

    @Test
    void testSendBookReturnReminders_withNoBorrowers() {
        LocalDate expectedDate = LocalDate.now().plusDays(1);
        when(borrowRequestRepository.findBorrowersWithBooksDueOn(expectedDate)).thenReturn(Collections.emptyList());
        scheduler.sendBookReturnReminders();
        verify(borrowRequestRepository, times(1)).findBorrowersWithBooksDueOn(expectedDate);
        verifyNoInteractions(borrowRequestService);
    }

    @Test
    void testSendBookReturnReminders_withOneBorrower() {
        LocalDate expectedDate = LocalDate.now().plusDays(1);
        UserEntity user = defaultUserEntity();
        when(borrowRequestRepository.findBorrowersWithBooksDueOn(expectedDate))
            .thenReturn(List.of(user));
        scheduler.sendBookReturnReminders();
        verify(borrowRequestService).sendBookReturnReminderEmail(user.getUsername(), user.getEmail());
    }

    @Test
    void testSendBookReturnReminders_whenEmailSendFails() {
        LocalDate expectedDate = LocalDate.now().plusDays(1);
        UserEntity user = defaultUserEntity();
        when(borrowRequestRepository.findBorrowersWithBooksDueOn(expectedDate))
            .thenReturn(List.of(user));
        doThrow(new AppException(AppError.USER_NOT_FOUND)).when(borrowRequestService)
            .sendBookReturnReminderEmail(any(), any());
        scheduler.sendBookReturnReminders();
        verify(borrowRequestService).sendBookReturnReminderEmail(user.getUsername(), user.getEmail());
    }
}
