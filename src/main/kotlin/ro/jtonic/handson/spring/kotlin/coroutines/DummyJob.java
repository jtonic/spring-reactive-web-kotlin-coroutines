package ro.jtonic.handson.spring.kotlin.coroutines;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DummyJob {

    @Scheduled(cron = "0 0/2 * * * ?") // run every 2 minutes
    public void run() {
        log.info("Running job");
    }
}
