;(function (angular) {
    'use strict';

    angular.module('core')
        .config(['$httpProvider', function($httpProvider) {
            $httpProvider.interceptors.push('LoadingListener');
        }])
        .factory('LoadingListener', [ '$q', '$rootScope', function($q, $rootScope) {
            let reqsActive = 0;

            function onResponse() {
                reqsActive--;
                if (reqsActive === 0) {
                    $rootScope.$broadcast('loading:completed');
                }
            }

            return {
                'request': function(config) {
                    if (config.method === 'POST') {
                        if (reqsActive === 0) {
                            $rootScope.$broadcast('loading:started');
                        }
                        reqsActive++;
                    }

                    return config;
                },
                'response': function(response) {
                    if (response.config.method === 'POST') {
                        if (!response || !response.config) {
                            return response;
                        }
                        onResponse();
                    }
                    return response;
                },
                'responseError': function(rejection) {
                    if (rejection.config && rejection.config.method === 'POST') {
                        if (!rejection || !rejection.config) {
                            return $q.reject(rejection);
                        }
                        onResponse();
                    }
                    return $q.reject(rejection);
                },
                isLoadingActive : function() {
                    return reqsActive === 0;
                }
            };
        }])
        .directive('loadingListener', ['$rootScope', 'LoadingListener', function ($rootScope, LoadingListener) {

            let tpl = '<div class="preloader-cl-wrap"><div class="preloader-cl _listener" ng-if="$ctrl.root.loadingProgress">\n' +
                '    <div class="sk-circle">\n' +
                '        <div class="sk-circle1 sk-child"></div>\n' +
                '        <div class="sk-circle2 sk-child"></div>\n' +
                '        <div class="sk-circle3 sk-child"></div>\n' +
                '        <div class="sk-circle4 sk-child"></div>\n' +
                '        <div class="sk-circle5 sk-child"></div>\n' +
                '        <div class="sk-circle6 sk-child"></div>\n' +
                '        <div class="sk-circle7 sk-child"></div>\n' +
                '        <div class="sk-circle8 sk-child"></div>\n' +
                '        <div class="sk-circle9 sk-child"></div>\n' +
                '        <div class="sk-circle10 sk-child"></div>\n' +
                '        <div class="sk-circle11 sk-child"></div>\n' +
                '        <div class="sk-circle12 sk-child"></div>\n' +
                '    </div>\n' +
                '</div></div>';

            return {
                restrict: 'CA',
                link: function linkFn(scope, elem) {
                    let indicator = angular.element(tpl);
                    elem.prepend(indicator);

                    elem.css('position', 'relative');
                    if (!LoadingListener.isLoadingActive()) {
                        indicator.css('display', 'none');
                    }

                    $rootScope.$on('loading:started', function () {
                        indicator.css('display', 'block');
                    });
                    $rootScope.$on('loading:completed', function () {
                        indicator.css('display', 'none');
                    });
                }
            };
        }]);
})(angular);