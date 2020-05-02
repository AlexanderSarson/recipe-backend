package rest;
/*
 * author mads
 * version 1.0
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.UserDto;
import dtos.favourites.FavouriteRecipeDTO;
import dtos.favourites.FavouriteRecipeDtoList;
import entity.User;
import errorhandling.RecipeException;
import errorhandling.UserException;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get() {
        return Response.ok().build();
    }

    @POST
    @Path("favourites")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addToFavourite(String jsonString) throws UserException, RecipeException {
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        // Gets the username from the body
        String username = json.get("username").getAsString();
        // Gets the nested recipe object from the body
        FavouriteRecipeDTO favouriteRecipeDTO = new Gson().fromJson(json.get("recipe"), FavouriteRecipeDTO.class);
        // Get the action from the body
        String action = json.get("action").getAsString();

        UserDto user;
        if (action.toLowerCase().equals("add")) {
            user = USER_FACADE.addFavourite(username, favouriteRecipeDTO);
        } else if (action.toLowerCase().equals("remove")) {
            user = USER_FACADE.removeFavourite(username, favouriteRecipeDTO);
        } else {
            return Response.serverError().build();
        }

        return Response.ok(user).build();
    }
    
    @GET
    @Path("favourites/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getFavourites(@PathParam("username") String username) throws UserException {
        FavouriteRecipeDtoList favouriteRecipeDTOList = USER_FACADE.getFavourites(username);
        return Response.ok(favouriteRecipeDTOList).build();
    }
}