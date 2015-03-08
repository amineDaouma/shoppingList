'use strict';

describe('Create account controller', function() {

    var httpBackend,
        scope,
        authService,
        ctrl,
        newAccount = {
            username: "Nicolas Durand",
            email: "ndurand@xebia.fr",
            password: "password"
        },
        createdUser = {
            userId: 123456,
            username: "Nicolas Durand",
            email: "ndurand@xebia.fr"
        };

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, _authService_) {
        authService = _authService_;
        httpBackend = _$httpBackend_;
        scope = $rootScope.$new();
        ctrl = $controller('accountCreationCtrl', {$scope: scope});
    }));

    it('should initiate AccountCreationCtrl', function() {
        expect(scope.newAccount).toBeDefined();
    });

    it('should send account creation to backend', inject(function($location) {

        httpBackend
            .whenPOST('/api/users', newAccount)
            .respond(201, createdUser);

        scope.newAccount = newAccount;

        scope.create();
        httpBackend.flush();
        httpBackend.expectPOST('/api/users');
        expect($location.path()).toBe('/me');
        expect(authService.isLoggedIn()).toBe(true);
        expect(authService.currentUser()).toEqual(createdUser);
    }));

});
