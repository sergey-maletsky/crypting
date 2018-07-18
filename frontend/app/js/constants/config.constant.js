;(function (angular) {
    'use strict';
    angular
        .module('core')
        .constant('CONFIG', {
            HOME: {
                products: {
                    header: true
                }
            },

            NOTIFICATIONS: {
                item: {
                    captionTemplate: "notifications"
                }
            }
        });
})(angular);