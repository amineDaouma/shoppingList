'use strict';

describe('My account controller', function() {

    var httpBackend,
        scope,
        ctrl,
        newList = {id: 2, name: 'Apéro tonight'},
        lists = [{id: 1, name: 'Romantic dinner'}];

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
        httpBackend = _$httpBackend_;
        scope = $rootScope.$new();
        ctrl = $controller('MyAccountCtrl', {$scope: scope});
        httpBackend.whenGET('/api/users/1/lists').respond(200, lists);
    }));

    it('it should initiate MyAccountCtrl', function() {
        expect(scope.newListName).toBeDefined();
        expect(scope.myShoppingLists).toBeDefined();
    });

    it('it should create one new shopping list', inject(function($location) {
        httpBackend
            .whenPOST('/api/users/1/lists', newList.name)
            .respond(201, newList);

        scope.newListName = newList.name;

        scope.create();
        httpBackend.flush();
        httpBackend.expectPOST('/api/users/1/lists');
        expect(scope.myShoppingLists.length).toEqual(2);
        expect(scope.myShoppingLists[1]).toEqual(newList);
        expect($location.path()).toBe('/me');
    }));

});
