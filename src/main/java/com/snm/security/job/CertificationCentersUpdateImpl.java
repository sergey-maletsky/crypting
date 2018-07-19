package com.snm.security.job;

import com.google.common.io.ByteStreams;
import com.snm.security.job.document.AccreditedCertificationCenters;
import com.snm.security.job.document.AddressesListsRevocation;
import com.snm.security.job.document.CertificateData;
import com.snm.security.model.CertificateAuthority;
import com.snm.security.model.CertificateRevocationList;
import com.snm.security.model.CertificateRevocationListMeta;
import com.snm.security.model.CertifyingCenter;
import com.snm.security.repository.CertificateAuthorityRepository;
import com.snm.security.repository.CertificateRevocationListMetaRepository;
import com.snm.security.repository.CertificateRevocationListRepository;
import com.snm.security.repository.CertifyingCenterRepository;
import com.snm.security.util.ConversionUtil;
import com.snm.security.util.TransactionUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.cert.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CertificationCentersUpdateImpl implements CertificationCentersUpdate {

    private static final Logger log = LoggerFactory.getLogger(CertificationCentersUpdateImpl.class);

    private static final char QUOTATION = '<';
    @Value("${incoming.file.date.pattern}")
    private String datePattern;
    @Value("${incoming.file.encoding}")
    private String encodingName;
    private Charset encoding;
    @Value("${incoming.file.url}")
    private String urlToGetFile;

    @PersistenceContext
    private EntityManager em;

    private final CertifyingCenterRepository certifyingCenterRepository;
    private final CertificateAuthorityRepository caCertificateRepository;
    private final CertificateRevocationListMetaRepository crlMetaRepository;
    private final CertificateRevocationListRepository crlRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Autowired
    public CertificationCentersUpdateImpl(CertifyingCenterRepository certifyingCenterRepository,
                                          CertificateAuthorityRepository caCertificateRepository,
                                          CertificateRevocationListMetaRepository crlMetaRepository,
                                          CertificateRevocationListRepository crlRepository,
                                          PlatformTransactionManager platformTransactionManager) {
        this.certifyingCenterRepository = certifyingCenterRepository;
        this.caCertificateRepository = caCertificateRepository;
        this.crlMetaRepository = crlMetaRepository;
        this.crlRepository = crlRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    public void run() {

        log.debug("Run job for update revocation certificates in " + new Date(System.currentTimeMillis()));

        encoding = Charset.forName(encodingName);

        File caListFile = null;
        try {
            caListFile = fetchCaListFile();
        } catch (Exception e) {
            log.error("Error while getting document from URL.", e);
        }

        if (caListFile != null && caListFile.exists()) {
            AccreditedCertificationCenters acc = parseCaListFile(caListFile);

            if (acc != null) {
                doCertificationsUpdate(acc);
            }

            caListFile.deleteOnExit();
        }

        log.debug("End job for update revocation certificates in " + new Date(System.currentTimeMillis()));
    }

    private void doCertificationsUpdate(AccreditedCertificationCenters acc) {
        try {
            updateCertifyingCenter(acc);
        } catch (Exception e) {
            log.error("Error while updating Certifying centers. ", e);
        }

        try {
            updateCRLMeta(acc);
        } catch (Exception e) {
            log.error("Error while updating CRL meta data. ", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateCertifyingCenter(AccreditedCertificationCenters accreditedCertificationCenters) {
        List<CertifyingCenter> inBaseList = StreamSupport.stream(certifyingCenterRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<CertifyingCenter> removeList = new ArrayList<>(inBaseList);
        List<Pair<com.snm.security.job.document.CertifyingCenter, CertifyingCenter>> updateList = new ArrayList<>();

        TransactionUtil.doInTransaction(platformTransactionManager, () -> {
            for (CertifyingCenter inBaseCC : inBaseList) {
                for (com.snm.security.job.document.CertifyingCenter actualCC : accreditedCertificationCenters.centers) {
                    if (inBaseCC.getId() == -1 || (actualCC.name != null && actualCC.name.equals(inBaseCC.getName()))) {

                        removeList.remove(inBaseCC);

                        if (actualCC.urlToInf == null) {
                            log.error("Update process was interrupted. Invalid data (urlToInf == null) in: " + actualCC);
                            break;
                        }

                        inBaseCC.setUrl(actualCC.urlToInf);
                        certifyingCenterRepository.save(inBaseCC);

                        updateList.add(new Pair<>(actualCC, inBaseCC));

                        break;
                    }
                }
            }
            em.flush();

            certifyingCenterRepository.delete((CertifyingCenter) removeList);
            em.flush();
            inBaseList.removeAll(removeList);
        });

        TransactionUtil.doInTransaction(platformTransactionManager, () -> {
            for (com.snm.security.job.document.CertifyingCenter actualCC : accreditedCertificationCenters.centers) {
                boolean found = false;
                for (CertifyingCenter inBaseCC : inBaseList) {
                    if (inBaseCC.getName().equals(actualCC.name)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {

                    if (actualCC.name == null || actualCC.urlToInf == null) {
                        log.error("Create process was interrupted. Invalid data (name == null || urlToInf == null) in: " + actualCC);
                        continue;
                    }

                    CertifyingCenter newCenter = new CertifyingCenter();
                    newCenter.setName(actualCC.name);
                    newCenter.setUrl(actualCC.urlToInf);
                    certifyingCenterRepository.save(newCenter);

                    for (CertificateData actualC : actualCC.getCertificateDataList()) {

                        if (actualC.serialNumber == null || actualC.data == null || actualC.validOf == null
                                || actualC.validTo == null) {
                            log.error("Create process was interrupted. Invalid data in: " + actualC);
                            continue;
                        }

                        CertificateAuthority inBaseC = new CertificateAuthority();
                        inBaseC.setSerialNumber(ConversionUtil.hexStringToDecimalString(actualC.serialNumber));
                        inBaseC.setData(Bytes.toBytes(actualC.data));
                        inBaseC.setValidityDateFrom(parseDate(actualC.validOf));
                        inBaseC.setValidityDateTo(parseDate(actualC.validTo));
                        inBaseC.setCertifyingCenter(newCenter);

                        caCertificateRepository.save(inBaseC);
                    }
                }
            }
            em.flush();
        });

        updateList.forEach(pair -> updateCertificate(pair.getFirst(), pair.getSecond()));
    }

    private void updateCertificate(com.snm.security.job.document.CertifyingCenter actualCC, CertifyingCenter inBaseCC) {
        TransactionUtil.doInTransaction(platformTransactionManager, () -> {
            List<CertificateAuthority> removeList = new ArrayList<>();

            for (CertificateAuthority inBaseC : inBaseCC.getCertificateAuthorities()) {
                boolean found = false;
                for (CertificateData actualC : actualCC.getCertificateDataList()) {
                    if (inBaseC.getSerialNumber().equals(ConversionUtil.hexStringToDecimalString(actualC.serialNumber))) {

                        if (actualC.data == null || actualC.validOf == null || actualC.validTo == null) {
                            log.error("Update process was interrupted. Invalid data in: " + actualC);
                            break;
                        }

                        found = true;
                        inBaseC.setData(Bytes.toBytes(actualC.data));
                        inBaseC.setValidityDateFrom(parseDate(actualC.validOf));
                        inBaseC.setValidityDateTo(parseDate(actualC.validTo));
                        caCertificateRepository.save(inBaseC);
                        break;
                    }
                }

                if (!found) {
                    removeList.add(inBaseC);
                }
            }
            em.flush();

            caCertificateRepository.delete((CertificateAuthority) removeList);
            em.flush();
        });
    }

    @SuppressWarnings("unchecked")
    private void updateCRLMeta(AccreditedCertificationCenters accreditedCertificationCenters) {


        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(2000)
                .setConnectionRequestTimeout(2000)
                .setSocketTimeout(2000)
                .build();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        CertificateFactory certFactory;
        try {
            certFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            log.error("Error while getting certificate factory. ", e);
            return;
        }

        TransactionUtil.doInTransaction(platformTransactionManager, () -> {
            Iterable<CertificateRevocationListMeta> inBaseList = crlMetaRepository.findAll();
            X509CRL crl = null;
            for (com.snm.security.job.document.CertifyingCenter center : accreditedCertificationCenters.centers) {
                for (AddressesListsRevocation listsRevocation : center.getAddressListRevocation()) {
                    Set<BigInteger> revocationCertifications = new HashSet<>();
                    for (String revocation : listsRevocation.revocationLists) {
                        crl = fetchCrlFile(client, certFactory, revocation, crl);
                        if (crl != null) {
                            if (!findCrlMeta(inBaseList, revocation, crl)) {
                                addNewRows(center, crl, revocation, revocationCertifications);
                            }
                        }
                    }
                }
            }
        });
    }

    private void addNewRows(com.snm.security.job.document.CertifyingCenter center, X509CRL crl, String revocation, Set<BigInteger> revocationCertifications) {
        List<CertifyingCenter> certifyingCenters = certifyingCenterRepository.findByName(center.name);

        if (certifyingCenters.isEmpty()) {
            log.error("Create process was interrupted. CertifyingCenterEntity with name: " + center.name + "not found in base.");
            return;
        }
        if (crl.getSigAlgName() == null || crl.getThisUpdate() == null) {
            log.error("Create process was interrupted. Invalid data in: " + crl);
            return;
        }

        CertificateRevocationListMeta newMeta = new CertificateRevocationListMeta();
        newMeta.setCertifyingCenter(certifyingCenters.get(0));
        newMeta.setUrl(revocation);
        newMeta.setAlgorithm(crl.getSigAlgName());
        newMeta.setUpdateDate(new Timestamp(crl.getThisUpdate().getTime()));

        crlMetaRepository.save(newMeta);

        if (crl.getRevokedCertificates() != null) {
            int count = 0;
            for (X509CRLEntry revokedC : crl.getRevokedCertificates()) {

                count++;

                if (count % 1000 == 0) {
                    em.flush();
                    em.clear();
                }

                if (revokedC.getSerialNumber() == null || revokedC.getRevocationDate() == null) {
                    log.error("Create process was interrupted. Invalid data in: " + revokedC);
                    continue;
                }

                if (revocationCertifications.contains(revokedC.getSerialNumber())) {
                    continue;
                }

                CertificateRevocationList existingCrl = crlRepository.findBySerialNumber(revokedC.getSerialNumber().toString());

                if (existingCrl != null) {
                    continue;
                }

                CertificateRevocationList crlEntity = new CertificateRevocationList();
                crlEntity.setRevocationListMeta(newMeta);
                crlEntity.setSerialNumber(revokedC.getSerialNumber().toString());
                crlEntity.setRevocationDate(new Timestamp(revokedC.getRevocationDate().getTime()));
                em.persist(crlEntity);

                revocationCertifications.add(revokedC.getSerialNumber());
            }
        }
        em.flush();
    }

    private boolean findCrlMeta(Iterable<CertificateRevocationListMeta> inBaseList, String revocation, X509CRL crl) {
        boolean found[] = new boolean[]{false};
        for (CertificateRevocationListMeta crlMetaEntity : inBaseList) {
            if (crlMetaEntity.getUrl() != null && crlMetaEntity.getUrl().equals(revocation)) {
                found[0] = true;

                if (crl.getSigAlgName() == null || crl.getThisUpdate() == null) {
                    log.error("Update process was interrupted. Invalid data in: " + crl);
                    break;
                }

                crlMetaEntity.setAlgorithm(crl.getSigAlgName());
                crlMetaEntity.setUpdateDate(new Timestamp(crl.getThisUpdate().getTime()));

                crlMetaRepository.save(crlMetaEntity);
                em.flush();
                break;
            }
        }

        return found[0];
    }

    private X509CRL fetchCrlFile(HttpClient client, CertificateFactory certFactory, String revocation, X509CRL crl) {

        HttpResponse response = null;
        try {
            HttpGet get = new HttpGet(revocation);
            response = client.execute(get);
            InputStream crlStream = response.getEntity().getContent();
            crl = (X509CRL) certFactory.generateCRL(crlStream);
        } catch (IOException | CRLException e) {
            log.error(revocation + " | " + e.toString());
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        return crl;
    }

    private Timestamp parseDate(String stringDate) {

        DateFormat format = new SimpleDateFormat(datePattern);
        try {
            Date date = format.parse(stringDate);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            log.error("Error parsing date: " + stringDate, e);
            return null;
        }
    }

    @NotNull
    private File fetchCaListFile() throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(urlToGetFile);

        HttpResponse response = client.execute(get);
        InputStream data = response.getEntity().getContent();

        File caListFile = File.createTempFile("motp-ca-list", ".xml");
        try (OutputStream output = new FileOutputStream(caListFile)) {
            ByteStreams.copy(data, output);
        } catch (Exception e) {
            log.error("Error during saving temp file " + caListFile.getPath(), e);
        }
        return caListFile;
    }

    private AccreditedCertificationCenters parseCaListFile(@NotNull File file) {
        try {
            String ss = removeBOMSymbol(new String(Files.readAllBytes(file.toPath()), encoding));

            JAXBContext jaxbContext = JAXBContext.newInstance(AccreditedCertificationCenters.class);
            StringReader reader = new StringReader(ss);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return (AccreditedCertificationCenters) unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            log.error("Error while parsing document.", e);
            return null;
        }
    }

    private String removeBOMSymbol(String s) {
        if (s.charAt(0) != QUOTATION) {
            return s.substring(1, s.length());
        } else {
            return s;
        }
    }
}