'use strict';

shoppingList.factory('authService', function() {
    var currentUser;

    return {
        setLoggedInUser: function(user) { currentUser = user },
        isLoggedIn: function() { return currentUser != null },
        currentUser: function() { return currentUser; }
    };
});
