package ie.gmit.socializer.SocializerAPI.services;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Result;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.Timeline;
import ie.gmit.socializer.SocializerAPI.models.TimelineMapper;

public class TimelineService {
	
	private final Cluster cluster;
	private TimelineMapper timelineMapper;
	static final String KEY_SPACE = "app_user_data";
	
	public TimelineService(){
		cluster = CassandraConnector.initalizeConnection("app_user_data");//connect to cluster
		initializeTimelineMapper(cluster);//use cluster to initialize session and timeline mapper object
	}
	
	private void initializeTimelineMapper(Cluster cluster){
		timelineMapper = new TimelineMapper(cluster.newSession(), KEY_SPACE);
	}
	
	//Add a new timeline post
	public Timeline createTimelinePost(Timeline newTimeline){
		newTimeline.setPost_uuid(UUIDs.random());//Give the post an id
                UUID postId = newTimeline.getPost_uuid();//get the newly create post id
		boolean created = timelineMapper.createEntry(newTimeline);
                
                //if new post was created successfully
                if(created){
                    return newTimeline;//return the new post
                }
                else{
                   return null;//otherwise return null
                }
	}
	
	//Get a specific timeline post
	public Timeline getTimelinePost(UUID post_uuid){
		return timelineMapper.getEntry(post_uuid);
	}
	
	//Get multiple timeline posts related to the same user
	public Result<Timeline> getMultipleUserTimelinePosts(UUID user_uuid){
		Session session = timelineMapper.getCurrentSession();
		
		PreparedStatement prepared = session.prepare(
                String.format("select * from %s.timeline where user_uuid = ?", KEY_SPACE)
        );
		BoundStatement bound = prepared.bind(user_uuid);
		Result<Timeline> results = timelineMapper.getMultiple(bound);
		
		return results;
	}
	
	//Update timeline post
	public boolean updateTimeline(Timeline updatedTimeline){
		try{
			timelineMapper.updateEntry(updatedTimeline);
			return true;
		}catch(Exception e){
			Logger.getLogger(TimelineService.class.getName()).log(Level.SEVERE, "Could not execute cassandra update - time", e.getMessage());
		}
			
		return false;
	}
	
	//Delete a timeline post
	public boolean deleteTimelinePost(UUID post_uuid){
		try{
			timelineMapper.deleteEntryAsync(post_uuid);
			return true;
		}catch(Exception e){
			Logger.getLogger(TimelineService.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - timeline", e.getMessage());
		}
			
		return false;
	}
}
