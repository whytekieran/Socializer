package ie.gmit.socializer.SocializerAPI.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.User;

public class UserService {
	
	private Session session;
	private Cluster cluster;
	
	public UserService(){
		cluster = CassandraConnector.initalizeConnection("app_user_data");
		session = cluster.newSession();
		session.init();
	}

	//Skeleton class giving general idea of the user service.
	//Could also implement method to get multiple users....maybe if search is being implemented?
	
	public User createUser(User newUser){
		//do stuff, interact with data class in the data package
		return null;
	}
	
	public User getUser(String uuid){
		//do stuff, interact with data class in the data package
		return null;
	}
	
	public User updateUser(String uuid){
		//do stuff, interact with data class in the data package
		return null;
	}
	
	public void deleteUser(String uuid){
		//do stuff, interact with data class in the data package
	}
}
