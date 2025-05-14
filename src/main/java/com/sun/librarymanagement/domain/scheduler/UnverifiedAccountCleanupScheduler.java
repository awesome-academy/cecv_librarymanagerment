package com.sun.librarymanagement.domain.scheduler;

import com.sun.librarymanagement.domain.entity.UserEntity;
import com.sun.librarymanagement.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnverifiedAccountCleanupScheduler {

    private final UserRepository userRepository;

    @Value("${app.account.activation-expiry-time:24}")
    private int activationExpiryTime;

    private static final Logger logger = LoggerFactory.getLogger(UnverifiedAccountCleanupScheduler.class);

    @Transactional
    @Scheduled(cron = "${scheduling.jobs.cleanup-unverified-accounts.cron:0 0 0 * * ?}")
    public void deleteUnverifiedAccounts() {
        LocalDateTime expiryTime = LocalDateTime.now().minusHours(activationExpiryTime);
        List<UserEntity> unverifiedUsers = userRepository.findByIsVerifiedFalseAndCreatedAtBefore(expiryTime);
        if (!unverifiedUsers.isEmpty()) {
            try {
                userRepository.deleteAll(unverifiedUsers);
                logger.info("Successfully deleted {} unverified accounts.", unverifiedUsers.size());
            } catch (Exception e) {
                logger.error("Error occurred while deleting unverified accounts: {}", e.getMessage(), e);
            }
        } else {
            logger.info("No unverified accounts to delete today.");
        }
    }
}
