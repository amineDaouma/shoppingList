'use strict';

describe('Manage list controller', function() {

    var httpBackend,
        scope,
        ctrl,
        param,
        list = {id: 1, title: "MyList", products: ["milk", "eggs", "pastas"]};

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
        httpBackend = _$httpBackend_;
        scope = $rootScope.$new();
        param = $routeParams;
        param.listId = 12;
        ctrl = $controller('listCtrl', {$scope: scope});
        httpBackend.whenGET('/api/users/1/lists/12').respond(200, list);
    }));

    it('it should initiate ListCtrl', function() {
        httpBackend.flush();

        expect(scope.list).toEqual(list);
        expect(scope.newProduct).toBeDefined();
    });

    it('should add a product and notify the server', inject(function() {
        scope.newProduct = "salad";
        httpBackend
            .whenPOST('/api/users/1/lists/12/products', scope.newProduct)
            .respond(201, scope.newProduct);

        scope.addProductToList();

        httpBackend.flush();
        httpBackend.expectPOST('/lists/12/products');
        expect(scope.list.products).toContain("salad");
        expect(scope.newProduct).toBe("");
    }));
});
