package com.snm.security.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CertificateJobsRunner {
    private final CertificationCentersUpdate certificationCentersUpdate;
    private final UserCertificatesCheck userCertificatesCheck;

    @Value("${update.certifications.job.is.switch.on}")
    private boolean updateCertificationCentersJobEnabled;

    @Value("${update.certifications.job.is.switch.on}")
    private boolean checkUserCertificatesJobEnabled;

    @Autowired
    public CertificateJobsRunner(CertificationCentersUpdate certificationCentersUpdate, UserCertificatesCheck userCertificatesCheck) {
        this.certificationCentersUpdate = certificationCentersUpdate;
        this.userCertificatesCheck = userCertificatesCheck;
    }

    @Scheduled(cron = "${update.certifications.cron}")
    public void updateCertificationCenters() {
        if (updateCertificationCentersJobEnabled) {
            certificationCentersUpdate.run();
        }
    }

    @Scheduled(cron = "${certifications.check.cron}")
    public void checkUserCertificates() {
        if (checkUserCertificatesJobEnabled) {
            userCertificatesCheck.run();
        }
    }
}
