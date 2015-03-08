'use strict';

var shoppingList = angular.module('shopping-list', ['ngRoute']);

shoppingList.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/signin', {
                templateUrl: 'views/signin.html',
                controller: 'accountCreationCtrl'
            }).
            when('/me', {
                templateUrl: 'views/me.html',
                controller: 'myAccountCtrl'
            }).
            when('/lists/:listName', {
                templateUrl: 'views/list.html',
                controller: 'listCtrl'
            }).
            otherwise({
                redirectTo: 'index.html'
            });
    }
]);
