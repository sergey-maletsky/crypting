;(function (angular) {
  'use strict';

  angular.module('core')
    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {
      'ngInject';
        $urlRouterProvider.when("/", "/home");
        $urlRouterProvider.otherwise(function($injector, $location){
        const state = $injector.get("$state");
        state.go("app.auth.not-found", {
            uri:$location.path()}
            );
        });
        $locationProvider.html5Mode(true);

      $stateProvider
        .state('app', {
          abstract: true,
            views: {
                '@': {
                    template: `<snm-layout user="$resolve.user"></snm-layout>`
                }
            },
            resolve:{
                user(AuthService){
                    return AuthService.getUser().then(function(user){
                        return user;
                    });
                }
            }
        })
        .state('app.auth', {
          abstract: true,
          views: {
            '@': {
              template: `<snm-layout user="$resolve.user"></snm-layout>`
            }
          }
        })
        .state('app.non_auth', {
          abstract: true,
          views: {
            '@': {
              template: `<snm-layout></snm-layout>`
            }
          }
        })
    });
})(angular);

angular.module('core').run(function ($rootScope) {
  $rootScope.$on('$locationChangeStart', function() {
    document.body.scrollTop = document.documentElement.scrollTop = 0;
  });
});
