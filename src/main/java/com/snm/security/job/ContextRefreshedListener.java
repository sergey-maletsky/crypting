package com.snm.security.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ContextRefreshedListener implements ApplicationListener {

    @Value("${app.run.ca.update.while.starting}")
    private boolean runWhileStarting;

    private final CertificateJobsRunner jobsRunner;
    private final TaskScheduler taskScheduler;

    @Autowired
    public ContextRefreshedListener(CertificateJobsRunner jobsRunner, TaskScheduler taskScheduler) {
        this.jobsRunner = jobsRunner;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            if (runWhileStarting) {
                taskScheduler.schedule(() -> {
                    jobsRunner.updateCertificationCenters();
                    jobsRunner.checkUserCertificates();
                }, new Date());
            }
        }
    }
}