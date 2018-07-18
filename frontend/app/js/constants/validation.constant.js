;(function (angular) {
    'use strict';
    angular
      .module('core')
      .constant('PATTERNS', {
          email: /^[а-яА-ЯёЁa-zA-Z0-9_+&*-]+(?:\.[а-яА-ЯёЁa-zA-Z0-9_+&*-]+)*@(?:[а-яА-ЯёЁa-zA-Z0-9-]+\.)+[а-яА-ЯёЁa-zA-Z]{2,7}$/,
          phone: /^[0-9]{10,15}$/, // от 10 до 15 цифр согласно стандарту Е.164
          onlyNumbers: /^\d+$/,
          inn: /^(?:\d{10}|\d{12})$/,
          price: /^\d+[,.]\d{2}$/,

      });
})(angular);