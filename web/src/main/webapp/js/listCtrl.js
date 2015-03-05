shoppingList.controller('listCtrl', [ '$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
    $scope.newProduct = "";
    $scope.list = {};

    $http.get('/api/users/1/lists/' + $routeParams.listId)
        .success(function (data) {
            $scope.list = data;
        })
    ;

    $scope.addProductToList = function () {
        $http.post('/api/users/1/lists/' + $routeParams.listId + '/products', $scope.newProduct)
            .success(function (data) {
                $scope.newProduct = "";
                $scope.list.products.push(data);
            }
        );
    };
}]);
