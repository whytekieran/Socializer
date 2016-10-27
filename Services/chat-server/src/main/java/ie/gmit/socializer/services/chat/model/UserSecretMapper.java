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
 * @description UserSecretMapper - Model Mapper for user_secret cassandra table
 * @package ie.gmit.socializer.services.chat.model
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


public class UserSecretMapper implements Mappable<UserSecret>{
    protected Mapper<UserSecret> mapper;
    protected Session session;
    protected MappingManager mappingManager;
    protected final String KEY_SPACE;

    public UserSecretMapper(Session session, final String keySpace) {
        this.session = session;
        this.mappingManager = new MappingManager(session);

        KEY_SPACE = keySpace;
        initializeMapper();
    }

    protected final void initializeMapper() {
        mapper = mappingManager.mapper(UserSecret.class);
    }
    
    @Override
    public boolean createEntry(UserSecret model) {
        try {
            mapper.save(model);
            return true;
        } catch (Exception e) {
            Logger.getLogger(UserSecretMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - user_secret", e);
        }
        return false;
    }

    @Override
    public void createEntryAsync(UserSecret model) {
        mapper.saveAsync(model);
    }

    @Override
    public BoundStatement getDeleteBoundStatement(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                String.format("delete from %s.user_secret where user_uuid = ?", KEY_SPACE)
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
            Logger.getLogger(UserSecretMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - user_secret", e);
        }
        return false;
    }

    @Override
    public boolean deleteEntryAsync(UUID entryUUID) {
        try {
            session.executeAsync(getDeleteBoundStatement(entryUUID));
            return true;
        } catch (Exception e) {
            //This is only possible if there is no connection
            Logger.getLogger(UserSecretMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete async - user_secret", e);
        }
        return false;
    }

    @Override
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
        PreparedStatement prepared = session.prepare(
                String.format("delete from %s.user_secret where %s in ?", KEY_SPACE, keyColumnName)
        );
        BoundStatement bound = prepared.bind(entryUUIDs);

        try {
            session.execute(bound);
            return true;
        } catch (Exception e) {
            Logger.getLogger(UserSecretMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete multiple - user_secret", e);
        }

        return false;
    }

    @Override
    public boolean executeQuery(String query) {
        try {
            session.execute(query);
            return true;
        } catch (Exception e) {
            Logger.getLogger(UserSecretMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra query - user_secret", e);
        }

        return false;
    }

    @Override
    public void updateEntry(UserSecret model) {
        model.setUpdated();
        mapper.saveAsync(model);
    }

    @Override
    public UserSecret getEntry(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                String.format("select * from %s.user_secret where user_uuid = ?", KEY_SPACE)
        );
        
        BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);

        return mapper.map(results).one();
    }

    @Override
    public Result<UserSecret> getMultiple(BoundStatement bound) {
        ResultSet results = session.execute(bound);
        return mapper.map(results);
    }
    
}
