;(function (angular) {
    "use strict";

    angular.module("core.ui")
        .component("snmButton", {
            templateUrl: function (ResourcesService) {
                return ResourcesService.getTemplate('ui/button');
            },
            bindings: {
                label: '@',
                disabled: '@',
                cancel: '@'
            },
            controller() {
                let $ctrl = this;
                Object.assign($ctrl, {
                    $onInit() {

                    }
                })
            }
        })
})(angular);