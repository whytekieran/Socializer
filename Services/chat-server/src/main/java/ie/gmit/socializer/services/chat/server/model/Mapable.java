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

import java.util.List;
import java.util.UUID;

/**
 *
 * @author pete
 */
public interface Mapable {

    /**
     * Create single entry in database
     * @param modelable
     */
    public void createEntry(Modelable modelable);

    /**
     * Create single entry in database async
     * @param modelable
     */
    public void createEntryAsync(Modelable modelable);

    /**
     * Delete entry by uuid
     * @param entryUUID
     */
    public void deleteEntry(UUID entryUUID);
    
    /**
     * Delete entry by uuid
     * @param entryUUID
     */
    public void deleteEntryAsync(UUID entryUUID);

    /**
     * Execute single query
     * @param query
     */
    public void executeQuery(String query);

    /**
     * Update single entry
     * @param modelable
     */
    public void updateEntry(Modelable modelable);
    
    /**
     * Get single entry
     * @param entryUUID
     */
    public Modelable getEntry(UUID entryUUID);
    
    public void deleteEntries(List<UUID> entryUUIDs, String keyColumnName);
}
