package io.pvardanega.shoppinglist.users;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UsersResource {

    private UserRepository userRepository;

    @Inject
    public UsersResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Path("{userId}/lists/{listId}/products")
    @POST
    public Response addProductToList(@PathParam("userId") Long userId,
                                     @PathParam("listId") Long listId,
                                     String product) {
        User user = userRepository.get(userId);
        user.lists.
            stream().
            filter(list -> listId.equals(list.id)).
            findFirst().
            ifPresent(list -> list.addProduct(product));
        return ok(product).build();
    }

    @POST
    public Response createUser(User user) {
        return ok(userRepository.create(user)).status(CREATED).build();
    }

    @Path("{userId}")
    @DELETE
    public Response removeUser(@PathParam("userId") Long userId) {
        userRepository.remove(userId);
        return ok().build();
    }

    @Path("{userId}/lists")
    @POST
    public Response addNewList(@PathParam("userId") Long userId, String listName) {
        User user = userRepository.get(userId);

        ShoppingList shoppingList = new ShoppingList(user.lists.size() + 1L, listName);
        user.lists.add(shoppingList);

        return ok(shoppingList).status(CREATED).build();
    }

    @Path("{userId}/lists")
    @GET
    public Response retrieveAllLists(@PathParam("userId") Long userId) {
        return
                ok().
                entity(userRepository.get(userId).lists).
                build();
    }

    @Path("{userId}/lists/{listId}")
    @GET
    public Response retrieveList(@PathParam("userId") Long userId,
                                 @PathParam("listId") Long listId) {
        User user = userRepository.get(userId);
        Optional<ShoppingList> listFound = user.lists.
                                            stream().
                                            filter(list -> listId.equals(list.id)).
                                            findFirst();

        if (listFound.isPresent()) {
            return ok(listFound.get()).build();
        }
        return status(NOT_FOUND).build();
    }
}
