;(function (angular) {
    'use strict';

    angular
      .module('core')
      .factory('DialogService', function ($rootScope, $window) {

          let defaultDialog = {
              show: false,
              template: null,
              header: null
          };
          let defaultNotification = {
              type: 'default',
              message: null,
              template: null,
              data: null
          };
          let countEnabled = false;

          let _openDialog = function (options, callbacks) {
              $rootScope.dialogSettings = Object.assign(defaultDialog, {
                  show: true,
                  template: options.template,
                  header: options.header,
                  data: options.data,
                  callbacks: callbacks
              })
          };

          function _removeNotification() {
              countEnabled = true;
              $window.setTimeout(function () {
                  $rootScope.notificationSettings.shift();
                  if ($rootScope.notificationSettings.length) {
                      _removeNotification();
                  } else {
                      countEnabled = false;
                  }
              }, 1000)
          }

          let _addNotification = function (options, callbacks) {
              $rootScope.notificationSettings.push(
                Object.assign(defaultNotification, {
                    type: options.type,
                    message: options.message,
                    template: options.template,
                    data: options.data,
                    callbacks: callbacks
                })
              );

              if (!countEnabled) {
                  _removeNotification()
              }
          };

          let switchType = function (type, dialog, NotificationService, ...args) {
              switch (type) {
                  case "dialog":
                      (function () {
                          return dialog(...args);
                      })();
                      break;
                  case "notification":
                      (function () {
                          //return NotificationService.show({message: args});
                      })();
                      break;
              }
          };

          return {
              init() {
                  $rootScope.notificationSettings = [];
                  $rootScope.dialogSettings = defaultDialog;
              },
              open(tmpl, header, data, callback) {
                  let template = tmpl ? tmpl : 'default';
                  $rootScope.dialogSettings.show = true;
                  $rootScope.dialogSettings.template = template;
                  $rootScope.dialogSettings.header = header || "Default dialog header";
                  $rootScope.dialogSettings.data = data || {};
                  $rootScope.dialogSettings.callback = callback || function () {
                  };

              },

              open1(type, options, callbacks) {
                  switch (type) {
                      case "dialog":
                          (function () {
                              _openDialog(options, callbacks)
                          })();
                          break;
                      case "notification":
                          (function () {
                              _addNotification(options, callbacks);
                          })();
                          break;
                  }
              },
              close() {
                  $rootScope.dialogSettings.show = false;
              },
              get(type, prop) {

                  let getValue = null;

                  switch (type) {
                      case "dialog":
                          (function () {
                              getValue = $rootScope.dialogSettings[prop];
                          })();
                          break;
                      case "notification":
                          (function () {
                              getValue = $rootScope.notificationSettings;
                          })();
                          break;
                  }

                  return getValue;
              },
              set(type, prop, value) {
                  $rootScope.dialogSettings[prop] = value;
              }
          }
      })
})(angular);