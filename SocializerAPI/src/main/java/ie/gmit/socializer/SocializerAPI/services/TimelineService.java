package ie.gmit.socializer.SocializerAPI.services;

import java.util.Date;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import ie.gmit.socializer.SocializerAPI.database.CassandraConnector;
import ie.gmit.socializer.SocializerAPI.models.Timeline;

public class TimelineService {
	
	private Session session;
	private Cluster cluster;
	private MappingManager manager;
	private Mapper<Timeline> mapper;
	
	public TimelineService(){
		//PROBLEM HERE WITH CONNECTION TO CASSANDRA - HOPEFULLY RESOLVE SOON
		cluster = CassandraConnector.initalizeConnection("timeline data area cassandra goes here");
		session = cluster.newSession().init();
		manager = new MappingManager(session);
		mapper = manager.mapper(Timeline.class);
	}
}
