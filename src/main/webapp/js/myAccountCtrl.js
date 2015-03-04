shoppingList.controller('MyAccountCtrl', [ '$scope', '$http', '$location', function($scope, $http, $location) {
    $scope.newListTitle = '';
    $scope.myShoppingLists = [];

    $http.get('/api/users/1/lists')
        .success(function (data) {
            $scope.myShoppingLists = data;
        })
    ;

    $scope.create = function () {
        $http.post('/api/users/1/lists', $scope.newListTitle)
            .success(function(data) {
                $scope.myShoppingLists.push(data);
                $scope.newListTitle = "";
                $location.path('/me');
            });
    };
}]);
