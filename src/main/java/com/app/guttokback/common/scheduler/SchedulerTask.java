package com.app.guttokback.common.scheduler;

import com.app.guttokback.email.application.service.ReminderService;
import com.app.guttokback.user.application.service.TestAccountResetService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SchedulerTask {

    private final ReminderService reminderService;
    private final TestAccountResetService testAccountResetService;

    @Scheduled(zone = "Asia/Seoul", cron = "0 0 9 * * ?") // 타임존 서울, 매일 09시 실행
    @SchedulerLock(name = "reminder", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void sendReminder() {
        reminderService.sendReminder(LocalDate.now());
    }

    @Scheduled(zone = "Asia/Seoul", cron = "0 0 0 * * ?")  // 매일 00시 실행
    @SchedulerLock(name = "resetTestAccounts", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void resetTestUsers() {
        testAccountResetService.resetTestAccounts();
    }
}
