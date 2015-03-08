package io.pvardanega.shoppinglist.users;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CONFLICT;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import io.pvardanega.shoppinglist.users.shoppinglist.ShoppingList;

@Path("users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UsersResource {

    private UsersRepository usersRepository;

    @Inject
    public UsersResource(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @POST
    public Response createUser(User user) {
        return ok(usersRepository.create(user).toUser()).status(CREATED).build();
    }

    @Path("{userId}")
    @DELETE
    public Response removeUser(@PathParam("userId") Long userId) {
        usersRepository.remove(userId);
        return ok().build();
    }

    @Path("{userId}/lists")
    @POST
    public Response addNewList(@PathParam("userId") Long userId, String listName) {
        User user = usersRepository.get(userId).toUser();
        Optional<ShoppingList> existingList = user.lists.
                stream().
                filter(list -> list.name.equals(listName))
                .findFirst();
        if (existingList.isPresent()) {
            return status(CONFLICT).entity("A list with name '" + listName + "' already exists!").build();
        }
        ShoppingList shoppingList = new ShoppingList(listName);
        usersRepository.addNewListTo(userId, shoppingList);
        return ok(shoppingList).status(CREATED).build();
    }

    @Path("{userId}/lists/{listName}")
    @GET
    public Response retrieveList(@PathParam("userId") Long userId,
                                 @PathParam("listName") String listName) {
        Optional<ShoppingList> listFound = usersRepository.get(userId).lists.
                stream().
                filter(list -> list.name.equals(listName)).
                findFirst();

        if (listFound.isPresent()) {
            return ok(listFound.get()).build();
        }
        return status(NOT_FOUND).build();
    }

    @Path("{userId}/lists/{listId}/products")
    @PUT
    public Response addProductToList(@PathParam("userId") Long userId,
                                     @PathParam("listId") String listName,
                                     String product) {
        Optional<ShoppingList> listFound = usersRepository.get(userId).lists.
                stream().
                filter(list -> list.name.equals(listName)).
                findFirst();
        if (listFound.isPresent()) {
            usersRepository.addProductToList(userId, listName, product);
            return ok(product).type(TEXT_PLAIN).build();
        }
        return status(NOT_FOUND).
                entity("Cannot add produt '" + product + "' to list '" + listName + "': list not found for user with id '" + userId + "'.").
                build();
    }
}
