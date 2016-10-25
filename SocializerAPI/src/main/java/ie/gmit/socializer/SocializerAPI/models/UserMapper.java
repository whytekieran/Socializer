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

//User Mapper class for handling Users by Ciaran Whyte while using Peters work as a reference, the 
//Mappable interface was written by Peter Nagy.
//Mapper interface declares all methods associated with a Mapper. Can then use the interface and re-define
//the methods for our own implementation.
public class UserMapper implements Mappable<User> {

	private Mapper<User> mapper;
    private Session session;
    private MappingManager mappingManager;
    private final String KEY_SPACE;
	
    //Constructor accepts the current session and the key space (column family) and the Cassandra database.
	public UserMapper(Session session, final String keySpace){
		
		this.session = session;
        this.mappingManager = new MappingManager(session);
        KEY_SPACE = keySpace;
        initializeUserMapper();
	}
	
	private final void initializeUserMapper() {
        mapper = mappingManager.mapper(User.class);
	}
	
	@Override
	public boolean createEntry(User model) {
		try {
            mapper.save(model);
            return true;
        } catch (Exception e) {
        	//If there is a problem use the java.util logger to keep a log of the issue. - No connection
            Logger.getLogger(UserMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - user", e.getMessage());
            System.out.println(e);
        }

		return false;	//If we reach this point of the method return false
	}

	@Override
	//Asynchronous...run in the background and allow other processes to continue
	public void createEntryAsync(User model) {
		 mapper.saveAsync(model);
	}

	@Override
	//Method that uses a ready prepared statement then binds a variable to it. This is returned
	//as a BoundStatement. In this case the BoundStatement is for deleting users.
	public BoundStatement getDeleteBoundStatement(UUID entryUUID) {
		PreparedStatement prepared = session.prepare(
                String.format("delete from %s.user where user_uuid = ?", KEY_SPACE)
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
            Logger.getLogger(UserMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - message", e.getMessage());
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
            Logger.getLogger(UserMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - message", e.getMessage());
        }
		
		return false;
	}

	@Override
	public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
		//Prepare statement with string formatter 
		PreparedStatement prepared = session.prepare(
                String.format("delete from %s.user where %s in ?", KEY_SPACE, keyColumnName)
        );
		//Bind variable (?) to parameter using .bind()
        BoundStatement bound = prepared.bind(entryUUIDs);

        try {
            session.execute(bound);			//Execute the statement and if successful return true
            return true;
        } catch (Exception e) {
        	//Otherwise log the error....
            Logger.getLogger(UserMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete multiple - message", e.getMessage());
        }

        return false;		//....and return false
	}

	@Override
	//Simply executes a query (string)
	public boolean executeQuery(String query) {
		try {
            session.execute(query);
            return true;
        } catch (Exception e) {
            Logger.getLogger(UserMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra query - message", e.getMessage());
        }

		return false;
	}

	@Override
	public void updateEntry(User model) {
		//If uuid or any other index matches the mapper will simply do the update for us.
		mapper.saveAsync(model);
	}
	
	public User getUserBasedOnEmail(String email){
		
		PreparedStatement prepared = session.prepare(
                String.format("select * from %s.user where email=?", KEY_SPACE)
        );
        
		BoundStatement bound = prepared.bind(email);
        ResultSet results = session.execute(bound);

        //gets the next one...but all emails for users are unique hence there will only be one user 
        //here to return because query above is based on email
        return mapper.map(results).one();
	}

	@Override
	//Get a particular user
	public User getEntry(UUID entryUUID) {
		PreparedStatement prepared = session.prepare(
                String.format("select * from %s.user where user_uuid = ?", KEY_SPACE)
        );
        
		BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);

        //gets the next one...but all uuids for users are unique hence there will only be one user here 
        //to return because query above is based on user_uuid
        return mapper.map(results).one();
	}

	@Override
	//Used if we want to retrieve multiple users. Accepts a pre-made bound statement
	public Result<User> getMultiple(BoundStatement bound) {
		ResultSet results = session.execute(bound);//Execute the statement and get the result set
		return mapper.map(results);//Will fetch User entities from result set using their PRIMARY KEY.
	}
}
