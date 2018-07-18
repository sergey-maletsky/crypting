;
(function (angular) {
  "use strict";

  angular.module("core.auth")
    .component("snmLoginKep", {
      templateUrl: function (ResourcesService) {
        return ResourcesService.getTemplate('auth/login-kep');
      },
      controller(AuthService, $state){
        let $ctrl = this;

        Object.assign($ctrl, {
          model: {
            popup: false,
            signature: null,
            signaturesList: [],
              isReg: false
          },
          $onInit(){
            AuthService.createCert($ctrl.model);
          },
          checkSignature() {
            return $ctrl.model.signature ? false : true;
          },
          togglePopup() {
            $ctrl.model.popup = !$ctrl.model.popup;
          },
          login(){
            AuthService.loginOfSertificate($ctrl.model, function () {
                $state.go('app.auth.home');
                $state.reload();
            });
          }
        })
      }
    })
})(angular);