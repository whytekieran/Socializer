package ie.gmit.socializer.SocializerAPI.resources;

import com.datastax.driver.mapping.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.gmit.socializer.SocializerAPI.models.Timeline;
import ie.gmit.socializer.SocializerAPI.models.TimelinePagination;
import ie.gmit.socializer.SocializerAPI.responseObjects.IdResponse;
import ie.gmit.socializer.SocializerAPI.responseObjects.LikeResponse;
import ie.gmit.socializer.SocializerAPI.responseObjects.NewTimelinePostResponse;
import ie.gmit.socializer.SocializerAPI.responseObjects.ResponseWithMessage;
import ie.gmit.socializer.SocializerAPI.responseObjects.TimelineResponse;
import javax.ws.rs.Path;

import ie.gmit.socializer.SocializerAPI.services.TimelineService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/timeline")
public class TimelineResource {

	//Timeline service handles all communication (CRUD) for Timeline objects between resource URL's and 
	//Cassandra by using Mapper classes
	TimelineService tService = new TimelineService();
        ObjectMapper mapper = new ObjectMapper();	//For JSON/Java conversion
	String jsonResponseString;			//Holds JSON response
        
        //CREATING A NEW TIMELINE POST
	@POST 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/new")
        public Response createTimelinePost(Timeline newTimelinePost, 
                @HeaderParam("Content-Type") String contentTypeValue,
                @HeaderParam("User-Id") UUID user_uuid){
            
            NewTimelinePostResponse successResponse;
            ResponseWithMessage failResponse;
            Timeline newPost = tService.createTimelinePost(newTimelinePost, user_uuid);
            
            if(newPost != null){
                try 
                    {
                        successResponse = new NewTimelinePostResponse("true", newPost);
                        jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);
			
                        return Response.status(Response.Status.CREATED)
                                    .entity(jsonResponseString)
                                    .header("Content-Type", contentTypeValue)
                                    .build();
                    } 
                    catch (JsonProcessingException e) 
                    {
                            System.out.println(e.getMessage());
                            failResponse = new ResponseWithMessage("false", "There has been a problem sending a succesful reponse");
			
                            try {
                                jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);
                                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(jsonResponseString)
					.header("Content-Type", contentTypeValue)
					.build();
                            } catch (JsonProcessingException e1) {
                                	System.out.println(e1.getMessage());
                            }
                    }
            }
            else{
                 failResponse = new ResponseWithMessage("false", "There has been a problem creating the new timeline post");
			
                try {
                     jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);
                     return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(jsonResponseString)
				.header("Content-Type", contentTypeValue)
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
            }
            
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("Something went wrong")
                       .header("Content-Type", "text/plain")
                       .build(); 
        }
        
        //RETRIEVING TIMELINE POSTS FOR ONE PARTICULAR USER. USED ON USER PROFILE PAGE (FIRST 20 OR LESS POSTS)
        @GET
	@Path("/getStarterProfileTimeline/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInitialProfileTimeline(@PathParam("user_uuid") UUID user_uuid){
            
            TimelineResponse successResponse;
            ResponseWithMessage failResponse;
            Result<Timeline> results = tService.getUserTimelinePosts(user_uuid);
            List<Timeline> timelineList = results.all();    //Convert to list of timeline objects. (Posts)
            
            if(timelineList.isEmpty() != true){
                
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    successResponse = new TimelineResponse("true", timelineList, 1, timelineList.size());
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
                
            }
            else{
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    failResponse = new ResponseWithMessage("false", "This user has not posted to the timeline yet");
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
            }
           
           
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
        }
        
        //MAY HAVE TO CHANGE DB STRUCTURE SLIGHTLY FOR THIS CODE TO WORK, OTHERWISE ITS FINE
        //RETRIEVING TIMELINE POSTS FOR USER AND ALL THEIR CONNECTIONS, USED ON DASHBOARD PAGE (FIRST 20 OR LESS POSTS)
        @GET
	@Path("/getStarterDashboardTimeline/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInitialDashboardTimeline(@PathParam("user_uuid") UUID user_uuid){
            
            TimelineResponse successResponse;
            ResponseWithMessage failResponse;
            Result<Timeline> results = tService.getMultipleUserTimelinePosts(user_uuid);
            List<Timeline> timelineList = results.all();    //Convert to list of timeline objects. (Posts)
            
            if(timelineList.isEmpty() == false){
                
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    successResponse = new TimelineResponse("true", timelineList, 1, timelineList.size());
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
                
            }
            else{
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    failResponse = new ResponseWithMessage("false", "This user has no posts to see :(");
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
            }
           
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
        }
        
        
        //RETRIEVING NEXT 20 TIMELINE POSTS ON USER PROFILE PAGE, PAGINATION
        @POST
        @Path("/getNextProfileTimeline")
        public Response getSectionProfileTimeline(TimelinePagination tp){
            
            TimelineResponse successResponse;
            ResponseWithMessage failResponse;
            List<Timeline> timelineList = tService.getSectionUserTimelinePosts(tp);
            
            if(timelineList.isEmpty() == false){
                
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    successResponse = new TimelineResponse("true", timelineList, tp.getCurrentTimelineEnd(), tp.getCurrentTimelineEnd() + 20);
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
                
            }
            else{
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    failResponse = new ResponseWithMessage("false", "This user has no posts to see :(");
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
            }
           
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
        }
        
        //RETRIEVING NEXT 20 TIMELINE POSTS ON USER DASHBOARD PAGE, PAGINATION
        //NOPOINT DOING THIS PARTICULAR ENDPOINT UNTIL MINOR DATABASE ISSUE IS RESOLVED BECAUSE CANT BE TESTED PROPERLY.
        
        //SETTING LIKE FOR A POST
        @POST
	@Path("/newLike")
	@Produces(MediaType.APPLICATION_JSON)
        public Response addALike(@HeaderParam("Post-Id") UUID post_uuid){
            
            LikeResponse successResponse;
            ResponseWithMessage failResponse;
            int result = tService.addPostLike(post_uuid);
            
            if(result != -1){
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    successResponse = new LikeResponse("true", result);
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
            }
            else{
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    failResponse = new ResponseWithMessage("false", "We had a problem adding a like to this post - the post may no longer exist");
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                } 
            }
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
        }
        
        //SETTING UNLIKE FOR A POST
        @POST
	@Path("/newUnlike")
	@Produces(MediaType.APPLICATION_JSON)
        public Response addAnUnlike(@HeaderParam("Post-Id") UUID post_uuid){
            
            LikeResponse successResponse;
            ResponseWithMessage failResponse;
            int result = tService.addPostUnlike(post_uuid);
            
            if(result != -1){
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    successResponse = new LikeResponse("true", result);
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                }
            }
            else{
                try {
                    
                    //Object with success of true, the timeline list and the start/end records retrieved from the timeline
                    failResponse = new ResponseWithMessage("false", "We had a problem adding an unlike to this post - the post may no longer exist");
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(failResponse);//object to json convert
                    
                    return Response.status(Response.Status.OK)
                                .entity(jsonResponseString)
				.header("Content-Type", "application/json")
				.build();
                } 
                catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                } 
            }
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Something went wrong")
                                .header("Content-Type", "text/plain")
                                .build(); 
        }
        
        //UPDATING A TIMELINE POST
        @PUT
	@Path("/update/{user_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response updateUser(Timeline updatePost, @PathParam("post_uuid") UUID post_id, 
                @HeaderParam("Content-Type") String contentTypeValue){
            
            ResponseWithMessage rwm;
            IdResponse ir;
            Timeline foundPost = tService.getTimelinePost(post_id);
            
            if(foundPost != null){
                //This data needs to be maintained, we dont want it being overriden during an update
                UUID post_uuid = post_id;
                UUID user_uuid = foundPost.getUser_uuid();
                UUID album_uuid = foundPost.getAlbum_uuid();
                UUID parent_uuid = foundPost.getParrent_uuid();
                Date created = foundPost.getCreated();
                int likes = foundPost.getLike_count();
                int unlikes = foundPost.getUnlike_count();
                //Now we can make the old post equal to the new post
                foundPost = updatePost;
                //The pass back in the values we want to make sure are maintained between updates.
                foundPost.setAlbum_uuid(album_uuid);
                foundPost.setLike_count(likes);
                foundPost.setUnlike_count(unlikes);
                foundPost.setPost_uuid(post_uuid);
                foundPost.setUser_uuid(user_uuid);
                foundPost.setCreated(created);
                foundPost.setParrent_uuid(parent_uuid);
                //Lastly the timeline was updated so...
                foundPost.setUpdated(new Date());
                
                boolean updated = tService.updateTimeline(foundPost);//Update the post and get back the result.
                
                if(updated){
                            ir = new IdResponse("true", post_uuid);
                            
                            try {
                                jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ir);
                                return Response.status(Status.OK)
						.entity(jsonResponseString)
						.header("Content-Type", contentTypeValue)
						.build();
                                
                            } catch (JsonProcessingException ex1) {
                                System.out.println(ex1.getMessage());
                            }
                    }else{//we found the user but something went wrong during the update
                        
                            rwm = new ResponseWithMessage("false", "Sorry something went wrong during the post update");
                        
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
            else{
                rwm = new ResponseWithMessage("false", "We could not find this post");
                    
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
            
            return null;
        }
        
        //DELETING A TIMELINE POST
        @DELETE
	@Path("/delete/{post_uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("post_uuid") UUID post_uuid){
            
            IdResponse ir;
            boolean deleted = tService.deleteTimelinePost(post_uuid);
                
            if(deleted){
                ir = new IdResponse("true", post_uuid);
                        
                try {
                    jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ir);
                    return Response.status(Status.OK)
                                    .entity(jsonResponseString)
                                    .header("Content-Type", "application/json")
                                    .build();
                            
                }catch (JsonProcessingException ex1) {
                            System.out.println(ex1.getMessage());
                }
            }
            else
            {
                ir = new IdResponse("false", post_uuid);
                 
                try {
                         jsonResponseString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ir);
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
}
