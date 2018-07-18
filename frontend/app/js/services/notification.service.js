;(function (angular) {
    'use strict';

    angular
        .module('core')
        .factory('NotificationService', function (Notification) {

            const show = function(params) {
                const message = params.message ? params.message : (params.type === 'error' ? 'Произошла ошибка' : '');

                Notification[params.type](
                    (params.type === 'success' ? 'Успешно: ' : '') +
                    (params.type === 'error' ? 'Ошибка: ' : '') +
                    message
                );
            };

            const showList = function(type, messages) {
                messages.forEach(message =>
                    show({ type, message })
                );
            };


            return {
                show,
                showList
            }
        });
})(angular);