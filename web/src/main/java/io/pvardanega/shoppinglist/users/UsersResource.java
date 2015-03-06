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

    @Path("{userId}/lists/{listId}/products")
    @POST
    public Response addProductToList(@PathParam("userId") Long userId,
                                     @PathParam("listId") Long listId,
                                     String product) {
        UserEntity userEntity = usersRepository.get(userId);
        userEntity.lists.
            stream().
            filter(list -> listId.equals(list.id)).
            findFirst().
            ifPresent(list -> list.addProduct(product));
        return ok(product).build();
    }

    @Deprecated
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
        UserEntity userEntity = usersRepository.get(userId);

        ShoppingList shoppingList = new ShoppingList(userEntity.lists.size() + 1L, listName);
        userEntity.lists.add(shoppingList);

        return ok(shoppingList).status(CREATED).build();
    }

    @Path("{userId}/lists")
    @GET
    public Response retrieveAllLists(@PathParam("userId") Long userId) {
        return
                ok().
                entity(usersRepository.get(userId).lists).
                build();
    }

    @Path("{userId}/lists/{listId}")
    @GET
    public Response retrieveList(@PathParam("userId") Long userId,
                                 @PathParam("listId") Long listId) {
        UserEntity userEntity = usersRepository.get(userId);
        Optional<ShoppingList> listFound = userEntity.lists.
                                            stream().
                                            filter(list -> listId.equals(list.id)).
                                            findFirst();

        if (listFound.isPresent()) {
            return ok(listFound.get()).build();
        }
        return status(NOT_FOUND).build();
    }
}
