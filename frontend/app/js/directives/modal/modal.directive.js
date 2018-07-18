angular.module('core').directive('snmModal', function($compile) {
  return {
    restrict: 'AE',
    scope: {
      show: '=',
      closeBtn: '=',
      hideModal: '@',
      component: '@',
      componentData: '='
    },
    multiElement: true,
    transclude: true,
    templateUrl: 'js/directives/modal/modal.html',
    link($scope, $elem) {
      $scope.$watch('show', function(value) {
        if (value) {
          const element = document.createElement($scope.component);
          element.setAttribute('show-modal', 'show');
          element.setAttribute('modal-data', 'componentData');
          const component = $compile(element)($scope);
          const wrap = $elem.find('[ng-transclude]');
          wrap.append(component);
        }
      });
      $scope.hideModal = () => {
        $scope.show = false;
      }
    }
  };
});