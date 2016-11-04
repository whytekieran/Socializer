package ie.gmit.socializer.SocializerAPI.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.gmit.socializer.SocializerAPI.models.Timeline;
import ie.gmit.socializer.SocializerAPI.responseObjects.NewTimelinePostResponse;
import ie.gmit.socializer.SocializerAPI.responseObjects.ResponseWithMessage;
import javax.ws.rs.Path;

import ie.gmit.socializer.SocializerAPI.services.TimelineService;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
                @HeaderParam("Content-Type") String contentTypeValue){
            
            NewTimelinePostResponse successResponse;
            ResponseWithMessage failResponse;
            Timeline newPost = tService.createTimelinePost(newTimelinePost);
            
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
                 } catch (JsonProcessingException e1) {
                     System.out.println(e1.getMessage());
                 }
            }
            
            //Default if everything goes wrong
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("Something went wrong")
                       .header("Content-Type", "text/plain")
                       .build(); 
        }
        
        //RETRIEVING TIMELINE POSTS FOR ONE PARTICULAR USER. USED ON USER PROFILE PAGE
        
        //RETRIEVING TIMELINE POSTS FOR USER AND ALL THEIR CONNECTIONS, USED ON DASHBOARD PAGE
        
        //RETRIEVING NEXT 20 TIMELINE POSTS ON USER PROFILE PAGE, PAGINATION
        
        //RETRIEVING NEXT 20 TIMELINE POSTS ON USER DASHBOARD PAGE, PAGINATION
}
