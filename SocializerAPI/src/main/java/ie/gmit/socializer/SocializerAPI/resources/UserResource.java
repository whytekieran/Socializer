package ie.gmit.socializer.SocializerAPI.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

	UserService uService = new UserService();
	
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/new")
	public Response newUser(@Context UriInfo uriInfo, User user, @HeaderParam("Content-Type") String contentTypeValue){
		
		UUIDs uuid = uService.createUser(user);
		String uri = uriInfo.getAbsolutePath().toString();
	
		try 
		{
			String response = "{'result':'true'}";

			return Response.status(Status.CREATED)
						.entity(response)
						.header("Content-Type", contentTypeValue)
						.build();
		} 
		catch (Exception e) 
		{
			String responseFail = "{'result':'false'}";

			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(responseFail)
					.header("Content-Type", contentTypeValue)
					.build();
		}
	}
}
