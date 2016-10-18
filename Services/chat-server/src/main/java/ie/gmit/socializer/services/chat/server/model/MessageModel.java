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
 * @description MessageModel - Entity for message table data
 * @package ie.gmit.socializer.services.chat.server.model
 */
package ie.gmit.socializer.services.chat.server.model;

import com.datastax.driver.core.utils.UUIDs;
import java.util.UUID;


public class MessageModel{
    private UUID message_uuid;
    private UUID msession_uuid;
    private String content;
    private int content_type;
    private long created;
    private long updated;

    public MessageModel() {}

    public MessageModel(UUID message_uuid, UUID msession_uuid, String content, int content_type, long created, long updated) {
        this.message_uuid = message_uuid;
        this.msession_uuid = msession_uuid;
        this.content = content;
        this.content_type = content_type;
        this.created = created;
        this.updated = updated;
    }
    
    public UUID getMessage_uuid() {
        return message_uuid;
    }

    public void setMessage_uuid(UUID message_uuid) {
        this.message_uuid = message_uuid;
    }

    /**
     * Generate new uuid and set
     */
    public void setMessage_uuid() {
        this.message_uuid = UUIDs.random();
    }
    
    public UUID getMsession_uuid() {
        return msession_uuid;
    }

    public void setMsession_uuid(UUID msession_uuid) {
        this.msession_uuid = msession_uuid;
    }

    /**
     * Generate new uuid and set
     */
    public void setMsession_uuid() {
        this.msession_uuid = UUIDs.random();
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Generate new uuid and set
     */
    public void setCreated() {
        this.created = System.currentTimeMillis();
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated() {
        this.updated = System.currentTimeMillis();
    }
    
    
    
}
