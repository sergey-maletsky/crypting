;(function (angular) {
    'use strict';

    angular
        .module('core')
        .factory('AuthByKepService', function (NotificationService) {

            let CADESCOM_CADES_BES = 1;
            let CAPICOM_CERTIFICATE_FIND_SUBJECT_NAME = 1;

            let isActiveX  = function(){
                try {
                    let store = new ActiveXObject('CAdESCOM.store');
                    return true;
                } catch (e) {
                    return false;
                }
            };

            let Verify = function(signedMessage, dataToVerify) {
                let cadesSignedData = null;
                let res = true;
                if (isActiveX()) {
                    cadesSignedData = new ActiveXObject('CAdESCOM.CadesSignedData');
                } else {
                    cadesSignedData = cadesplugin.CreateObject('CAdESCOM.CadesSignedData');
                }

                try {
                    cadesSignedData.Content = dataToVerify;
                    cadesSignedData.VerifyCades(signedMessage, CADESCOM_CADES_BES, true);
                } catch (err) {
                    NotificationService.show({
                        type: 'error',
                        message: "Failed to verify signature. Error: " + cadesplugin.getLastError(err)
                    });
                    res = false;
                    return res;
                }

                return res;
            };

            let SignCreate = function(certSubjectName, dataToSign) {
                let cadesStore = cadesplugin.CreateObject("CAdESCOM.Store");
                let cadesSigner = null;
                let signedMessage = "";
                let cadesSignedData = null;
                let certificates = cadesStore.Certificates.Find(
                    CAPICOM_CERTIFICATE_FIND_SUBJECT_NAME, certSubjectName);
                if (certificates.Count === 0) {
                    NotificationService.show({
                        type: 'error',
                        message: "Certificate not found: " + certSubjectName
                    });
                    return;
                }
                let certificate = certificates.Item(1);

                cadesStore.Open();

                if (isActiveX()) {
                    cadesSigner = new ActiveXObject('CAdESCOM.CPSigner');
                } else {
                    cadesSigner = cadesplugin.CreateObject('CAdESCOM.CPSigner');
                }

                cadesSigner.Certificate = certificate;

                if (isActiveX()) {
                    cadesSignedData = new ActiveXObject('CAdESCOM.CadesSignedData');
                } else {
                    cadesSignedData = cadesplugin.CreateObject('CAdESCOM.CadesSignedData');
                }

                cadesSignedData.Content = dataToSign;

                try {
                    signedMessage = cadesSignedData.SignCades(cadesSigner, CADESCOM_CADES_BES, true);
                } catch (err) {
                    NotificationService.show({
                        type: 'error',
                        message: "Failed to create signature. Error: " + cadesplugin.getLastError(err)
                    });
                    return;
                }

                cadesStore.Close();

                return signedMessage;
            };

            let run = function(data) {
                let certNameFromElement = document.getElementById("certName");
                let certName = certNameFromElement.value;
                if ("" == sCertName) {
                    NotificationService.show({
                        type: 'error',
                        message: 'Введите имя сертификата (CN).'
                    });
                    return;
                }

                let signedMessage = SignCreate(certName, data);

                let verifyResult = Verify(signedMessage, data);
                if (verifyResult) {

                    NotificationService.show({
                        type: 'success',
                        message: 'Signature verified'
                    });
                }
            };

            return {
                run
            }
        });
})(angular);