'use strict';

angular.module('myApp.teacher', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/teacher', {
            templateUrl: 'teacher/teacher.html',
            controller: 'TeacherCtrl'
        });

        $routeProvider.when("/profile", {
            templateUrl: 'teacher/profile.html'
        });
    }])

    .controller('TeacherCtrl', ['$scope','$http','$location','$rootScope', function($scope,$http,$location,$rootScope) {
        $scope.type = "";


        $scope.init = function () {

            //initialize with available subjects. Currently only English and Math are available
            $http({
                method: 'GET',
                url: '/sathelper/students/'+$rootScope.subjects[0].name
            }).then(function successCallback(response) {
                $scope.students = response.data;
            });
        };
        $scope.testResults = {

        };
        $scope.save = function() {


            var results = [];
            for(var i=0;i<$scope.students.length;i++) {
                var result = {
                    user:{
                        id:$scope.students[i].id
                    },
                    subject:{
                        id:1
                    },
                    score:$scope.students[i].score
                };
                if ($scope.students[i].score != null && $scope.students[i].score.length > 0) {
                    results.push(result);
                }
                if(results.length > 10){
                    alert("Entered results for "+results.length+" Can send test results for 10 students only");
                    results = [];
                    return;
                }

            }
            $scope.testResults.results = results;

            $http({
                method: 'POST',
                url: '/sathelper/testResults',
                data:$scope.testResults,
                dataType:'json',
                headers: {
                    "Content-Type": "application/json"
                }
            }).then(function successCallback(response) {
                alert("Completed");
                $location.path("/profile");
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $rootScope.errors = "Data Validation errors.Please correct and submit";
            });
        };


        $scope.init();

        $scope.cancel = function() {
            $scope.testResults.results=[];
        };


    }]);