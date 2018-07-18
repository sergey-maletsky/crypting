;(function (angular) {
    "use strict";

    angular.module("core")
        .component("snmLayout", {
            templateUrl: function(ResourcesService) {
                return ResourcesService.getTemplate('layout');
            },
            controller(AuthService, $window, $http){
                let $ctrl = this;

                Object.assign($ctrl, {
                    $onInit(){
                        $ctrl.currentLang = $window.localStorage.lang ? $window.localStorage.lang : null;
                        $http.defaults.headers.common['Accept-Language'] = $ctrl.currentLang;
                    },
                    auth: AuthService
                })
            }
        })
})(angular);