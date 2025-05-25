package com.app.guttokback.common.scheduler;

import com.app.guttokback.email.application.service.ReminderService;
import com.app.guttokback.user.application.service.TestAccountService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SchedulerTask {

    private final ReminderService reminderService;
    private final TestAccountService testAccountService;

    /**
     * 리마인더 이메일 발송
     * 매일 09시 실행
     */
    @Scheduled(zone = "Asia/Seoul", cron = "0 0 9 * * ?")
    @SchedulerLock(name = "reminder", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void sendReminder() {
        reminderService.sendReminder(LocalDate.now());
    }

    /**
     * 테스트 계정 처리
     * 매일 00시 실행
     */
    @Scheduled(zone = "Asia/Seoul", cron = "0 0 0 * * ?")
    @SchedulerLock(name = "resetTestAccounts", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void resetTestUsers() {
        testAccountService.resetTestAccounts();
    }
}
