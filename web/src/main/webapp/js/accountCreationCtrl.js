shoppingList.controller('AccountCreationCtrl', [ '$scope', '$http', '$location', 'authService', function($scope, $http, $location, authService) {
    $scope.newAccount = {};

    $scope.create = function() {
        $http.post('/api/users', $scope.newAccount)
            .success(function(data) {
                authService.setLoggedInUser(data);
                $location.path('/me');
            });
    };
}]);
