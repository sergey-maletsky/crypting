
;(function (angular) {
    'use strict';

    angular.module('core')
      .run(function ($rootScope, $state, $window, AuthService, DialogService) {
          'ngInject';

          const DEFAULT_PAGE = 'app.auth.home';
          const LOGIN_PAGE = 'app.non_auth.loginKep';

          $rootScope.go = $state.go;

          DialogService.init();

          $rootScope.$on('$stateChangeStart', function () {
              let stateAuth = arguments[1].name.split('.')[1];
              $rootScope.loadingProgress = true;
              DialogService.init();
              AuthService.checkUser().then(function () {
                  if (AuthService.isAuthentication()) {
                      if (stateAuth === 'non_auth') {
                          $state.go(DEFAULT_PAGE);
                      }
                  } else {
                      if (stateAuth === 'auth') {
                          $state.go(LOGIN_PAGE);
                      }
                  }
              })
          });

          $rootScope.$on('$stateNotFound', function () {
          });

          $rootScope.$on('unauthorized', function () {
              AuthService.logout();
          });

          $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, from, fromParams) {
              $rootScope.startBreadcrumbsState = toState;
              $rootScope.startBreadcrumbsParams = toParams;
              $rootScope.loadingProgress = false;
              if (from) $rootScope.previousState = from.name;
          });

          $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
          });

          $rootScope.state = $state;
          $rootScope.startBreadcrumbs = {};


      });
})(angular);