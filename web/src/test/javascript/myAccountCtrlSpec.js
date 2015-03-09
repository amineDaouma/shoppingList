'use strict';

describe('My account controller', function() {

    var httpBackend,
        scope,
        authService,
        ctrl,
        newList = {name: 'Apéro tonight', products: []};

    var loggedInUser;

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, _authService_) {
        loggedInUser = {
            userId: 12345,
            email: 'email@test.fr',
            username: 'username',
            lists: [{name: 'list1', products: ['milk', 'cheese']}, {name: 'list2', products: ['vegetables', 'beef']}]
        };
        httpBackend = _$httpBackend_;
        authService = _authService_;
        authService.setLoggedInUser(loggedInUser);
        scope = $rootScope.$new();
        ctrl = $controller('myAccountCtrl', {$scope: scope});
    }));

    it('should initiate MyAccountCtrl', function() {
        expect(scope.newListName).toBeDefined();
        expect(scope.currentUser).toEqual(loggedInUser);
    });

    it('should create one new shopping list', inject(function($location) {
        httpBackend
            .whenPOST('/api/users/12345/lists', newList.name)
            .respond(201, newList);

        scope.newListName = newList.name;

        scope.create();
        httpBackend.flush();
        httpBackend.expectPOST('/api/users/12345/lists');
        expect(scope.currentUser.lists.length).toEqual(3);
        expect(scope.currentUser.lists[2]).toEqual(newList);
    }));

    it('should display an error message when creating a new shopping list with an already used name', inject(function($location) {
        httpBackend
            .whenPOST('/api/users/12345/lists', loggedInUser.lists[0].name)
            .respond(409, "Error message");

        scope.newListName = loggedInUser.lists[0].name;

        scope.create();
        httpBackend.flush();
        httpBackend.expectPOST('/api/users/12345/lists', loggedInUser.lists[0].name);
        expect(scope.errorMessage).toEqual("Error message");
        expect(scope.currentUser.lists.length).toEqual(2);
    }));

});
