shoppingList.controller('MyAccountCtrl', [ '$scope', '$http', '$location', 'authService', function($scope, $http, $location, authService) {
    $scope.newListName = '';
    $scope.myShoppingLists = [];

    $http.get('/api/users/' + authService.currentUser().userId + '/lists')
        .success(function (data) {
            $scope.myShoppingLists = data;
        })
    ;

    $scope.create = function () {
        $http.post('/api/users/' + authService.currentUser().userId + '/lists', $scope.newListName)
            .success(function(data) {
                $scope.myShoppingLists.push(data);
                $scope.newListName = "";
                $location.path('/me');
            });
    };
}]);
