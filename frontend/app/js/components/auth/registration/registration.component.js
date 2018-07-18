;
(function (angular) {
    "use strict";

    angular.module("core.auth")
      .component("snmRegistration", {
          templateUrl: function (ResourcesService) {
              return ResourcesService.getTemplate('auth/registration');
          },
          controller(AuthService, NotificationService, PATTERNS, REGISTRATION, $state) {
              let $ctrl = this;
              Object.assign($ctrl, {
                  PATTERNS,
                  model: {
                      currentStep: 0,
                      signaturesList: [],
                      signature: null,
                      isReg: true,
                      done: false
                  },
                  submitForm() {
                      AuthService.loginOfSertificate({
                          signature: $ctrl.model.signature,
                          signaturesList: $ctrl.model.signaturesList,
                          isReg: $ctrl.model.isReg
                      }, resp => {
                          $ctrl.model.currentStep = 0;
                          $ctrl.model.done = true;
                          if (resp.data.userAccepted) $state.go('app.auth.home');
                      });
                  },
                  $onInit() {
                      AuthService.createCert($ctrl.model);
                  },
                  setStepDoneClass() {
                      return ($ctrl.model.requisitesForm) ? ' _done' : '';
                  }
              })
          }
      })
})(angular);