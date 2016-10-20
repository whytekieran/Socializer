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
 * @description NewInterface - Interface for classes that handle database (Cassandra) ORM
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.server.model;

import com.datastax.driver.core.BoundStatement;
import java.util.List;
import java.util.UUID;

public interface Mapable{

    /**
     * Create single entry in database
     * @param modelable
     */
    public boolean createEntry(Modelable modelable);

    /**
     * Create single entry in database async
     * @param modelable
     */
    public void createEntryAsync(Modelable modelable);

    /**
     * Generate bound statement for delete
     * @param entryUUID
     * @return 
     */
    public BoundStatement getDeleteBoundStatement(UUID entryUUID);
    /**
     * Delete entry by uuid
     * @param entryUUID
     */
    public boolean deleteEntry(UUID entryUUID);
    
    /**
     * Delete entry by uuid
     * @param entryUUID
     */
    public boolean deleteEntryAsync(UUID entryUUID);
    
    /**
     * Delete multiple entries
     * @param entryUUIDs
     * @param keyColumnName 
     */
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName);

    /**
     * Execute single query
     * @param query
     */
    public boolean executeQuery(String query);

    /**
     * Update single entry
     * 
     * Note: Method could throw an exception for invalid update or closed connection
     * @param modelable
     */
    public void updateEntry(Modelable modelable);
    
    /**
     * Get single entry
     * @param entryUUID
     * @return Modelable or null
     */
    public Modelable getEntry(UUID entryUUID);
    
    
}
