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
 * @description OauthTokenMapper - The entity mapper for Cassandra
 * @package ie.gmit.socializer.services.chat.model
 */
package ie.gmit.socializer.services.chat.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OauthTokenMapper implements Mappable<OauthToken>{
    protected Mapper<OauthToken> mapper;
    protected Session session;
    protected MappingManager mappingManager;
    protected final String KEY_SPACE;
    
    public OauthTokenMapper(Session session, final String keySpace) {
        this.KEY_SPACE = keySpace;
        this.session = session;
        this.mappingManager = new MappingManager(session);
        initializeMapper();
    }
    
    protected final void initializeMapper(){
        mapper = mappingManager.mapper(OauthToken.class);
    }
    
    @Override
    public boolean createEntry(OauthToken model) {
        try{
            mapper.save(model);
            return true;
        }catch(Exception e){
            Logger.getLogger(OauthTokenMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - oauth_token", e);
        }
        return false;
    }

    @Override
    public void createEntryAsync(OauthToken model) {
        mapper.saveAsync(model);
    }

    @Override
    public BoundStatement getDeleteBoundStatement(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                            String.format("delete from %s.oauth2_data where access_token = ?", KEY_SPACE)
                              );
        return prepared.bind(entryUUID);
    }

    @Override
    public boolean deleteEntry(UUID entryUUID) {
        try {
            session.execute(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(MessageSessionMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - oauth_token", e);
        }
        return false;
    }

    @Override
    public boolean deleteEntryAsync(UUID entryUUID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean executeQuery(String query) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateEntry(OauthToken model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OauthToken getEntry(UUID entryUUID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Result<OauthToken> getMultiple(BoundStatement bound) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
