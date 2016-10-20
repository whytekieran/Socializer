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
 * @description NewInterface - Short description
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.server.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.mapping.Result;
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
    void createEntry(Modelable modelable);

    /**
     * Create single entry in database async
     * @param modelable
     */
    void createEntryAsync(Modelable modelable);

    /**
     * Delete entry by uuid
     * @param entryUUID
     */
    void deleteEntry(UUID entryUUID);
    
    /**
     * Delete entry by uuid
     * @param entryUUID
     */
    void deleteEntryAsync(UUID entryUUID);

    /**
     * Execute single query
     * @param query
     */
    void executeQuery(String query);

    /**
     * Get multiple entries by executing a select statement
     *
     * @param query
     * @return
     */
    Result<MessageModel> getMultiple(BoundStatement bound);

    /**
     * Update single entry
     * @param modelable
     */
    void updateEntry(Modelable modelable);
    
    /**
     * Get single entry
     * @param entryUUID
     */
    Modelable getEntry(UUID entryUUID);
}
