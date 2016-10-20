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
 * @description MessageSessionModel - Holds the object for message_session table
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.server.model;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Table;
import java.util.List;
import java.util.UUID;

@Table(keyspace = "app_user_data", name = "message_session",
       readConsistency = "QUORUM",
       writeConsistency = "QUORUM",
       caseSensitiveKeyspace = false,
       caseSensitiveTable = false)
public class MessageSessionModel implements Modelable{
    private UUID msession_uuid;
    private List<UUID> user_uuid_list;
    private String name;
    private int permission;
    private long created;
    private long updated;

    public MessageSessionModel() {}

    /**
     * Constructor for new message session with auto generated fields  
     * @param user_uuid_list - the list of participiants
     * @param name - the session name
     * @param permission - the visibility & permission of extending group
     */
    public MessageSessionModel(List<UUID> user_uuid_list, String name, int permission) {
        this.user_uuid_list = user_uuid_list;
        this.name = name;
        this.permission = permission;
    }
    
    /**
     * Constructor with fields 
     * @param msession_uuid - the session uuid
     * @param user_uuid_list - the list of participiants
     * @param name - the session name
     * @param permission - the visibility
     * @param created - timestamp of creation
     * @param updated - last updated timestamp
     */
    public MessageSessionModel(UUID msession_uuid, List<UUID> user_uuid_list, String name, int permission, long created, long updated) {
        this.msession_uuid = msession_uuid;
        this.user_uuid_list = user_uuid_list;
        this.name = name;
        this.permission = permission;
        this.created = created;
        this.updated = updated;
    }
    
    
    public UUID getMsession_uuid() {
        return msession_uuid;
    }

    public void setMsession_uuid(UUID msession_uuid) {
        this.msession_uuid = msession_uuid;
    }
    
    /**
     * Generate a random uuid
     */
    public void setMsession_uuid() {
        this.msession_uuid = UUIDs.random();
    }

    public List<UUID> getUser_uuid_list() {
        return user_uuid_list;
    }

    public void setUser_uuid_list(List<UUID> user_uuid_list) {
        this.user_uuid_list = user_uuid_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Generate current timestamp
     */
    public void setCreated() {
        this.created = System.currentTimeMillis();
    }
    
    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
    
    /**
     * Generate current timestamp
     */
    public void setUpdated() {
        this.updated = System.currentTimeMillis();
    }
}
