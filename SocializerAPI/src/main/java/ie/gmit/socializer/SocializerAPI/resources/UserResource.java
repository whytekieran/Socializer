package ie.gmit.socializer.SocializerAPI.resources;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ie.gmit.socializer.SocializerAPI.models.User;
import ie.gmit.socializer.SocializerAPI.models.UserLogin;
import ie.gmit.socializer.SocializerAPI.responseObjects.UserIdResponse;
import ie.gmit.socializer.SocializerAPI.services.UserService;
import io.netty.util.internal.SystemPropertyUtil;

@Path("/user")
public class UserResource {

	//Global variables
	//User service handles all communication (CRUD) for User objects between resource URL's and 
	//Cassandra by using Mapper classes
	UserService uService = new UserService();
	ObjectMapper mapper = new ObjectMapper();	//For JSON/Java conversion
	String jsonResponseString;					//Holds JSON response
	
	//CREATING A NEW USER
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/new")
	public Response newUser(@Context UriInfo uriInfo, User user, 
			@HeaderParam("Content-Type") String contentTypeValue, 
			@HeaderParam("Password") String password){
		
		UserIdResponse ur;						//Response Object to be converted to JSON
		String hashedPassword = uService.hashPassword(password);
		UUID uuid = uService.createUser(user, hashedPassword);

		try 
		{
			ur = new UserIdResponse("true", uuid);
			jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
			
				return Response.status(Status.CREATED)
						.entity(jsonResponseString)
						.header("Content-Type", contentTypeValue)
						.build();
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			ur = new UserIdResponse("false", null);
			
			try {
				jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
			} catch (JsonProcessingException e1) {
				System.out.println(e1.getMessage());
			}
			
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
		}
	}
	
	//LOGGING IN A USER
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response loginUser(UserLogin user, @HeaderParam("Content-Type") String contentTypeValue){
		
		UserIdResponse ur;
		UUID uuid = uService.validateUser(user);
		
		if(uuid != null){
			ur = new UserIdResponse("true", uuid);
				
			try {
			jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
			} catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}
				
			return Response.status(Status.CREATED)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
				
		}
		else{
			ur = new UserIdResponse("false", uuid);
			try {
			jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
			} catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}
				
			return Response.status(Status.CREATED)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
		}
	}
	
	//RETRIEVING A USER
	@GET
	@Path("/get/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("user_uuid") UUID user_uuid){
		return uService.getUser(user_uuid);
	}
	
	//DELETING A USER
	@DELETE
	@Path("/delete/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("user_uuid") UUID user_uuid){
		boolean deleted = uService.deleteUser(user_uuid);
		if(deleted){
			String response = "{'result':'true'}";

			return Response.status(Status.CREATED)
						.entity(response)
						.header("Content-Type", "application/json")
						.build();
		}else{
			String responseFail = "{'result':'false'}";

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(responseFail)
					.header("Content-Type", "application/json")
					.build();
		}
	}
	
	//UPDATING A USER...ALL FIELDS
	@PUT
	@Path("/update/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(User updateUser){
		boolean updated = uService.updateUser(updateUser);
		if(updated){
			String response = "{'result':'true'}";

			return Response.status(Status.CREATED)
						.entity(response)
						.header("Content-Type", "application/json")
						.build();
		}else{
			String responseFail = "{'result':'false'}";

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(responseFail)
					.header("Content-Type", "application/json")
					.build();
		}
	}
}
