'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.teacher',
  'myApp.login',
  'ui.bootstrap'
]).
config(['$routeProvider','$httpProvider', function($routeProvider,$httpProvider) {


  $httpProvider.interceptors.push(['$q','$rootScope', '$location',function ($q,$rootScope, $location) {
    return {
      'request': function(config) {
          if ($rootScope.authToken) {
            config.headers.authorization = $rootScope.authToken;
          } else if (config.url.indexOf("login") == -1 && config.url.indexOf("register") == -1) {
            $location.path("/login");
          }
          return config;
      },
      'responseError': function (rejection) {
        if (rejection.status === 401) {
          $location.path("/login")
        } else if (rejection.status === 403) {
          $rootScope.logout();
        }
        return $q.reject(rejection)
      }
    }
  }])


  $routeProvider.otherwise({redirectTo: '/login'});
}]);
