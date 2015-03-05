shoppingList.controller('AccountCreationCtrl', [ '$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.newAccount = {};

    $scope.create = function() {
        $http.post('/api/users', $scope.newAccount)
            .success(function() {
                $location.path('/me');
            });
    };
}]);
