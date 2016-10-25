package ie.gmit.socializer.SocializerAPI.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.utils.UUIDs;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.User;
import ie.gmit.socializer.SocializerAPI.models.UserLogin;
import ie.gmit.socializer.SocializerAPI.models.UserMapper;
import ie.gmit.socializer.SocializerAPI.utilities.HashingUtil;

public class UserService {
	
	private Cluster cluster;
	private UserMapper userMapper;
	private final String KEY_SPACE = "app_user_data";
	
	//Constructor, connects to cassandra cluster then initializes the UserMapper object
	public UserService(){
		cluster = CassandraConnector.initalizeConnection("app_user_data");//connect to cluster
		initializeUserMapper(cluster);//use cluster to initialize session and user mapper object
	}
	
	//Creates an instance of UserMapper
	private final void initializeUserMapper(Cluster cluster){
		userMapper = new UserMapper(cluster.newSession(), KEY_SPACE);
	}
	
	//Turns user the password into a hashed password
	public String hashPassword(String password){
		char[] passArrayChar = password.toCharArray();
		byte[] passArrayByte = HashingUtil.createPassword(passArrayChar);
		String hashedPassword = new String(passArrayByte);
		return hashedPassword;
	}

	//Add a new user accepts user object and hashed password
	public UUID createUser(User newUser, String hashedPass){
		
		//Set variables needed by the database. Also because new user can set updated and created.
		//Could also do some validation here if needed to ensure certain values have been given...eg email
		newUser.setSlug(newUser.getFirstname() + "." + newUser.getSurname());	//Set user short identifier
		newUser.setUser_uuid(UUIDs.random());									//Give user a UUID
		newUser.setPassword_hash(hashedPass);									//Set password (hashed)
		newUser.setCreated(new Date());					//New user so date created and updated are the same
		newUser.setUpdated(new Date());
		
		UUID userId = newUser.getUser_uuid();			//Get user UUID so we can return it to the client
		userMapper.createEntry(newUser);				//Create the new user
		
		return userId;									//return the new user id
	}
	
	//Validates a user when attempting to login
	public UUID validateUser(UserLogin user){
		
		User foundUser = userMapper.getUserBasedOnEmail(user.getEmail());
		
		if(foundUser != null){
			byte[] bytePassHash = foundUser.getPassword_hash().getBytes();
			char[] charPass = user.getPassword().toCharArray();
			boolean valid = HashingUtil.validatePassword(bytePassHash, charPass);
			
			if(valid){
				return foundUser.getUser_uuid();
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
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
