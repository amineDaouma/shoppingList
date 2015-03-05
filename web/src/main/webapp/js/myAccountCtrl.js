shoppingList.controller('MyAccountCtrl', [ '$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.newListName = '';
    $scope.myShoppingLists = [];

    $http.get('/api/users/1/lists')
        .success(function (data) {
            $scope.myShoppingLists = data;
        })
    ;

    $scope.create = function () {
        $http.post('/api/users/1/lists', $scope.newListName)
            .success(function(data) {
                $scope.myShoppingLists.push(data);
                $scope.newListName = "";
                $location.path('/me');
            });
    };
}]);
