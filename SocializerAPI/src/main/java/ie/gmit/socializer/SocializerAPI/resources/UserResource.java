package ie.gmit.socializer.SocializerAPI.resources;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
import ie.gmit.socializer.SocializerAPI.models.AddUserConnection;

import ie.gmit.socializer.SocializerAPI.models.User;
import ie.gmit.socializer.SocializerAPI.models.UserLogin;
import ie.gmit.socializer.SocializerAPI.responseObjects.ResponseWithMessage;
import ie.gmit.socializer.SocializerAPI.responseObjects.NewConnectionResponse;
import ie.gmit.socializer.SocializerAPI.responseObjects.UserIdResponse;
import ie.gmit.socializer.SocializerAPI.services.UserService;
import java.util.Date;

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
		catch (JsonProcessingException e) 
		{
			System.out.println(e.getMessage());
			ur = new UserIdResponse("false", null);
			
			try {
                            jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                            return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
			} catch (JsonProcessingException e1) {
				System.out.println(e1.getMessage());
                        }
		}
                
                //Default if everything goes wrong
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
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
                        return Response.status(Status.CREATED)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
			} catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}	
		}
		else{
			ur = new UserIdResponse("false", uuid);
			try {
			jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                        return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
			} catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}
		}
                
                //Default if everything goes wrong
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
	}
	
	//RETRIEVING A USER
	@GET
	@Path("/get/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("user_uuid") UUID user_uuid){
		
		ResponseWithMessage failResponse;
		User foundUser = uService.getUser(user_uuid);
                
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		if(foundUser != null){
			try {
                            jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundUser);	
                            return Response.status(Status.CREATED)
					.entity(jsonResponseString)
					.header("Content-Type", "application/json")
					.build();
			}catch (JsonProcessingException e) {
				System.out.println(e.getMessage());
			}
		}
		else
		{
			failResponse = new ResponseWithMessage("false", "Could not find this user");
			
			try {
                            jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);
                            return Response.status(Status.NOT_FOUND)
					.entity(jsonResponseString)
					.header("Content-Type", "application/json")
					.build();
			} catch (JsonProcessingException e) {
                            System.out.println(e.getMessage());
			}
		}
            
            //Default if everything goes wrong
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity("Something went wrong")
                                    .header("Content-Type", "text/plain")
                                    .build(); 
	}
        
        //ADDING A USER CONNECTION
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addconnection")
	public Response addConnection(AddUserConnection connectionIds, @HeaderParam("Content-Type") String contentTypeValue){
            
            ResponseWithMessage ur;
            NewConnectionResponse successResponse;
            boolean connectionAdded = uService.connectUsers(connectionIds);
            
            if(connectionAdded){
                successResponse = new NewConnectionResponse("true", connectionIds.getRequestedFriendId(), connectionIds.getCurrentUserId());
                try {
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);
                    return Response.status(Status.CREATED)
					.entity(jsonResponseString)
					.header("Content-Type", "application/json")
					.build(); 
                } catch (JsonProcessingException ex) {
                       System.out.println(ex.getMessage());
            }
            }
            else{
                 ur = new ResponseWithMessage("false", "Something went wrong when adding the connection");
                 try {
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                    return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity(jsonResponseString)
                                    .header("Content-Type", "application/json")
                                    .build(); 
                } catch (JsonProcessingException ex1) {
                    System.out.println(ex1.getMessage());
                }
            }
            
            //Default if everything goes wrong
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity("Something went wrong")
                                    .header("Content-Type", "text/plain")
                                    .build(); 
	}
        
        //DELETING A USER CONNECTION
	@POST
        @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteconnection")
        public Response deleteConnection(AddUserConnection connectionIds, @HeaderParam("Content-Type") String contentTypeValue){
            
            ResponseWithMessage ur;
            
            boolean connectionDeleted = uService.disconnectUsers(connectionIds);
            if(connectionDeleted){
                
                ur = new ResponseWithMessage("true", "You are no longer connected with the user you requested");
                try {
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                    return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity(jsonResponseString)
                                    .header("Content-Type", "application/json")
                                    .build(); 
                } catch (JsonProcessingException ex1) {
                    System.out.println(ex1.getMessage());
                }
            }
            else{
                
                ur = new ResponseWithMessage("false", "Something went wrong when trying to disconnected you from this user");
                try {
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                    return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity(jsonResponseString)
                                    .header("Content-Type", "application/json")
                                    .build(); 
                } catch (JsonProcessingException ex1) {
                    System.out.println(ex1.getMessage());
                }
            }
            
            //Default if everything goes wrong
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity("Something went wrong")
                                    .header("Content-Type", "text/plain")
                                    .build(); 
        }
        
        
	//DELETING A USER
	@DELETE
	@Path("/delete/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("user_uuid") UUID user_uuid){
            
                UserIdResponse ur;
		boolean deleted = uService.deleteUser(user_uuid);
                
		if(deleted){
                    ur = new UserIdResponse("true", user_uuid);
                        
                    try {
                        jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                        return Response.status(Status.ACCEPTED)
                                        .entity(jsonResponseString)
					.header("Content-Type", "application/json")
					.build();
                            
                    }catch (JsonProcessingException ex1) {
                            System.out.println(ex1.getMessage());
                    }
		}
                else
                {
                    ur = new UserIdResponse("false", user_uuid);
                 
                    try {
                         jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                    } catch (JsonProcessingException ex1) {
                        System.out.println(ex1.getMessage());
                    }
                        
                    return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponseString)
					.header("Content-Type", "application/json")
					.build();
		}
                
            //Default if everything goes wrong
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                                    .entity("Something went wrong")
                                    .header("Content-Type", "text/plain")
                                    .build(); 
	}
	
	//UPDATING A USER...ALL FIELDS
        //IMPORTANT NOTE, database wont update fields proparly unless users email, uuid and slug match....so here none
        //of those attributes are editable. (Email, Slug or user_uuid are never meant to be passed in a request to this endpoint)
	@PUT
	@Path("/update/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(User updateUser, @PathParam("user_uuid") UUID user_uuid, 
                @HeaderParam("Content-Type") String contentTypeValue){
                
                ResponseWithMessage rwm;
                UserIdResponse ur;
                User currentUser = uService.checkIfUserExists(user_uuid);
                
                if(currentUser != null){
                
                    //IMPORTANT
                    //Also need to hold the users password before updated!!!!!!!
                    String currentUserSlug = currentUser.getSlug();
                    String currentUserEmail = currentUser.getEmail();
                    Date created = currentUser.getCreated();
                    currentUser = updateUser;                   //The current user is equal to the updatedUser that was just passed
                    currentUser.setUser_uuid(user_uuid);        //Make sure his id is still the same, updateUser would not have this.
                    currentUser.setSlug(currentUserSlug);       //Do the same with slug
                    currentUser.setEmail(currentUserEmail);     //and same with the email (email, slug and id must be maintained for update)
                    currentUser.setCreated(created);            //Maintain created date and also update the updated date
                    currentUser.setUpdated(new Date());

                    boolean updated = uService.updateUser(currentUser);//Update the user and get back the result.
                
                    if(updated){
                            ur = new UserIdResponse("true", user_uuid);
                            
                            try {
                                jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ur);
                                return Response.status(Status.ACCEPTED)
						.entity(jsonResponseString)
						.header("Content-Type", contentTypeValue)
						.build();
                                
                            } catch (JsonProcessingException ex1) {
                                System.out.println(ex1.getMessage());
                            }
                    }else{//we found the user but something went wrong during the update
                        
                            rwm = new ResponseWithMessage("false", "Sorry something went wrong during the user update");
                        
                            try {
                                jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rwm);
                                return Response.status(Status.INTERNAL_SERVER_ERROR)
                                            .entity(jsonResponseString)
                                            .header("Content-Type", contentTypeValue)
                                            .build();
                                
                            } catch (JsonProcessingException ex1) {
                                System.out.println(ex1.getMessage());
                            }
                    }
                }
                else{//if the user we search for comes back null
                    
                    rwm = new ResponseWithMessage("false", "We could not find this user");
                    
                    try {
                        jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rwm);
                        return Response.status(Status.INTERNAL_SERVER_ERROR)
                                            .entity(jsonResponseString)
                                            .header("Content-Type", contentTypeValue)
                                            .build();
                        
                    } catch (JsonProcessingException ex1) {
                        System.out.println(ex1.getMessage());
                    }
                }
                
            //Default if everything goes wrong
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
                
	}
        
        //UPDATING A USER....ONE FIELD @PATCH not supported in Jersey framework
        //This should only be used if updating username or password. An update of any other field can be handled by the update
        //type that has been defined above this one. ...All others, eg user_uuid and slug should never allow a user to edit them.
        @PUT
	@Path("/updatesingle/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response updateUserField(@PathParam("user_uuid") UUID user_uuid, @HeaderParam("User-Attribute") String userAttribute,
                User userUpdate){
            
            boolean result = uService.updateUserAttribute(user_uuid, userAttribute, userUpdate);
            
            if(result){
                return Response.status(Status.CREATED)
                                .entity("Okay all good!!!!")
                                .header("Content-Type", "text/plain")
                                .build(); 
            }
            else{
                return Response.status(Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
            }
        }
}
