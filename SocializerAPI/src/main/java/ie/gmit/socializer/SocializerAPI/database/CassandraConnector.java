package ie.gmit.socializer.SocializerAPI.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;

//Cassandra connector class written by Peter Nagy and included into the code by Ciaran Whyte
public class CassandraConnector {
    
    public static Cluster initalizeConnection(String columnFamily){
    	
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL,  4)
                        .setMaxConnectionsPerHost(HostDistance.LOCAL, 10);

        Cluster cluster = Cluster.builder()
                            .addContactPoint("127.0.0.1")
                            .withPort(9042)
                            .withPoolingOptions(poolingOptions)
                            .build();
        
        
        cluster.connect(columnFamily);
        return cluster;
    } 
}
