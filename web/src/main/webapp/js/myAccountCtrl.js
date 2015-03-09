'use strict';

shoppingList.controller('myAccountCtrl', [ '$scope', '$http', 'authService', function($scope, $http, authService) {
    $scope.newListName = '';
    $scope.currentUser = {};
    $scope.errorMessage = '';

    $scope.$watch( authService.isLoggedIn, function () {
        $scope.currentUser = authService.currentUser();
    });

    function initCurrentUser() {
        $scope.currentUser = authService.currentUser();
    }

    $scope.create = function () {
        $http.post('/api/users/' + $scope.currentUser.userId + '/lists', $scope.newListName)
            .success(function(data) {
                cleanErrorMessage();
                $scope.currentUser.lists.push(data);
                $scope.newListName = '';
            })
            .error(function(data) {
                $scope.errorMessage = data;
                $scope.newListName = '';
            })
        ;
    };

    function cleanErrorMessage() {
        $scope.errorMessage = '';
    }

    initCurrentUser();
}]);
