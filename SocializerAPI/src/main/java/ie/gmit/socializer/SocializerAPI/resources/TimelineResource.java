package ie.gmit.socializer.SocializerAPI.resources;

import javax.ws.rs.Path;

import ie.gmit.socializer.SocializerAPI.services.TimelineService;

@Path("/timeline")
public class TimelineResource {

	//Timeline service handles all communication (CRUD) for Timeline objects between resource URL's and 
	//Cassandra by using Mapper classes
	TimelineService tService = new TimelineService();
}
