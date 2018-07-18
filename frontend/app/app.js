let app = angular
  .module('core', [
      'ui.router',
      'ui.select',
      'ngAnimate',
      'ngMessages',
      'ngSanitize',
      'ngDialog',
      'ngScrollbars',
      'ng-ip-address',
      'jlareau.bowser',
      'ui-notification',
      'pascalprecht.translate',
      'daterangepicker',
      'ui-rangeSlider',
      'td.barcode',
      'templates',
      'ui.bootstrap',
      'core.ui',
      'core.auth',
      'core.notFound',
      'core.common',
      'react'
  ]);

app.config(function ($translateProvider, $compileProvider, TRANSLATES, $windowProvider, ResourcesServiceProvider, $httpProvider, ngDialogProvider) {
    $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|javascript|mailto|coui):/);
    let siteId = ResourcesServiceProvider.getSiteId();
    $httpProvider.defaults.useXDomain = true;
    //disable IE ajax request caching
    if (!$httpProvider.defaults.headers.get) {
        $httpProvider.defaults.headers.get = {};
    }
    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
    // extra
    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
    ngDialogProvider.setForceHtmlReload(true);
    ngDialogProvider.setForceBodyReload(true);
    if (siteId) {
        siteId = siteId.toUpperCase();
        $translateProvider.translations('en', TRANSLATES[siteId].EN);
        $translateProvider.translations('ru', TRANSLATES[siteId].RU);
        if (!$windowProvider.$get().localStorage.lang) {
            $windowProvider.$get().localStorage.lang = 'ru';
            $translateProvider.preferredLanguage('ru');
        } else {
            $translateProvider.preferredLanguage($windowProvider.$get().localStorage.lang);
        }
    }
    $httpProvider.interceptors.push('authInterceptor');
});

app.service('authInterceptor', function ($q, $rootScope) {
    this.responseError = (response) => {
        if (response.status === 401) {
            $rootScope.$emit('unauthorized');
        }
        return $q.reject(response);
    };
});