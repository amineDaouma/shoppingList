package io.pvardanega.shoppinglist.users;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.ok;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.mongodb.DuplicateKeyException;
import io.pvardanega.shoppinglist.exception.ConflictException;
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
        try {
            return ok(usersRepository.create(user).toUser()).status(CREATED).build();
        } catch (DuplicateKeyException e) {
            throw new ConflictException("Email '" + user.email + "' already used!");
        }
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
        Optional<UserEntity> user = retrieveUserFromId(userId);
        if (findListFromName(listName, user.get().lists).isPresent()) {
            throw new ConflictException("A list with name '" + listName + "' already exists!");
        }

        ShoppingList shoppingList = new ShoppingList(listName);
        usersRepository.addNewListTo(userId, shoppingList);
        return ok(shoppingList).status(CREATED).build();
    }

    @Path("{userId}/lists/{listName}")
    @GET
    public Response retrieveList(@PathParam("userId") Long userId,
                                 @PathParam("listName") String listName) {
        Optional<UserEntity> user = retrieveUserFromId(userId);

        Optional<ShoppingList> listFound = findListFromName(listName, user.get().lists);
        if (listFound.isPresent()) {
            return ok(listFound.get()).build();
        }
        throw new NotFoundException("List with name '" + listName + "' not found for user with id '" + userId + "'!");
    }

    @Path("{userId}/lists/{listId}/products")
    @PUT
    public Response addProductToList(@PathParam("userId") Long userId,
                                     @PathParam("listId") String listName,
                                     String product) {
        Optional<UserEntity> user = retrieveUserFromId(userId);

        Optional<ShoppingList> listFound = findListFromName(listName, user.get().lists);
        if (listFound.isPresent()) {
            usersRepository.addProductToList(userId, listName, product);
            return ok(product).type(TEXT_PLAIN).build();
        }
        throw new NotFoundException("Cannot add produt '" + product + "' to list '" + listName + "': list not found for user with id '" + userId + "'.");
    }

    private Optional<ShoppingList> findListFromName(String listName, List<ShoppingList> lists) {
        return lists.
                stream().
                filter(list -> list.name.equals(listName)).
                findFirst();
    }

    private Optional<UserEntity> retrieveUserFromId(Long userId) {
        Optional<UserEntity> user = usersRepository.get(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("User with id '" + userId + "' not found!");
        }
        return user;
    }
}
