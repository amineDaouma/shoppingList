'use strict';

describe('Manage list controller', function() {

    var httpBackend,
        scope,
        authService,
        ctrl,
        param,
        list = {name: "MyList", products: ["milk", "eggs", "pastas"]};

    var loggedInUser = {userId: 12345, email: 'email@test.fr', username: 'username'};

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams, _authService_) {
        authService = _authService_;
        authService.setLoggedInUser(loggedInUser);
        httpBackend = _$httpBackend_;
        scope = $rootScope.$new();
        param = $routeParams;
        param.listName = "MyList";
        ctrl = $controller('listCtrl', {$scope: scope});
        httpBackend.whenGET('/api/users/12345/lists/MyList').respond(200, list);
    }));

    it('should initiate ListCtrl', function() {
        httpBackend.flush();

        expect(scope.list).toEqual(list);
        expect(scope.newProduct).toBeDefined();
    });

    it('should add a product and notify the server', inject(function() {
        scope.newProduct = "salad";
        httpBackend
            .whenPOST('/api/users/12345/lists/MyList/products', scope.newProduct)
            .respond(201, scope.newProduct);

        scope.addProductToList();

        httpBackend.flush();
        httpBackend.expectPOST('/api/users/12345/lists/MyList/products');
        expect(scope.list.products).toContain("salad");
        expect(scope.newProduct).toBe("");
    }));
});
