;(function (angular) {
    'use strict';

    angular.module('core.notFound', [])
        .config(config);

    function config($stateProvider) {
        $stateProvider
            .state({
                name: 'app.auth.not-found',
                url:'/404',
                views: {
                    'content@app.auth': {
                        template: `<not-found></not-found>`
                    }
                },
                bodyClass: 'not-found'
            });
    }
})(angular);