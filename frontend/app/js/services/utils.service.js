;(function (angular) {
  'use strict';

  angular
    .module('core')
    .factory('UtilsService', function () {
      return {
        Base64: {
          _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

          encode: function (input) {
            let output = "";
            let chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            let i = 0;

            input = Base64._utf8_encode(input);

            while (i < input.length) {

              chr1 = input.charCodeAt(i++);
              chr2 = input.charCodeAt(i++);
              chr3 = input.charCodeAt(i++);

              enc1 = chr1 >> 2;
              enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
              enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
              enc4 = chr3 & 63;

              if (isNaN(chr2)) {
                enc3 = enc4 = 64;
              } else if (isNaN(chr3)) {
                enc4 = 64;
              }

              output = output + this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

            }

            return output;
          },
          decode: function (input) {
            let output = "";
            let chr1, chr2, chr3;
            let enc1, enc2, enc3, enc4;
            let i = 0;

            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            while (i < input.length) {

              enc1 = this._keyStr.indexOf(input.charAt(i++));
              enc2 = this._keyStr.indexOf(input.charAt(i++));
              enc3 = this._keyStr.indexOf(input.charAt(i++));
              enc4 = this._keyStr.indexOf(input.charAt(i++));

              chr1 = (enc1 << 2) | (enc2 >> 4);
              chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
              chr3 = ((enc3 & 3) << 6) | enc4;

              output = output + String.fromCharCode(chr1);

              if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
              }
              if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
              }

            }
            output = Base64._utf8_decode(output);
            return output;
          }
        },
        b64DecodeUnicode(str) {
          const base64 = str.replace(/-/g, '+').replace(/_/g, '/');
          return decodeURIComponent(Array.prototype.map.call(atob(base64), function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
          }).join(''))
        },

        isArray: Array.isArray,

        /**
         * @param arr object to check
         * @return {boolean} true if arr is array object and is not empty, false otherwise
         */
        isNotEmptyArray(arr) {
          return Array.isArray(arr) && arr.length > 0;
        },

        /**
         * Converts hexadecimal string mask to array of items picked from source array.
         * @param hex hexadecimal string that represents bitmask. Each hex digit stores 4 bits of mask.
         * @param arr source array of items to search in
         * @return {Array} array of picked items
         */
        hexMaskToArrayItems(hex, arr){
          let pickedItems = [];
          if (this.isNotEmptyArray(arr) && typeof hex === 'string') {
            for (let i = 0; i < hex.length; i++) {
              const mask = parseInt(hex.charAt(i), 16);
              if (!isNaN(mask)) {
                for (let j = 0; j < 4; j++) {
                  if ((mask >>> j) % 2 === 1) {
                    let pickedItem = arr[i * 4 + j];
                    if (pickedItem) {
                      pickedItems.push(pickedItem)
                    }
                  }
                }
              }
            }
          }
          return pickedItems
        },

        getValueEnding(value, endings) {
            const number = value % 100;

            if (number >= 11 && number <= 19)
                return endings[2];

            switch (number % 10) {
                case 1:
                    return endings[0];
                case 2:
                case 3:
                case 4:
                    return endings[1];
                default:
                    return endings[2];
            }
        }
      }
    })
})(angular);