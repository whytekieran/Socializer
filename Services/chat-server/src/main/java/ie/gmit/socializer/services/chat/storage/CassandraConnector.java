/**
 * Copyright (C) 2016 Peter Nagy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ======================================================================
 *
 * @author Peter Nagy - peternagy.ie
 * @since October 2016
 * @version 0.1
 * @description CassandraConnector - Handles cassandra connection based on Datastax driver
 * @package ie.gmit.socializer.services.chat.server.storage
 */
package ie.gmit.socializer.services.chat.storage;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;

public class CassandraConnector {
    
    /**
     * Initialize the cluster with default pooling
     * 
     * @param columnFamily - the column family to connect to
     * @return initialized cluster
     * @todo: use config file for connection points
     */
    public static Cluster initalizeConnection(String columnFamily){
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL,  4)
                        .setMaxConnectionsPerHost( HostDistance.LOCAL, 10);

        Cluster cluster = Cluster.builder()
                            .addContactPoint("127.0.0.1")
                            .withPoolingOptions(poolingOptions)
                            .build();
        
        cluster.connect(columnFamily);
        
        return cluster;
    }
}
