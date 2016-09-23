/**
 * Created by Chandana on 9/18/2016.
 */
'use strict';

angular.module('myApp.login', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl: 'login/login.html',
            controller: 'LoginCtrl'
        });
        $routeProvider.when('/register', {
            templateUrl: 'login/register.html',
            controller: 'LoginCtrl'
        });
    }])

    .controller('LoginCtrl', ['$scope','$http','$location','$rootScope',function($scope,$http,$location,$rootScope) {

        $scope.user = {};
        $scope.passwords = {};



        $scope.login = function() {


            $http({
                method: 'POST',
                url: '/sathelper/users/login',
                data:{email:$scope.user.userName,password:$scope.user.password},
                transformResponse: []
            }).then(function successCallback(response) {
                $rootScope.authToken=response.data;
                $http({
                    method: 'GET',
                    url: '/sathelper/users/subject'
                }).then(function successCallback(response) {
                    $rootScope.subjects = response.data;
                });


                $rootScope.logout = function() {
                     $rootScope.authToken = null;
                    $location.path("/login")
                };
                $location.path("/profile");
            }, function(response) {
                $rootScope.authToken = null;
                $location.path("/login")
            });


        };


        $scope.makeAuth = function() {
                var tok = $scope.user.userName + ':' + $scope.user.password;
                var hash = window.btoa(tok);
                return "Basic " + hash;
        };



        $scope.init = function () {

            //initialize with available subjects. Currently only English and Math are available
            $http({
                method: 'GET',
                url: '/sathelper/register/subjects'
            }).then(function successCallback(response) {
                $scope.subjects = response.data;
                $scope.subjects.push({
                    id:'ALL',
                    name:'ALL'
                });
            });
        };

        $scope.register = function() {
            if ($scope.passwords.password1 == $scope.passwords.password2) {
                $scope.user.password = $scope.passwords.password1;

            }

            if ($scope.selectedSubject != null) {
                $scope.user.subjects = [];
                if ($scope.selectedSubject == 'ALL') {
                    for(var i=0;i<$scope.subjects.length;i++) {
                        if ($scope.subjects[i].id != 'ALL') {
                            $scope.user.subjects.push({
                                id:$scope.subjects[i].id
                            });
                        }
                    }
                } else {
                    $scope.user.subjects = [{
                        id: $scope.selectedSubject
                    }]
                }
            }
            var role="ROLE_STUDENT";
            if ($scope.isTeacher) {
                role = "ROLE_TEACHER";
            }
            $http({
                method: 'POST',
                url: '/sathelper/register/' + role,
                data:$scope.user,
                dataType:'json',
                headers: {
                    "Content-Type": "application/json"
                }
            }).then(function successCallback(response) {
                if (response.status != 201) {
                    $scope.error=response;
                }  else {
                    $location.path("/login");
                }
            }, function errorCallback(response) {
                $scope.error="Error creating user";
            });
        };


        $scope.init();

        $scope.cancel = function() {
            $scope.user={};
        };


    }]);