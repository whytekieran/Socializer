package ie.gmit.socializer.SocializerAPI.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.SocketOptions;

public class CassandraConnector {
    
    public static Cluster initalizeConnection(String columnFamily){
    	
    	//PROBLEM WITH CONNECTIONTING HERE STILL NOT SOLVED
    	SocketOptions socketOptions = new SocketOptions();  
    	socketOptions.setReadTimeoutMillis(10000);  
    	socketOptions.setConnectTimeoutMillis(10000);
    	
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions.setCoreConnectionsPerHost(HostDistance.REMOTE,  4)
                        .setMaxConnectionsPerHost(HostDistance.REMOTE, 10);

        Cluster cluster = Cluster.builder()
                            .addContactPoint("31.220.44.44")
                            .withPort(9042)
                            .withSocketOptions(socketOptions)
                            .withPoolingOptions(poolingOptions)
                            .withProtocolVersion(ProtocolVersion.V3)
                            .build();
        
        
        cluster.connect(columnFamily);
        return cluster;
    } 
}
