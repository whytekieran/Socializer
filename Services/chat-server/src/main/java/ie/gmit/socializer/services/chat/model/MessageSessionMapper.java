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
 * @description MessageSessionModelMapper - ORM class for message model -> Cassandra Database
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

public class MessageSessionMapper implements Mappable<MessageSession>{
    protected Mapper<MessageSession> mapper;
    protected Session session;
    protected MappingManager mappingManager;
    protected final String KEY_SPACE;
    
    public MessageSessionMapper(Session session, final String keySpace) {
        this.session = session;
        this.mappingManager = new MappingManager(session);
        
        KEY_SPACE = keySpace;
        initializeMapper();
    }
    
    protected final void initializeMapper(){
        mapper = mappingManager.mapper(MessageSession.class);
    }
    
    /**
     * Create new entry in Database
     * 
     * @param ms - MessageSession object
     * @return true on success
     */
    public boolean createEntry(MessageSession ms) {
        try{
            mapper.save(ms);
            return true;
        }catch(Exception e){
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - message_session", e);
        }
        return false;
    }

    /**
     * Create new entry async 
     * @param ms 
     */
    public void createEntryAsync(MessageSession ms) {
        mapper.saveAsync(ms);
    }

    /**
     * Generate bound statement for delete entry from database
     * 
     * @param entryUUID - the unique identifier
     * @return BoundStatement
     */
    public BoundStatement getDeleteBoundStatement(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                            String.format("delete from %s.message_session where msession_uuid = ?", KEY_SPACE)
                              );
        return prepared.bind(entryUUID);
    }
    
    /**
     * Delete entry from database
     * @param entryUUID - the unique identifier
     * @return true on success
     */
    public boolean deleteEntry(UUID entryUUID) {
        try {
            session.execute(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - message_session", e);
        }
        return false;
    }

    /**
     * Delete entry async
     * 
     * @param entryUUID - the unique identifier
     * @return true if no exception
     */
    public boolean deleteEntryAsync(UUID entryUUID) {
        try {
            session.executeAsync(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete async - message_session", e);
        }
        return false;
    }

    /**
     * Execute nonQuery
     * 
     * @param query - string based query
     * @return 
     */
    public boolean executeQuery(String query) {
        try{
            session.execute(query);
            return true;
        }catch(Exception e){
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra query - message", e);
        }
        
        return false;
    }

    /**
     * Update entry in database
     * @param ms - MessageSession object
     */
    public void updateEntry(MessageSession ms) {
        ms.setUpdated();
        mapper.saveAsync(ms);
    }

    /**
     * Get single entry
     * @param entryUUID
     * @return MessageSession object or null
     */
    public MessageSession getEntry(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                            String.format("select * from %s.message_session where msession_uuid = ?", KEY_SPACE)
                              );
        BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);
        
        return mapper.map(results).one();
    }
    
    /**
     * Get multiple entries by executing a select statement 
     * 
     * @param bound - the statement to execute
     * @return 
     */
    public Result<MessageSession> getMultiple(BoundStatement bound){
        ResultSet results = session.execute(bound);
        return mapper.map(results);
    }
    
    /**
     * Delete entries from db associated with MessageSession 
     * 
     * @param entryUUIDs
     * @param keyColumnName
     * @return true on success
     */
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
        PreparedStatement prepared = session.prepare(
                            String.format("delete from %s.message_session where %s in ?", KEY_SPACE,  keyColumnName)
                              );
        BoundStatement bound = prepared.bind(entryUUIDs);
        
        try {
            session.execute(bound);
            return true;
        } catch (Exception e) {
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete multiple - message_session", e);
        }

        return false;
    }
    
}
