;(function (angular) {
    'use strict';

    angular
      .module('core')
      .factory('ApiService', function ($http, $window, $state, ResourcesService, $q) {

          const ROOT_URL = window.location.origin === 'http://localhost:63342' ? 'http://localhost:3000' : window.location.origin;
          const BASE_URL = '/';
          const VERSION_URL = '';
          const PATHS = {
              SIGNIN: '/auth/login',
              HEART_BEAT: '/heartbeat'
          };

          let getApiUrl = function () {
              return ROOT_URL + BASE_URL + VERSION_URL;
          };

          let _request = function (options) {
              return $http(options)
                  .then(resp => resp)
                  .catch(err => {
                      if (err.status >= 500)
                          throw { message: 'Сервер недоступен' };
                      else
                          throw err;
              });
          };

          let _get = function (url, params) {
              let requestData = {
                  method: "GET",
                  url: url
              };

              if (params)
                  requestData = {...requestData, ...params};

              return _request(requestData);
          };

          let _post = function (url, data, params) {
              let requestData = {
                  method: "POST",
                  url: url,
                  data: data || {}
              };

              if (params)
                  requestData = {...requestData, ...params};

              return _request(requestData);
          };

          let _put = function (url, data) {
              return _request({
                  method: "PUT",
                  url: url,
                  data: data || {}
              })
          };

          let _delete = function (url) {
              return _request({
                  method: "DELETE",
                  url: url
              })
          };

          return {
              get: _get,
              post: _post,
              put: _put,
              delete: _delete,

              auth: {
                  login(data) {
                      return _post(getApiUrl(true) + PATHS.SIGNIN, data);
                  },
                  currentUser() {
                      return $q.when({}).then(function () {
                          return JSON.parse(window.localStorage.getItem('user'))
                      });
                  },
                  getUsers(data) {
                      return _post(getApiUrl(true) + PATHS.USERS, data);
                  },
                  logout() {
                      return $q.when({}).then(function () {
                          return null;
                      });
                  }
              },

              getRandomKey() {
                  return _get(getApiUrl() + "auth/key");
              },

              signInByKep(data) {
                  return _post(getApiUrl() + "auth/signInByKep", data);
              }
          }
      })
})(angular);