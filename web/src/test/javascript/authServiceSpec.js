'use strict';

describe('Authentication service', function() {

    var service;

    beforeEach(module('shopping-list'));

    beforeEach(inject(function(_authService_) {
        service = _authService_;
    }));

    it('should not be loggedIn by default', function() {
        expect(service.isLoggedIn()).toBe(false);
        expect(service.currentUser()).toBeUndefined();
    });

    it('should not loggedIn when a user is logged in', function() {
        var user = {userId: 12345, email: 'test@test.fr', username: 'username', password: 'password'};

        service.setLoggedInUser(user);

        expect(service.isLoggedIn()).toBe(true);
        expect(service.currentUser()).toBe(user);
    });
});
