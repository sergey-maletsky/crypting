;(function (angular) {
    'use strict';

    angular.module('core')
        .provider('ResourcesService', function ResourcesServiceProvider(CONFIG) {
            let siteId = 'SNM';
            this.getSiteId = function () {
                return siteId;
            };

            this.$get = function ResourcesServiceFactory() {
                let current_tmpl = 'home';
                let current_module = {};
                /**
                 * @param template - имя html шаблона
                 * @param uniqueTemplates - массив с идентификаторами сайта для которых необходим уникальный template
                 * @returns {string}
                 */
                let getTemplate = function (template) {
                    let uniqueTemplates = [];
                    let pathArray = template.split('/');
                    let file = pathArray.slice(-1)[0];
                    let path = "js/components/" + pathArray.join('/') + '/';

                    if (uniqueTemplates.indexOf(siteId) !== -1) {
                        file = file + '-' + siteId;
                    }

                    return path + file + '.html';
                };

                let setModule = function(name, own){
                    let templateUrl = name;
                    if(own){
                      templateUrl = 'page'
                    }
                    current_module = {
                        list: templateUrl + '/list',
                        item: templateUrl + '/item',
                        type: name,
                        groups:CONFIG[name.toUpperCase()],
                        config:CONFIG[name.toUpperCase()]
                    };

                    return current_module;
                };

                let getModule = function(){
                    return current_module;
                };

                let getModuleTemplate = function(){
                    return current_tmpl;
                };

                let setModuleTemplate = function(template){
                    current_tmpl = template;
                };

                let getSiteId = function () {
                    return siteId;
                };

                let setSiteId = function (id) {
                    siteId = id;
                };

                let getConstants = function(type){
                  let constant_type = type.toUpperCase();
                  return {
                    grid:CONFIG[constant_type],
                    col_types:CONFIG[constant_type],
                    config:CONFIG[constant_type],
                    groups:CONFIG[constant_type],
                    tabs:CONFIG[constant_type]
                  }
                };

                return {
                    getTemplate,
                    getModuleTemplate,
                    setModuleTemplate,
                    getConstants,
                    setModule,
                    getModule,
                    getSiteId,
                    setSiteId
                };
            };
        });
})(angular);