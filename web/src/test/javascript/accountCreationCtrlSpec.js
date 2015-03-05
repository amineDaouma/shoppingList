'use strict';

describe('Create account controller', function() {

    var httpBackend,
        scope,
        ctrl,
        newAccount = {
            username: "Nicolas Durand",
            email: "ndurand@xebia.fr",
            password: "password"
        };

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
        httpBackend = _$httpBackend_;
        scope = $rootScope.$new();
        ctrl = $controller('AccountCreationCtrl', {$scope: scope});
    }));

    it('it should initiate AccountCreationCtrl', function() {
        expect(scope.newAccount).toBeDefined();
    });

    it('should send account creation to backend', inject(function($location) {

        httpBackend
            .whenPOST('/api/users', newAccount)
            .respond(201, newAccount);

        scope.newAccount = newAccount;

        scope.create();
        httpBackend.flush();
        httpBackend.expectPOST('/api/users');
        expect($location.path()).toBe('/me');
    }));

});
