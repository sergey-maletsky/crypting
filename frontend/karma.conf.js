// Karma configuration
// Generated on Wed Jun 21 2017 14:50:45 GMT+0300 (Russia TZ 2 Standard Time)

module.exports = function (config) {
    config.set({

        // base path, that will be used to resolve files and exclude
        basePath: '.',


        // frameworks to use
        frameworks: [/*'jasmine-ajax',*/'jasmine'],
        //plugins: [karma-jasmine-ajax],


        // list of files / patterns to load in the browser
      files: [
        'bower_components/angular/angular.js',
        /*Modules*/
        'bower_components/angular-ui-router/release/angular-ui-router.js',
        'bower_components/angular-ui-notification/dist/angular-ui-notification.js',
        'bower_components/angular-translate/angular-translate.js',
        'bower_components/angularjs-datepicker/dist/angular-datepicker.js',
        'bower_components/angular-bootstrap/ui-bootstrap.js',
        'app/js/components/auth/auth.module.js',
        /*App*/
        'app/app.js',
        'app/js/app.directive.js',
        'app/js/app.filter.js',
        'app/js/app.route.js',
        /*Constants*/
        'tests/dummy/translates.constants.js',

        /*'app/js/app.run.js',*/

        /*Services*/
        'app/js/services/resources.service.js',
        'app/js/services/api.service.js',
        'app/js/services/filter.service.js',

        /*Components*/
        'app/js/components/auth/login-new/*.js',

        /*Tests modules*/
        'bower_components/angular-mocks/angular-mocks.js'
      ],

        // list of files to exclude
        exclude: [],

        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
          'app/**/*.html': ['ng-html2js']
        },


        // test results reporter to use
        // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
        reporters: ['progress'],


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera (has to be installed with `npm install karma-opera-launcher`)
        // - Safari (only Mac; has to be installed with `npm install karma-safari-launcher`)
        // - PhantomJS
        // - IE (only Windows; has to be installed with `npm install karma-ie-launcher`)
        browsers: ['Chrome'],

        browserConsoleLogOptions: {
            terminal: true,
            level: ""
        },


        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,


        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: true
    });
};
