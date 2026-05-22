package ro.jtonic.handson.spring.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DummyJob {

    private static final Logger log = LoggerFactory.getLogger(DummyJob.class);

    @Scheduled(cron = "0 0/2 * * * ?") // run every 2 minutes
    public void run() {
        log.info("Running job");
    }
}
