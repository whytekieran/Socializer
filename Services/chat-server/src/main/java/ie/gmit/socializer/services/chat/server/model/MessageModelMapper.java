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
 * @description MessageModelMapper - Short description
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.server.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import java.util.UUID;

public class MessageModelMapper implements Mapable{
    
    protected Mapper<MessageModel> mapper;
    protected Session session;
    protected MappingManager mappingManager;
    protected final String KEY_SPACE;
    
    public MessageModelMapper(Session session, final String keySpace) {
        this.session = session;
        this.mappingManager = new MappingManager(session);
        
        KEY_SPACE = keySpace;
        initializeMapper();
    }
    
    protected final void initializeMapper(){
        mapper = mappingManager.mapper(MessageModel.class);
    }
    
    /**
     * Create single entry in database
     * @param modelable 
     */
    @Override
    public void createEntry(Modelable modelable){
        mapper.save((MessageModel)modelable);
    }
    
    /**
     * Create single entry in database async
     * @param modelable 
     */
    @Override
    public void createEntryAsync(Modelable modelable){
        mapper.saveAsync((MessageModel)modelable);
    }
    /**
     * Update single entry
     * @param modelable 
     */
    @Override
    public void updateEntry(Modelable modelable){
        mapper.saveAsync((MessageModel)modelable);
    }
    
    /**
     * Get multiple entries by executing a select statement 
     * 
     * @param query
     * @return 
     */
    @Override
    public Result<MessageModel> getMultiple(BoundStatement bound){
        ResultSet results = session.execute(bound);
        return mapper.map(results);
    }
    
    /**
     * Delete entry async by uudi
     * @param entryUUID 
     */
    @Override
    public void deleteEntryAsync(UUID entryUUID){
        mapper.deleteAsync(entryUUID);
    }
    
    /**
     * Delete entry async by uuid
     * @param entryUUID 
     */
    @Override
    public void deleteEntry(UUID entryUUID){
        mapper.delete(entryUUID);
    }
    
    /**
     * Execute single query
     * @param query 
     */
    @Override
    public void executeQuery(String query){
        session.execute(query);
    }
    
    /**
     * Get single entry
     * @param entryUUID
     * @return 
     */
    public MessageModel getEntry(UUID entryUUID){
        PreparedStatement prepared = session.prepare(
                            String.format("select * from %s.message where message_uuid = ?", KEY_SPACE)
                              );
        BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);
        
        System.out.println("Query: " + String.format("select * from %s.message where message_uuid = " + entryUUID.toString(), KEY_SPACE) + "\n Results" + results.toString());
        
        return mapper.map(results).one();
    }
    
    public MessageModel getEntry(MessageModel item){
        return getEntry(item.getMessage_uuid());
    }
    
}
