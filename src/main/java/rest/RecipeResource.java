/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.FoodResultDTOList;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import spoonacular.FoodFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author root
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Recipe API",
                version = "0.1",
                description = "This API is use as a base for building a backend for a separate Frontend",
                contact = @Contact(name = "Gruppe 2", email = "gruppe2@cphbusiness.dk")
        ),
        tags = {
            @Tag(name = "recipe", description = "API related to recipes"),
            @Tag(name = "login", description = "API related to Login"),},
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/recipe-backend"
            ),
            @Server(
                    description = "Server API",
                    url = "https://www.sarson.codes/recipe-backend"
            )
        }
)
@Path("recipe")
public class RecipeResource {

    private FoodFacade foodFacade = new FoodFacade();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RecipeResource
     */
    public RecipeResource() {
    }

    /**
     * Retrieves representation of an instance of rest.RecipeResource
     *
     * @return an instance of java.lang.String
     */
    @Operation(summary = "Search for recipes, given a part of a full title of the recipe",
            tags = {"search"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodResultDTOList.class))),
                @ApiResponse(responseCode = "200", description = "The found recipes")})
    @Path("/search")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchForRecipe(String json) {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        String name = object.get("name").getAsString();
        int number = object.get("number").getAsInt();
        return Response
                .ok(foodFacade.searchByName(name, number))
                .build();
    }

    @Operation(summary = "Get x random number of recipes",
            tags = {"random"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodResultDTOList.class))),
                @ApiResponse(responseCode = "200", description = "The found random recipes")})
    @Path("/random/{number}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandomRecipe(@PathParam("number") int number) {
        return Response
                .ok(foodFacade.getRandomRecipes(number))
                .build();
    }

}
