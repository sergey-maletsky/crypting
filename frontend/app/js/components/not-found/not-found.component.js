;(function (angular) {
    "use strict";

    angular.module("core.notFound")
        .component("notFound", {
            templateUrl: function (ResourcesService) {
                return ResourcesService.getTemplate('not-found');
            },
            controller() {
                let $ctrl = this;

                Object.assign($ctrl, {
                    model: {
                        search: ''
                    },
                    $onInit() {
                    }
                })
            }
        })
})(angular);