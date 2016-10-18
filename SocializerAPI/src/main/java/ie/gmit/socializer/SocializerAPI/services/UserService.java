package ie.gmit.socializer.SocializerAPI.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.core.Session;
//import static com.datastax.driver.mapping.Mapper.Option.*;
import com.datastax.driver.core.utils.UUIDs;

import java.util.Date;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.User;

public class UserService {
	
	private Session session;
	private Cluster cluster;
	private MappingManager manager;
	private Mapper<User> mapper;
	private Date date = new Date();
	
	public UserService(){
		//PROBLEM HERE WITH CONNECTION TO CASSANDRA - HOPEFULLY RESOLVE SOON
		cluster = CassandraConnector.initalizeConnection("app_user_data");
		session = cluster.newSession().init();
		manager = new MappingManager(session);
		mapper = manager.mapper(User.class);
	}

	//Could also implement method to get multiple users....maybe if search is being implemented?
	
	public UUIDs createUser(User newUser){
		
		newUser.setCreated(date.getTime());
		mapper.save(newUser);
		newUser = mapper.get(newUser.getEmail());
		
		return newUser.getUuid();
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
