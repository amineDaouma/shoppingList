'use strict';

describe('Application routes', function() {

    it('should map routes to controllers', function() {
        module('shopping-list');

        inject(function($route) {

            expect($route.routes['/signin'].templateUrl).toEqual('views/signin.html');
            expect($route.routes['/signin'].controller).toEqual('AccountCreationCtrl');

            expect($route.routes['/me'].templateUrl).toEqual('views/me.html');
            expect($route.routes['/me'].controller).toEqual('MyAccountCtrl');

            expect($route.routes['/lists/:listId'].templateUrl).toEqual('views/list.html');
            expect($route.routes['/lists/:listId'].controller).toEqual('listCtrl');

            // otherwise redirect to
            expect($route.routes[null].redirectTo).toEqual('index.html')
        });
    });
});
