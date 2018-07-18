;(function (angular) {
  'use strict';

  angular.module('core.auth', [])
    .config(config);

  function config($stateProvider) {
    $stateProvider
      .state({
        name: 'app.non_auth.reg',
        url:'/registration',
        views: {
          'content@app.non_auth': {
            template: `<snm-registration></snm-registration>`
          }
        },
        bodyClass: 'login'
      })

      //new login
      .state({
        name: 'app.non_auth.loginKep',
        url:'/login-kep',
        views: {
          'content@app.non_auth': {
            template: `<snm-login-kep></snm-login-kep>`
          }
        },
        bodyClass: 'login-kep'
      })
  }
})(angular);