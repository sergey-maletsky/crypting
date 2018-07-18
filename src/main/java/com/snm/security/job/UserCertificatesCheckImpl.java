package com.snm.security.job;

import com.snm.security.repository.UserCertificateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class UserCertificatesCheckImpl implements UserCertificatesCheck {

    private final UserCertificateRepository userCertificateRepository;

    private static final Logger log = LoggerFactory.getLogger(UserCertificatesCheckImpl.class);

    @Autowired
    public UserCertificatesCheckImpl(UserCertificateRepository userCertificateRepository) {
        this.userCertificateRepository = userCertificateRepository;
    }

    public void run() {
        log.info("Run job for user certificates check in " + new Date(System.currentTimeMillis()));
        int updatedEntitiesCount = userCertificateRepository.updateRevokedCertificates();

        log.error("End job for user certificates check in " + new Date(System.currentTimeMillis()) + ", " + updatedEntitiesCount + " certificates were updated");
    }
}