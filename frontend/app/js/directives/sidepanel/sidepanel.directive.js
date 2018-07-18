angular.module('core').directive('snmSidePanel', function($compile) {
  return {
    restrict: 'AE',
    scope: {
      show: '=',
      hidePanel: '@',
      component: '@',
      componentData: '='
    },
    multiElement: true,
    transclude: true,
    templateUrl: 'js/directives/sidepanel/sidepanel.html',
    link($scope, $elem) {
      $scope.$watch('show', function(value) {
        if (value) {
          const element = document.createElement($scope.component);
          element.setAttribute('show-panel', 'show');
          element.setAttribute('panel-data', 'componentData');
          const component = $compile(element)($scope);
          const wrap = $elem.find('[ng-transclude]');
          wrap.append(component);
        }
      });
      $scope.hidePanel = () => {
        $scope.show = false;
      }
    }
  };
});