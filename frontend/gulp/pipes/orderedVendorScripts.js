import { pipes, plugins } from '../config'

pipes.orderedVendorScripts = () => {
  return plugins.order([
    'jquery.js',
    'angular.js',
    'moment.js',
    'handlebars.js',
    'bootstrap.js',
    'lodash.js',
    'jquery.mCustomScrollbar.js'
]);
};