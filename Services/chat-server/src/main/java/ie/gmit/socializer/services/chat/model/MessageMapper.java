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
 * @description MessageModelMapper - ORM class for message model -> Cassandra
 * Database
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageMapper implements Mappable<Message> {

    protected Mapper<Message> mapper;
    protected Session session;
    protected MappingManager mappingManager;
    protected final String KEY_SPACE;

    public MessageMapper(Session session, final String keySpace) {
        this.session = session;
        this.mappingManager = new MappingManager(session);

        KEY_SPACE = keySpace;
        initializeMapper();
    }

    protected final void initializeMapper() {
        mapper = mappingManager.mapper(Message.class);
    }

    /**
     * Create single entry in database
     *
     * @param modelable
     */
    public boolean createEntry(Message model) {
        try {
            mapper.save(model);
            return true;
        } catch (Exception e) {
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - message", e.getMessage());
        }

        return false;
    }

    /**
     * Create single entry in database async
     *
     * @param modelable
     */
    public void createEntryAsync(Message message) {
        mapper.saveAsync(message);
    }

    /**
     * Update single entry
     *
     * @param modelable
     */
    public void updateEntry(Message message) {
        mapper.saveAsync(message);
    }

    /**
     * Get multiple entries by executing a select statement
     *
     * @param bound - the statement to execute
     * @return
     */
    public Result<Message> getMultiple(BoundStatement bound) {
        ResultSet results = session.execute(bound);
        return mapper.map(results);
    }

    /**
     * Generate bound statement for delete
     *
     * @param entryUUID
     * @return
     */
    public BoundStatement getDeleteBoundStatement(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                String.format("delete from %s.message where message_uuid = ?", KEY_SPACE)
        );
        return prepared.bind(entryUUID);
    }

    /**
     * Delete entry async by uudi
     *
     * @param entryUUID
     */
    public boolean deleteEntryAsync(UUID entryUUID) {
        try {
            session.executeAsync(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - message", e.getMessage());
        }
        return false;
    }

    /**
     * Delete entry async by uuid
     *
     * @param entryUUID
     */
    public boolean deleteEntry(UUID entryUUID) {
        try {
            session.execute(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - message", e.getMessage());
        }
        return false;
    }

    /**
     * Delete multiple entries by key column name and id list
     *
     * @param entryUUIDs - the list of uuid's to delete with
     * @param keyColumnName - the index column to deal with
     * @return success
     */
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
        PreparedStatement prepared = session.prepare(
                String.format("delete from %s.message where %s in ?", KEY_SPACE, keyColumnName)
        );
        BoundStatement bound = prepared.bind(entryUUIDs);

        try {
            session.execute(bound);
            return true;
        } catch (Exception e) {
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete multiple - message", e.getMessage());
        }

        return false;
    }

    /**
     * Execute single query
     *
     * @param query
     */
    public boolean executeQuery(String query) {
        try {
            session.execute(query);
            return true;
        } catch (Exception e) {
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra query - message", e.getMessage());
        }

        return false;
    }

    /**
     * Get single entry
     *
     * @param entryUUID
     * @return
     */
    public Message getEntry(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                String.format("select * from %s.message where message_uuid = ?", KEY_SPACE)
        );
        BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);

        return mapper.map(results).one();
    }

    /**
     * Get message by object
     *
     * @param item the object to find
     * @return Message or null
     */
    public Message getEntry(Message item) {
        return getEntry(item.getMessage_uuid());
    }

}
