package ie.gmit.socializer.SocializerAPI.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;

public class CassandraConnector {
    
    public static Cluster initalizeConnection(String columnFamily){
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions.setCoreConnectionsPerHost(HostDistance.REMOTE,  4)
                        .setMaxConnectionsPerHost( HostDistance.REMOTE, 10);

        Cluster cluster = Cluster.builder()
                            .addContactPoint("127.0.0.1")//need server ip here
                            .withPoolingOptions(poolingOptions)
                            .build();
        
        cluster.connect(columnFamily);
        return cluster;
    } 
    
    public static void closeConnection(Cluster currentCluster, Session currentSession){
        
    	if(currentCluster != null){
    		currentCluster.close();
    	}
    	
    	if(currentSession != null){
    		currentSession.close();
    	}
    } 
}
