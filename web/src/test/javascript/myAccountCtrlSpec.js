'use strict';

describe('My account controller', function() {

    var httpBackend,
        scope,
        authService,
        ctrl,
        newList = {id: 2, name: 'Apéro tonight'},
        lists = [{id: 1, name: 'Romantic dinner'}];

    var loggedInUser = {userId: 12345, email: 'email@test.fr', username: 'username'};

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, _authService_) {
        httpBackend = _$httpBackend_;
        authService = _authService_;
        authService.setLoggedInUser(loggedInUser);
        scope = $rootScope.$new();
        ctrl = $controller('MyAccountCtrl', {$scope: scope});
        httpBackend.whenGET('/api/users/12345/lists').respond(200, lists);
    }));

    it('it should initiate MyAccountCtrl', function() {
        expect(scope.newListName).toBeDefined();
        expect(scope.myShoppingLists).toBeDefined();
    });

    it('it should create one new shopping list', inject(function($location) {
        httpBackend
            .whenPOST('/api/users/12345/lists', newList.name)
            .respond(201, newList);

        scope.newListName = newList.name;

        scope.create();
        httpBackend.flush();
        httpBackend.expectPOST('/api/users/12345/lists');
        expect(scope.myShoppingLists.length).toEqual(2);
        expect(scope.myShoppingLists[1]).toEqual(newList);
        expect($location.path()).toBe('/me');
    }));

});
