;(function (angular) {
    'use strict';

    angular
      .module('core')
      .factory('AuthService', function (ApiService, NotificationService, bowser, UtilsService, FilterService, $window, $http, $q, $state) {
          const authSchemePrefix = 'Bearer ';
          let CurrentUser = null;
          let authentication = false;
          let signaturesObjList = [];
          let certificatesList = null;

          const setHeaderToken = function (token) {
              if (token) {
                  $http.defaults.headers.common['Authorization'] = authSchemePrefix + token;
              }
          };

          const saveToken = function (token) {
              $window.localStorage['token'] = token;
          };

          const getToken = function () {
              return $window.localStorage['token'];
          };

          //auth by kep
          const showError = err => {
              let message = err.message || (err.data ? err.data.message : false) || undefined;
              if (message) {
                  if (message.search('0x000004C7') > 0 || message.search('0x8010006E') > 0) {
                      message = 'Действие отменено пользователем';
                  } else if (message.search('0x8010006B') > 0) {
                      message = 'Неверно указан пароль сертификата';
                  } else if (message.search('0x800B010A') > 0) {
                      message = 'Не удается построить цепочку сертификатов до доверенного корневого центра';
                  } else if (message.search('0x8009200B') > 0) {
                      message = 'Не удается найти сертификат и закрытый ключ для дешифрования';
                  } else if (message.search('0x8007065B') > 0) {
                      message = 'Истекла лицензия на КриптоПро CSP. Проверить валидность лицензии можно утилитой cpconfig, например: /opt/cprocsp/sbin/amd64/cpconfig -license -view';
                  } else if (message.search('0x8009000D') > 0) {
                      message = 'Не удалось загрузить приватный ключ. Возможно действие было отменено пользователем';
                  }
                  NotificationService.show({
                      type: 'error',
                      message
                  });
              } else {
                  NotificationService.showList('error', err.data.errors);
              }
          };

          const createObject = function (name, async) {
              if (async) return cadesplugin.CreateObjectAsync(name);
              return cadesplugin.CreateObject(name);
          };

          const openAndGetCertificates = function (name) {
              return cadesplugin
                .then(() => {
                    return createObject(name, true);
                }).then(methods => {
                    methods.Open();
                    return methods;
                }).then(methods => {
                    return methods.Certificates;
                }).catch(err => {
                    showError(err);
                })
          };

          const checkCount = function (count) {
              if (count === 0) {
                  showError({ message: 'Электронная подпись не обнаружена' });
                  return false;
              }
              return true;
          };

          const parseCert = (string, param) => {
              let value = '';
              string = string.split(param + '=')[1];
              if (string) value = string.split(', ')[0];
              value = value.replace(/""/gi, '"');
              return value
          };

          const getNameFromCertificate = (source) => {
                const name = parseCert(source, 'G');
                const sirname = parseCert(source, 'SN');

                return (sirname && name) ? `${sirname} ${name}` : parseCert(source, 'CN');
          }

          const getCertificatesList = function (count, model, async) {
              if (checkCount(count)) {
                  for (let i = 1; i <= count; i++) {
                      if (async) {
                          certificatesList.Item(i).then(cert => {
                              signaturesObjList.push(cert);
                          });

                          certificatesList.Item(i).then((cert => {
                              Promise.all([
                                  cert.SubjectName,
                                  cert.ValidFromDate,
                                  cert.ValidToDate
                              ]).then(result => {
                                  model.signaturesList.push({
                                      company: parseCert(result[0], 'O'),
                                      email: parseCert(result[0], 'E'),
                                      fromDate: new Date(result[1]),
                                      toDate: new Date(result[2]),
                                      name: getNameFromCertificate(result[0])
                                  });
                              });

                          }))
                      } else {
                          const cert = certificatesList.Item(i);
                          signaturesObjList.push(cert);

                        model.signaturesList.push({
                              company: parseCert(cert.SubjectName, 'O'),
                              email: parseCert(cert.SubjectName, 'E'),
                              fromDate: new Date(cert.ValidFromDate),
                              toDate: new Date(cert.ValidToDate),
                              name: getNameFromCertificate(cert.SubjectName)
                          });
                      }
                  }
              }
          };

          const createCert = function (model) {
              if (bowser.msie) {
                  try {
                      const oStore = createObject("CAdESCOM.Store");
                      oStore.Open();
                      const count = oStore.Certificates.Count;
                      certificatesList = oStore.Certificates;
                      getCertificatesList(count, model);
                  }
                  catch (err) {
                      showError(err);
                  }
              } else {
                  openAndGetCertificates("CAdESCOM.Store").then(function (certificates) {
                      if (certificates) {
                          certificatesList = certificates;
                          return certificates.Count;
                      }
                  }).then(function (count) {
                      getCertificatesList(count, model, true);
                  }).catch(err => {
                      showError(err);
                  })
              }
          };

          const signInByKep = (signature, uuid, isReg, cb) => {
              ApiService.signInByKep({
                  uuid: uuid,
                  data: signature,
                  isReg: isReg
              }).then(resp => {
                  if (resp.status < 300) {
                      authentication = true;
                      if (resp.data && resp.data.token) {
                          saveToken(resp.data.token);
                          setHeaderToken(resp.data.token);
                      } else {
                          NotificationService.show({
                              type: 'success',
                              message: resp.data.message
                          });
                      }
                  } else {
                      NotificationService.show({
                          type: 'error',
                          message: resp.data.message
                      });
                  }
                  cb(resp);
              }, err => {
                  showError(err)
              });
          };

          const checkKey = (resp) => {
              if (!resp.data && !resp.uuid) console.log('key is empty');
          };

          const loginOfSertificate = function (data, cb) {
              cadesplugin.CADESCOM_ENCODE_BASE64 = 1;
              const CADESCOM_CADES_BES = 1;
              const CAPICOM_CERTIFICATE_INCLUDE_WHOLE_CHAIN = 1;
              const INDEX_ITEM = data.signaturesList.indexOf(data.signature);

              if (bowser.msie) {
                  const oSigner = createObject("CAdESCOM.CPSigner");
                  oSigner.Certificate = signaturesObjList[INDEX_ITEM];
                  const oSignedData = cadesplugin.CreateObject("CAdESCOM.CadesSignedData");
                  ApiService.getRandomKey().then(resp => {
                      checkKey(resp.data);
                      oSignedData.Content = resp.data.data;
                      return resp.data.uuid;
                  }).then(uuid => {
                      signInByKep(oSignedData.SignCades(oSigner, CADESCOM_CADES_BES), uuid, data.isReg, cb)
                  }).catch(err => {
                      showError(err);
                  })
              } else {
                  createObject("CAdESCOM.CPSigner", true).then(CPSigner => {
                      CPSigner.propset_Certificate(signaturesObjList[INDEX_ITEM]);
                      CPSigner.propset_Options(CAPICOM_CERTIFICATE_INCLUDE_WHOLE_CHAIN);
                      createObject("CAdESCOM.CadesSignedData", true).then(function (CadesSignedData) {
                          ApiService.getRandomKey().then(resp => {
                              checkKey(resp.data);
                              CadesSignedData.propset_Content(String(resp.data.data));
                              return resp.data.uuid;
                          }).then(uuid => {
                              CadesSignedData.SignCades(CPSigner, CADESCOM_CADES_BES)
                                .then(signature => {
                                  signInByKep(signature, uuid, data.isReg, cb);
                              }).catch(err => {
                                  showError(err);
                              })
                          })
                      })

                  })
              }
          };
          //end auth by kep

          const isAuthentication = function () {
              return authentication;
          };

          const removeToken = function () {
              $window.localStorage.removeItem('token');
          };

          const clearAllFilters = function () {
              FilterService.clearAllFilters();
          };

          const clearSession = function () {
              authentication = false;
              CurrentUser = null;
              removeToken();
              clearAllFilters();
              setHeaderToken(null);
              window.localStorage.clear();
              $state.go('app.non_auth.loginKep');
          };

          const login = function (form, errorCallback) {
              return ApiService.auth.login({
                  login: form.login,
                  pass: form.password
              }).then(function (resp) {
                  authentication = true;
                  if (resp.data && resp.data.token) {
                      saveToken(resp.data.token);
                      setHeaderToken(resp.data.token);
                  }
                  $state.go('app.auth.home');
                  return resp;
              })
                  .catch(err => {
                  if (errorCallback)
                  errorCallback();
          else {
                  throw err;
              }
          });
          };

          const logout = function () {
              return ApiService.auth.logout().then(function () {
                  clearSession();
              }, function (err) {
                  console.error('error logout', err);
                  return err;
              })

          };

          const checkUser = function () {
              let token = getToken();
              let promise = $q.when({});
              authentication = token ? true : false;
              if (CurrentUser) {
                  promise = $q.when(CurrentUser);
              } else {
                  if (token) {
                      setHeaderToken(token);
                      promise = ApiService.auth.currentUser().then(function (data) {
                          CurrentUser = data;
                          return data;
                      }, function () {
                          $state.go('app.non_auth.loginKep');
                      })
                  }
              }

              return promise;
          };

          const getUser = function () {
              return ApiService.auth.currentUser();
          };

          return {
              createCert,
              loginOfSertificate,
              isAuthentication,
              clearSession,
              setHeaderToken,
              login,
              logout,
              getUser,
              setAvatar(avatar) {
                  CurrentUser.avatar = avatar;
              },
              checkUser
          }
      });
})(angular);