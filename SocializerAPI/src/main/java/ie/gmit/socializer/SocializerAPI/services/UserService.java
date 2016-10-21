package ie.gmit.socializer.SocializerAPI.services;

import com.datastax.driver.core.Cluster;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.User;
import ie.gmit.socializer.SocializerAPI.models.UserMapper;

public class UserService {
	
	private Cluster cluster;
	private UserMapper userMapper;
	static final String KEY_SPACE = "app_user_data";
	
	public UserService(){
		cluster = CassandraConnector.initalizeConnection("app_user_data");//connect to cluster
		initializeUserMapper(cluster);//use cluster to initialize session and user mapper object
	}
	
	private final void initializeUserMapper(Cluster cluster){
		userMapper = new UserMapper(cluster.newSession(), KEY_SPACE);
	}

	//Add a new user
	public UUID createUser(User newUser){
		UUID userId = newUser.getUuid();
		userMapper.createEntryAsync(newUser);
		return userId;
	}
	
	//Get a specific user
	public User getUser(UUID uuid){
		return userMapper.getEntry(uuid);
	}
	
	//Update user
	public boolean updateUser(User user){
		try{
			userMapper.updateEntry(user);
			return true;
		}catch(Exception e){
			Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Could not execute cassandra update - user", e.getMessage());
		}
		
		return false;
	}
	
	//Delete user
	public boolean deleteUser(UUID uuid){
		try{
			userMapper.deleteEntryAsync(uuid);
			return true;
		}catch(Exception e){
			Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - user", e.getMessage());
		}
		
		return false;
	}
}
