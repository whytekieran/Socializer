package ie.gmit.socializer.SocializerAPI.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.utils.UUIDs;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.AddUserConnection;
import ie.gmit.socializer.SocializerAPI.models.User;
import ie.gmit.socializer.SocializerAPI.models.UserLogin;
import ie.gmit.socializer.SocializerAPI.models.UserMapper;
import ie.gmit.socializer.SocializerAPI.utilities.HashingUtil;
import java.util.List;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class UserService {
	
	private final Cluster cluster;
	private UserMapper userMapper;
	private final String KEY_SPACE = "app_user_data";
	
	//Constructor, connects to cassandra cluster then initializes the UserMapper object
	public UserService(){
            cluster = CassandraConnector.initalizeConnection("app_user_data");//connect to cluster
            initializeUserMapper(cluster);//use cluster to initialize session and user mapper object
	}
	
	//Creates an instance of UserMapper
	private void initializeUserMapper(Cluster cluster){
            userMapper = new UserMapper(cluster.newSession(), KEY_SPACE);
	}
	
	//Turns user the password into a hashed password
	public String hashPassword(String password){
            char[] passArrayChar = password.toCharArray();
            byte[] passArrayByte = HashingUtil.createPassword(passArrayChar);
            String hashedPassword = Hex.encodeHexString(passArrayByte);
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
		
            char[] charPass = user.getPassword().toCharArray();
            User foundUser = userMapper.getUserBasedOnEmail(user.getEmail());
		
            if(foundUser != null){
		
		byte[] bytePassHash = null;
		try {
			bytePassHash = Hex.decodeHex(foundUser.getPassword_hash().toCharArray());
		} catch (DecoderException e) {
                    System.out.println(e.getMessage());
		}
	
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
        
        //Connect two users together (friends) their user ids are passe in here
        public boolean connectUsers(AddUserConnection connectionIds){
            
            User currentUser = checkIfUserExists(UUID.fromString(connectionIds.getCurrentUserId()));
            User newFriend = checkIfUserExists(UUID.fromString(connectionIds.getRequestedFriendId()));
            if(currentUser != null && newFriend != null){
                
                List<UUID> currentUsersFriendList = currentUser.getConnections();   //get current users current friends
                currentUsersFriendList.add(newFriend.getUser_uuid());               //add the new friends uuid to the currents users friend list
                currentUser.setConnections(currentUsersFriendList);                 //Update the user object
                boolean result = updateUser(currentUser);                           //Save the updated user
                return result;                                          
            }
            else{
                return false;
            }
        }
        
        //Disconnects a user from another user (No longer friends) 
        public boolean disconnectUsers(AddUserConnection connectionIds){
            
            User currentUser = checkIfUserExists(UUID.fromString(connectionIds.getCurrentUserId()));
            User exFriend = checkIfUserExists(UUID.fromString(connectionIds.getRequestedFriendId()));
            
            if(currentUser != null & exFriend != null){
                
                List<UUID> currentUsersFriendList = currentUser.getConnections();   //get current users current friends
                currentUsersFriendList.remove(exFriend.getUser_uuid());
                currentUser.setConnections(currentUsersFriendList); 
                boolean result = updateUser(currentUser);                           //Save the updated user
                return result;                             
            }
            else{
                return false;
            }
        }
        
        public boolean updateUserAttribute(UUID userId, String userAttribute, User user){
            
            User currentUser = checkIfUserExists(userId);//Get user we are updating
            
            if(currentUser != null){//if its not null
                boolean updateResult = false;
                
                //Update will depend on the user attibute passed in the header
                //I feel the only single attibutes that should be edited here are email or password. All others are edited in the other update
                //endpoint defined for users.
                switch(userAttribute){//switch on the header param which indicates attribute being edited
                    case "email":
                        
                        String oldEmail = currentUser.getEmail();//Get current and soon to be old email
                        
                        if(!oldEmail.equals(user.getEmail())){//If the old and new email are not the same
                            currentUser.setEmail(user.getEmail());//Set the users new email
                            userMapper.deleteUserBasedOnEmailAndId(oldEmail, userId);//Delete the user associated with the old email
                            updateResult = updateUser(currentUser);//Update the new user with updated email
                        }
                        else{
                            updateResult = false;            //no point in doing an update if the emails are the same
                        }
                        break;
                    case "password_hash"://No need to worry about dublicate user_uuids when changing password so dont have to delete old user
                        String hashedPassword = hashPassword(user.getPassword_hash());//get the new hashed password
                        currentUser.setPassword_hash(hashedPassword);//set it to the user for updating
                        updateResult = updateUser(currentUser); //update that user
                        break;
                } 
          
                return updateResult;
            }
            else{
                return false;     
            }
        }
        
        //Get a specific user, for checking purposes
	public User checkIfUserExists(UUID uuid){
            User foundUser = userMapper.getEntry(uuid);
		
            return foundUser;
	}
	
	//Get a specific user to return to client
	public User getUser(UUID uuid){
            User foundUser = userMapper.getEntry(uuid);
            foundUser.setPassword_hash(null);
		
            return foundUser;
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
		userMapper.deleteEntry(uuid);
		return true;
            }catch(Exception e){
		Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - user", e.getMessage());
            }
		
            return false;
	}
}
