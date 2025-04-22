package com.app.guttokback.common.scheduler;

import com.app.guttokback.email.application.service.ReminderService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SchedulerTask {

    private final ReminderService reminderService;

    //    @Scheduled(zone = "Asia/Seoul", cron = "0 0 9 * * ?") // 타임존 서울, 매일 09시 실행
    @Scheduled(zone = "Asia/Seoul", cron = "0 * * * * ?")
    @SchedulerLock(name = "reminder", lockAtMostFor = "1h", lockAtLeastFor = "5m")
    public void sendReminder() {
        reminderService.sendReminder(LocalDate.now());
    }
    /*
     * 애플리케이션 실행 시 주기적인 동작으로 임시 주석처리
     * 테스트 시 주석 해제 후 애플리케이션 실행
     * */

}
