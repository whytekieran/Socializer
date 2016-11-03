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

import ie.gmit.socializer.services.chat.common.DataParseHelper;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OauthTokenMapper implements Mappable<OauthToken> {

    protected Mapper<OauthToken> mapper;
    protected Session session;
    protected MappingManager mappingManager;
    protected final String KEY_SPACE;
    protected final DataParseHelper dataParseHelper;

    public OauthTokenMapper(Session session, final String keySpace) {
        this.KEY_SPACE = keySpace;
        this.session = session;
        this.mappingManager = new MappingManager(session);
        dataParseHelper = DataParseHelper.getInstance();
        initializeMapper();
    }

    protected final void initializeMapper() {
        mapper = mappingManager.mapper(OauthToken.class);
    }

    @Override
    public boolean createEntry(OauthToken model) {
        try {
            long ttl = getDateDiff(new Date(), model.getExpires(), TimeUnit.SECONDS);
            mapper.save(model, Option.ttl((int) ttl));
            return true;
        } catch (Exception e) {
            Logger.getLogger(OauthTokenMapper.class.getName()).log(Level.SEVERE, "Could not create cassandra entry - oauth_token", e);
        }
        return false;
    }

    @Override
    public void createEntryAsync(OauthToken model) {
        long ttl = getDateDiff(new Date(), model.getExpires(), TimeUnit.SECONDS);
        mapper.saveAsync(model, Option.ttl((int)ttl));
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
            Logger.getLogger(OauthTokenMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete - oauth_token", e);
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
            Logger.getLogger(OauthTokenMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete async - oauth_token", e);
        }
        return false;
    }

    @Override
    public boolean deleteEntries(List<UUID> entryUUIDs, String keyColumnName) {
        PreparedStatement prepared = session.prepare(
                String.format("delete from %s.oauth2_data where %s in ?", KEY_SPACE, keyColumnName)
        );
        BoundStatement bound = prepared.bind(entryUUIDs);

        try {
            session.execute(bound);
            return true;
        } catch (Exception e) {
            Logger.getLogger(OauthTokenMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra delete multiple - oauth_token", e);
        }

        return false;
    }

    @Override
    public boolean executeQuery(String query) {
        try {
            session.execute(query);
            return true;
        } catch (Exception e) {
            Logger.getLogger(OauthTokenMapper.class.getName()).log(Level.SEVERE, "Could not execute cassandra query - oauth_token", e);
        }

        return false;
    }

    @Override
    public void updateEntry(OauthToken model) {
        long ttl = getDateDiff(new Date(), model.getExpires(), TimeUnit.SECONDS);
        mapper.saveAsync(model, Option.ttl((int) ttl));
    }

    @Override
    public OauthToken getEntry(UUID entryUUID) {
        PreparedStatement prepared = session.prepare(
                String.format("select * from %s.oauth2_data where access_token = ?", KEY_SPACE)
        );
        
        BoundStatement bound = prepared.bind(entryUUID);
        ResultSet results = session.execute(bound);

        return mapper.map(results).one();
    }

    @Override
    public Result<OauthToken> getMultiple(BoundStatement bound) {
        ResultSet results = session.execute(bound);
        return mapper.map(results);
    }

    /**
     * Validate ouath token against data
     * 
     * Note: this should be initialized from the core application
     * 
     * @todo: implement caching push to cache as <Token, User> with ttl caclulated on token
     * @param token - the string token
     * @return initialized token if valid
     */
    public OauthToken validateToken(String token){
        UUID uuid = dataParseHelper.convertStringToUUID(token);
        if(null != token)
            return getEntry(uuid);
        else 
            return null;
    }
    
    /**
     * Check if token exists
     * 
     * @param token
     * @return 
     */
    public boolean isValidToken(UUID token){
        return null != getEntry(token);
    }
    
    /**
     * Get a diff between two dates
     * 
     * Source: http://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
     * 
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
