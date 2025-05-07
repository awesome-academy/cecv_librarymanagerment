package com.sun.librarymanagement.domain.service;

import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.BorrowRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReturnReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(BookReturnReminderScheduler.class);

    private final BorrowRequestRepository borrowRequestRepository;
    private final BorrowRequestService borrowRequestService;

    @Value("${app.book.book-return-reminder-before:1}")
    private int daysBeforeDue;

    @Transactional
    @Scheduled(cron = "${scheduling.jobs.book-return-reminder.cron:0 0 8 * * ?}")
    public void sendBookReturnReminders() {
        LocalDate targetDate = LocalDate.now().plusDays(daysBeforeDue);
        List<UserEntity> borrowers = borrowRequestRepository.findBorrowersWithBooksDueOn(targetDate);
        if (borrowers.isEmpty()) {
            logger.info("No users found with books due on {}", targetDate);
            return;
        }
        int successCount = 0;
        for (UserEntity borrower : borrowers) {
            try {
                borrowRequestService.sendBookReturnReminderEmail(
                        borrower.getUsername(),
                        borrower.getEmail()
                );
                successCount++;
            } catch (Exception e) {
                logger.warn("Failed to send reminder to {}: {}", borrower.getEmail(), e.getMessage(), e);
            }
        }
        logger.info("Book return reminders sent: {}/{}", successCount, borrowers.size());
    }
}
