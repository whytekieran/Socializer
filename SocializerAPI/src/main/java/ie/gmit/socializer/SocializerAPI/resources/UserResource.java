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

import com.datastax.driver.core.utils.UUIDs;

import ie.gmit.socializer.SocializerAPI.models.User;
import ie.gmit.socializer.SocializerAPI.services.UserService;

@Path("/user")
public class UserResource {

	//User service handles all communication (CRUD) for User objects between resource URL's and 
	//Cassandra by using Mapper classes
	UserService uService = new UserService();
	
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/new")
	public Response newUser(@Context UriInfo uriInfo, User user, @HeaderParam("Content-Type") String contentTypeValue){
		
		UUID uuid = uService.createUser(user);
		String uri = uriInfo.getAbsolutePath().toString();
	
		try 
		{
			//String response = "{'result':'true'}";

			return Response.status(Status.CREATED)
						.entity("It Worked")
						.header("Content-Type", "text/plain")
						.build();
		} 
		catch (Exception e) 
		{
			//String responseFail = "{'result':'false'}";

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Nope!!")
					.header("Content-Type", "text/plain")
					.build();
		}
	}
	
	@GET
	@Path("/get/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("user_uuid") UUID user_uuid){
		return uService.getUser(user_uuid);
	}
	
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
