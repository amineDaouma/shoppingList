shoppingList.controller('listCtrl', [ '$scope', '$http', '$routeParams', 'authService', function($scope, $http, $routeParams, authService) {
    $scope.newProduct = "";
    $scope.list = {};

    $http.get('/api/users/' + authService.currentUser().userId + '/lists/' + $routeParams.listName)
        .success(function (data) {
            $scope.list = data;
        })
    ;

    $scope.addProductToList = function () {
        $http.post('/api/users/' + authService.currentUser().userId + '/lists/' + $routeParams.listName + '/products', $scope.newProduct)
            .success(function (data) {
                $scope.newProduct = "";
                $scope.list.products.push(data);
            }
        );
    };
}]);
