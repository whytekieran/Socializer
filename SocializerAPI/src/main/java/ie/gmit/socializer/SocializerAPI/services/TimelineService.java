package ie.gmit.socializer.SocializerAPI.services;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Result;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.Timeline;
import ie.gmit.socializer.SocializerAPI.models.TimelineMapper;
import ie.gmit.socializer.SocializerAPI.models.TimelinePagination;
import ie.gmit.socializer.SocializerAPI.models.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimelineService {
	
	private final Cluster cluster;
	private TimelineMapper timelineMapper;
	static final String KEY_SPACE = "app_user_data";
        private final UserService uService = new UserService();
	
	public TimelineService(){
            cluster = CassandraConnector.initalizeConnection("app_user_data");//connect to cluster
            initializeTimelineMapper(cluster);//use cluster to initialize session and timeline mapper object
	}
	
	private void initializeTimelineMapper(Cluster cluster){
            timelineMapper = new TimelineMapper(cluster.newSession(), KEY_SPACE);
	}
	
	//Add a new timeline post
	public Timeline createTimelinePost(Timeline newTimeline, UUID user_uuid){
            newTimeline.setUser_uuid(user_uuid);
            newTimeline.setPost_uuid(UUIDs.random());//Give the post an id
            newTimeline.setLike_count(0);
            newTimeline.setUnlike_count(0);
            newTimeline.setCreated(new Date());
            newTimeline.setUpdated(new Date());
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
	
	//Get a specific timeline post if needed
	public Timeline getTimelinePost(UUID post_uuid){
            return timelineMapper.getEntry(post_uuid);
	}
	
	//Get first 20 timeline posts related to the same user...used on user profile timeline
	public Result<Timeline> getUserTimelinePosts(UUID user_uuid){
            Session session = timelineMapper.getCurrentSession();
		
            PreparedStatement prepared = session.prepare(
            String.format("select * from %s.timeline where user_uuid = ? Limit 20", KEY_SPACE)
            );
		
            BoundStatement bound = prepared.bind(user_uuid);
            Result<Timeline> results = timelineMapper.getMultiple(bound);
		
            return results;
	}
        
        //Gets next 20 posts for a particular user profile timeline when the user scrolls to the bottom of the page
        public List<Timeline> getSectionUserTimelinePosts(TimelinePagination tp){
            Session session = timelineMapper.getCurrentSession();
		
            PreparedStatement prepared = session.prepare(
            String.format("select * from %s.timeline where user_uuid = ?", KEY_SPACE)
            );
		
            BoundStatement bound = prepared.bind(UUID.fromString(tp.getUser_uuid()));
            Result<Timeline> results = timelineMapper.getMultiple(bound);
            
            List<Timeline> returnedPosts = new ArrayList<>();
            List<Timeline> posts = results.all();
            
            //Only 20 loops maximum....fast
            for(int i = tp.getCurrentTimelineEnd() + 1; i <= tp.getCurrentTimelineEnd() + 20; ++i){
                
                if(i >= posts.size()){
                    break;
                }
                else{
                    returnedPosts.add(posts.get(i));
                }
            }
                
            return returnedPosts;
        }
        
        //THIS CODE WOULD WORK IF USER_UUID WAS A PK OF THE TIMELINE TABLE....DB STRUCTURE MAY HAVE TO BE CHANGED
        //Get first 20 timeline posts related to the user and all the users connections...used on dashboard timeline
	public Result<Timeline> getMultipleUserTimelinePosts(UUID user_uuid){
            Session session = timelineMapper.getCurrentSession();
            User currentUser = uService.checkIfUserExists(user_uuid);
            List<UUID> currentUserFriendList = currentUser.getConnections();
            currentUserFriendList.add(user_uuid);//Add his/herself to the list because trying to get first 20 posts including current user
		
            //PreparedStatement prepared = session.prepare(
            //String.format("select * from %s.timeline where user_uuid in ? Limit 20 Allow Filtering", KEY_SPACE)
            //);
        
            String userIds = currentUserFriendList.toString();
            userIds = userIds.substring(1, userIds.length()-1);
            //String tester = "7c3764d8-84da-4587-87f8-1e638edc8e63";
               
            //System.out.println("IDS  "+userIds);
            String query = "select * from app_user_data.timeline where user_uuid in ("+userIds+") Limit 20 Allow Filtering";
                
            //BoundStatement bound = prepared.bind(currentUserFriendList.toArray());
            //Result<Timeline> results = timelineMapper.getMultiple(bound);
            ResultSet results = session.execute(query);
            Result<Timeline> tl = timelineMapper.getStarterDashboard(results);
                    
            return tl;
	}
        
        //Add additional like to a post
        public int addPostLike(UUID post_uuid){
            
            Timeline foundPost = timelineMapper.getEntry(post_uuid);
            
            if(foundPost != null){
                
                int currentLikeCount = foundPost.getLike_count();
                ++currentLikeCount;
                foundPost.setLike_count(currentLikeCount);
                timelineMapper.updateEntry(foundPost);
                return currentLikeCount;
            }
            else{
                return -1;
            }
        }
        
        //Add additional like to a post
        public int addPostUnlike(UUID post_uuid){
            
            Timeline foundPost = timelineMapper.getEntry(post_uuid);
            
            if(foundPost != null){
                
                int currentUnlikeCount = foundPost.getUnlike_count();
                ++currentUnlikeCount;
                foundPost.setUnlike_count(currentUnlikeCount);
                timelineMapper.updateEntry(foundPost);
                return currentUnlikeCount;
            }
            else{
                return -1;
            }
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
