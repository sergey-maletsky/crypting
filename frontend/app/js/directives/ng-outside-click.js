angular.module('core').directive('ngOutsideClick', function($document) {
  return {
    link: function ($scope, $element, $attrs) {
      $document.click(function (e) {
        const target = angular.element(e.target).closest($element);
        if (target.length === 0) {
          const Func = $scope.$eval($attrs.ngOutsideClick);
          Func();
        }
      });
    }
  };
});