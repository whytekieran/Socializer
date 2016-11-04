package ie.gmit.socializer.SocializerAPI.models;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

import ie.gmit.socializer.SocializerAPI.utilities.Mappable;

//Timeline Mapper class for handling timeline posts by Ciaran Whyte while using Peters work as a reference,
//the Mappable interface was written by Peter Nagy. 
//Mapper interface declares all methods associated with a Mapper. Can then use the 
//interface and re-define the methods for our own implementation.
public class TimelineMapper implements Mappable<Timeline> {

    private Mapper<Timeline> mapper;
    private final Session session;
    private final MappingManager mappingManager;
    private final String KEY_SPACE;
	
    public TimelineMapper(Session session, final String keySpace){
		
	this.session = session;
        this.mappingManager = new MappingManager(session);
        KEY_SPACE = keySpace;
        initializeTimelineMapper();
    }
	
    private void initializeTimelineMapper() {
        mapper = mappingManager.mapper(Timeline.class);
    }
	
    public Session getCurrentSession(){
		return session;
    }
	
    @Override
    public boolean createEntry(Timeline model) {
	try {
            mapper.save(model);
            return true;
        } catch (Exception e) {
            //If there is a problem use the java.util logger to keep a log of the issue. - No connection
            Logger.getLogger(TimelineMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - timeline", e.getMessage());
        }

        return false;	//If we reach this point of the method return false
    }

    @Override
    //Asynchronous...run in the background and allow other processes to continue
    public void createEntryAsync(Timeline model) {
	mapper.saveAsync(model);
    }

    @Override
    //Method that uses a ready prepared statement then binds a variable to it. This is returned
    //as a BoundStatement. In this case the BoundStatement is for deleting timeline posts.//allow filtering
    public BoundStatement getDeleteBoundStatement(UUID entryUUID) {
            PreparedStatement prepared = session.prepare(
            String.format("delete from %s.timeline where post_uuid = ?", KEY_SPACE)
        );
		
            return prepared.bind(entryUUID);//Here we bind the statement to the variable then return it
    }

    @Override
    //Synchronous
    public boolean deleteEntry(UUID entryUUID) {
	try {
            session.execute(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(TimelineMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - timeline", e.getMessage());
        }
		
            return false;
    }

    @Override
    //Same as method above except we execute asynchronously not synchronously like above.
    public boolean deleteEntryAsync(UUID entryUUID) {
	try {
            //Retrieve the created statement (bound statement) and execute it asynchronously.
            session.executeAsync(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(TimelineMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - timeline", e.getMessage());
        }
		
	return false;
    }

    @Override
    //delete multiple posts - if needed
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
	//Prepare statement with string formatter 
	PreparedStatement prepared = session.prepare(
		String.format("delete from %s.timeline where %s in ?", KEY_SPACE, keyColumnName)
	);
	//Bind variable (?) to parameter using .bind()
	BoundStatement bound = prepared.bind(entryUUIDs);

	try {
            session.execute(bound);			//Execute the statement and if successful return true
            return true;
	} catch (Exception e) {
            //Otherwise log the error....
            Logger.getLogger(TimelineMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete multiple - timeline", e.getMessage());
	}

	return false;		//....and return false
    }

    @Override
    //Simply executes a query (string)
    public boolean executeQuery(String query) {
        try {
            session.execute(query);//execute the query and if unsuccessful log the error
            return true;
        } catch (Exception e) {
            Logger.getLogger(TimelineMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra query - user", e.getMessage());
        }

	return false;
    }

    @Override
    public void updateEntry(Timeline model) {
	//If uuid or any other index matches the mapper will simply do the update for us.
	mapper.saveAsync(model);
    }

    @Override
    //Get a particular timeline entry
    public Timeline getEntry(UUID entryUUID) {
	PreparedStatement prepared = session.prepare(
        String.format("select * from %s.timeline where post_uuid = ?", KEY_SPACE)
        );
        
	BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);

        //gets the next one...but all uuids for posts are unique hence there will only be one post here 
        //to return because query above is based on post_uuid
        return mapper.map(results).one();
    }

    @Override
    //Used if we want to retrieve multiple timeline posts. Accepts a pre-made bound statement
    public Result<Timeline> getMultiple(BoundStatement bound) {
	ResultSet results = session.execute(bound);//Execute the statement and get the result set
	return mapper.map(results);//Will fetch Timeline entities from result set using their PRIMARY KEY.
    }
        
    //public Result<Timeline> getByUserUUID(List<UUID> userUUIDS){
    //}

}
