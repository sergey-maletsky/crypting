;(function (angular) {
    'use strict';

    angular
      .module('core')
      .factory('FilterService', ['$location', function () {

          const LOCAL_STORAGE_FILTERS = 'snm-filters';
          const DEFAULT_FILTERS = function () {
              return {
                  limit: 10,
                  offset: 0,
                  needTotal: true
              }
          };

          let currentFilters = {};

          const permanentFilters = {};

          const prevFilters = {
              state: null,
              filter: null,
              total: 0
          };

          // сохранение фильтров в localStorage для конкретного state (с заменой существующих)
          function saveFilterToLS(filter, state) {
              if (window.localStorage.getItem(LOCAL_STORAGE_FILTERS) !== null) {
                  let currentLSFilters = JSON.parse(window.localStorage.getItem(LOCAL_STORAGE_FILTERS));
                  currentLSFilters[state] = filter;
                  window.localStorage.setItem(LOCAL_STORAGE_FILTERS, JSON.stringify(currentLSFilters));
              } else {
                  let LSFilter = {};
                  LSFilter[state] = filter;
                  window.localStorage.setItem(LOCAL_STORAGE_FILTERS, JSON.stringify(LSFilter));
              }
          }

          // чтение фильтров из localStorage для конкретного state
          function getLSFilter(state) {
              if (window.localStorage.getItem(LOCAL_STORAGE_FILTERS) !== null) {
                  return JSON.parse(window.localStorage.getItem(LOCAL_STORAGE_FILTERS))[state];
              } else {
                  return {};
              }
          }

          // удаление отдельного фильтра для конкретного state в localStorage
          function clearLSFilter(id, state) {
              let currentLSFilters = JSON.parse(window.localStorage.getItem(LOCAL_STORAGE_FILTERS));
              if (currentLSFilters && currentLSFilters[state]) {
                  delete currentLSFilters[state][id];
                  window.localStorage.setItem(LOCAL_STORAGE_FILTERS, JSON.stringify(currentLSFilters));
              }
          }

          function getPermanentFilters(state) {
              return permanentFilters[state] ? permanentFilters[state] : {};
          }

          function setPermanentFilters(filters, state) {
              permanentFilters[state] = filters;
          }

          function checkCurrentFilters(state) {
              if (!currentFilters[state]) {
                  currentFilters[state] = {};
                  angular.copy(DEFAULT_FILTERS(), currentFilters[state]);
              }
          }

          function compareFilters(filter1, filter2) {
              //при сравнении нужно не учитывать limit и offset, т.к. total не зависит от них

              const filter1_cleaned = {...filter1};

              delete filter1_cleaned.limit;
              delete filter1_cleaned.offset;

              if (filter1_cleaned.date && !filter1_cleaned.date.startDate && !filter1_cleaned.date.endDate)
                  delete filter1_cleaned.date;

              const filter2_cleaned = {...filter2};

              delete filter2_cleaned.limit;
              delete filter2_cleaned.offset;

              if (filter2_cleaned.date && !filter2_cleaned.date.startDate && !filter2_cleaned.date.endDate)
                  delete filter2_cleaned.date;

              return angular.equals(filter1_cleaned, filter2_cleaned);

          }

          return {
              pushFilters: function (filters, state) {
                  checkCurrentFilters(state);
                  Object.assign(currentFilters[state], filters);
                  saveFilterToLS(currentFilters[state], state);
                  return currentFilters[state];
              },
              deleteFilter: function (id, state) {
                  checkCurrentFilters(state);
                  delete currentFilters[state][id];
                  clearLSFilter(id, state);
              },
              clearFilters: function (state) {
                  checkCurrentFilters(state);
                  _.forOwn(currentFilters[state], function (value, key) {
                      delete currentFilters[state][key];
                      clearLSFilter(key, state);
                  });
                  angular.copy(DEFAULT_FILTERS(), currentFilters[state]);
              },
              clearAllFilters: function() {
                  currentFilters = {};
              },
              updateOffset(filters, offset, state) {
                  if (!currentFilters[state])
                      currentFilters[state] = filters;
                  currentFilters[state].offset = offset;
                  return currentFilters[state];
              },
              updateLimit(limit, state) {
                  checkCurrentFilters(state);
                  currentFilters[state].limit = limit;
                  return currentFilters[state];
              },
              getFilters: function (state, noAddTotal) {
                  const result = Object.assign(currentFilters[state] ? currentFilters[state] : DEFAULT_FILTERS(), getLSFilter(state), getPermanentFilters(state));

                  if (prevFilters.state === state) {
                      if (!compareFilters(result, prevFilters.filter) && !noAddTotal)
                          result.needTotal = true;
                      else
                          delete result.needTotal;
                  }
                  else
                      result.needTotal = true;

                  prevFilters.state = state;
                  prevFilters.filter = angular.copy(result);

                  return result;
              },
              getLastTotal(state) {
                  if (prevFilters.state === state)
                      return prevFilters.total;

                  return 0;
              },
              setLastTotal(state, total) {
                  if (prevFilters.state === state)
                      prevFilters.total = total;
              },
              setPermanentFilters
          };

      }])
})(angular);