'use strict';

describe('Manage list controller', function() {

    var httpBackend,
        scope,
        authService,
        ctrl,
        param;

    var loggedInUser = {userId: 12345, email: 'email@test.fr', username: 'username', lists: [{name: "MyList", products: ["milk", "eggs", "pastas"]}]};

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, _authService_) {
        authService = _authService_;
        authService.setLoggedInUser(loggedInUser);
        httpBackend = _$httpBackend_;
        scope = $rootScope.$new();
        param = $routeParams;
        param.listName = "MyList";
        ctrl = $controller('listCtrl', {$scope: scope});
    }));

    it('should initiate ListCtrl', function() {
        expect(scope.newProduct).toBeDefined();
        expect(scope.listName).toBe('MyList');
        expect(scope.currentUser).toEqual(loggedInUser);
    });

    it('should return all products from current list', function() {
        expect(scope.products()).toEqual(loggedInUser.lists[0].products);
    });

    it('should add a product and notify the server', inject(function() {
        scope.newProduct = "salad";
        httpBackend
            .whenPUT('/api/users/12345/lists/MyList/products', scope.newProduct)
            .respond(201, scope.newProduct);

        scope.addProductToList();

        httpBackend.flush();
        httpBackend.expectPOST('/api/users/12345/lists/MyList/products');
        expect(scope.currentUser.lists[0].products).toContain("salad");
        expect(scope.newProduct).toBe("");
    }));
});
