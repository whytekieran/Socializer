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
 * @description OauthToken - Entity class for oauth2_data Cassandra column family 
 * @package ie.gmit.socializer.services.chat.model
 */
package ie.gmit.socializer.services.chat.model;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ie.gmit.socializer.services.chat.common.SecurityUtil;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;

@Table(keyspace = "app_data", name = "oauth2_data",
       caseSensitiveKeyspace = false,
       caseSensitiveTable = false)
public class OauthToken {
    @PartitionKey(0)
    private String access_token;
    private UUID client_uuid;
    private UUID user_uuid;
    private Date expires;
    private String scope;
    private String token_type;
    private Date created;

    public OauthToken(UUID client_uuid, UUID user_uuid, Date expires, String scope, String token_type) {
        byte[] currentTimeBytes = ByteBuffer.allocate(Long.BYTES).putLong(System.currentTimeMillis()).array();
        this.access_token = Hex.encodeHexString(SecurityUtil.addAll(SecurityUtil.getSecureBytes(16), currentTimeBytes));
        this.client_uuid = client_uuid;
        this.user_uuid = user_uuid;
        this.expires = expires;
        this.scope = scope;
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UUID getClient_uuid() {
        return client_uuid;
    }

    public void setClient_uuid(UUID client_uuid) {
        this.client_uuid = client_uuid;
    }

    public UUID getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(UUID user_uuid) {
        this.user_uuid = user_uuid;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    
}
