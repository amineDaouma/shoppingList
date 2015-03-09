'use strict';

shoppingList.controller('accountCreationCtrl', [ '$scope', '$http', '$location', 'authService', function($scope, $http, $location, authService) {
    $scope.newAccount = {};
    $scope.errorMessage = '';

    $scope.create = function() {
        $http.post('/api/users', $scope.newAccount)
            .success(function(data) {
                authService.setLoggedInUser(data);
                $location.path('/me');
            })
            .error(function(message) {
                $scope.errorMessage = message;
            })
        ;
    };
}]);
