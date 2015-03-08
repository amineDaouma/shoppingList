'use strict';

shoppingList.controller('myAccountCtrl', [ '$scope', '$http', '$location', 'authService', function($scope, $http, $location, authService) {
    $scope.newListName = '';
    $scope.currentUser = {};

    $scope.$watch( authService.isLoggedIn, function () {
        $scope.currentUser = authService.currentUser();
    });

    function initCurrentUser() {
        $scope.currentUser = authService.currentUser();
    }

    $scope.create = function () {
        $http.post('/api/users/' + $scope.currentUser.userId + '/lists', $scope.newListName)
            .success(function(data) {
                $scope.currentUser.lists.push(data);
                $scope.newListName = '';
                $location.path('/me');
            });
    };

    initCurrentUser();
}]);
