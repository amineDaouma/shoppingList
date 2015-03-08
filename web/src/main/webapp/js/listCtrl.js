'use strict';

shoppingList.controller('listCtrl', [ '$scope', '$http', '$routeParams', 'authService', function($scope, $http, $routeParams, authService) {
    $scope.newProduct = '';
    $scope.listName = '';
    $scope.currentUser = {};

    $scope.$watch(authService.isLoggedIn, function () {
        $scope.currentUser = authService.currentUser();
    });

    function initCurrentUser() {
        $scope.listName = $routeParams.listName;
        $scope.currentUser = authService.currentUser();
    }

    $scope.addProductToList = function () {
        $http.put('/api/users/' + $scope.currentUser.userId + '/lists/' + $scope.listName + '/products', $scope.newProduct)
            .success(function (data) {
                $scope.currentUser.lists.forEach(function (element) {
                    if (element.name == $scope.listName) {
                        element.products.push(data);
                    }
                });
                $scope.newProduct = '';
            });
    };

    $scope.products = function () {
        var products;
        $scope.currentUser.lists.forEach(function (element) {
            if (element.name == $scope.listName) {
                products = element.products;
            }
        });
        return products;
    };

    initCurrentUser();
}]);
