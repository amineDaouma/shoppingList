'use strict';

describe('Application routes', function() {

    var route;

    beforeEach(module('shopping-list'));

    beforeEach(inject(function($route) {
        route = $route;
    }));

    it('should map routes to controllers', function() {
        expect(route.routes['/signin'].templateUrl).toEqual('views/signin.html');
        expect(route.routes['/signin'].controller).toEqual('accountCreationCtrl');

        expect(route.routes['/me'].templateUrl).toEqual('views/me.html');
        expect(route.routes['/me'].controller).toEqual('myAccountCtrl');

        expect(route.routes['/lists/:listName'].templateUrl).toEqual('views/list.html');
        expect(route.routes['/lists/:listName'].controller).toEqual('listCtrl');

        // otherwise redirect to
        expect(route.routes[null].redirectTo).toEqual('index.html')
    });
});
