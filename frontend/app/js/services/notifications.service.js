;(function (angular) {
    'use strict';

    angular
        .module('core')
        .factory('NotificationsService', function (SIDEBAR_NOTIFICATION) {
            return {
                get(code, data) {
                    switch (code) {
                        case 'code':
                            return `${SIDEBAR_NOTIFICATION[code]} <strong>${data[0]}</strong>`;
                        case 'certificate':
                            return `Через ${data[0]} ${SIDEBAR_NOTIFICATION[code]} <strong>${data[1]}</strong>`;
                        case 'registration':
                            return `<strong>${data[0]}</strong> ${SIDEBAR_NOTIFICATION[code]}`;
                    }
                }

            }
        })
})(angular);