angular.module('core').directive('drop', function() {
  return {
    link: function (scope, element, attrs) {
      const dropFunc = scope.$eval(attrs.drop);
      element.bind('drop', dropFunc, false);
    }
  };
});