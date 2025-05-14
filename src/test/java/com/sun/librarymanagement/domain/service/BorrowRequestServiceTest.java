package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.service.impl.BorrowRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

import static com.sun.librarymanagement.data.TestDataProvider.defaultUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowRequestServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private BorrowRequestServiceImpl borrowRequestService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(borrowRequestService, "senderEmail", "noreply@example.com");
    }

    @Test
    void testSendBookReturnReminderEmail() {
        UserEntity user = defaultUserEntity();
        String email = "noreply@example.com";
        borrowRequestService.sendBookReturnReminderEmail(user.getUsername(), user.getEmail());
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(email, sentMessage.getFrom());
        assertEquals(user.getEmail(), Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Reminder: Book Return", sentMessage.getSubject());
        assertEquals("Dear " + user.getUsername() + ", you need to return your book tomorrow.", sentMessage.getText());
    }

    @Test
    void testSendReminder_withNullEmail_shouldNotSend() {
        UserEntity user = defaultUserEntity();
        borrowRequestService.sendBookReturnReminderEmail(user.getUsername(), null);
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendReminder_withBlankEmail_shouldNotSend() {
        UserEntity user = defaultUserEntity();
        borrowRequestService.sendBookReturnReminderEmail(user.getUsername(), "   ");
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendReminder_withNullUsername_shouldNotSend() {
        UserEntity user = defaultUserEntity();
        borrowRequestService.sendBookReturnReminderEmail(null, user.getEmail());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendReminder_withBlankUsername_shouldNotSend() {
        UserEntity user = defaultUserEntity();
        borrowRequestService.sendBookReturnReminderEmail("   ", user.getEmail());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}
