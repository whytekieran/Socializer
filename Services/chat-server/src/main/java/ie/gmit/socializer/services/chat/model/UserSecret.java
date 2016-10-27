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
 * @description User - Model for user_secret 
 * @package ie.gmit.socializer.services.chat.model
 */
package ie.gmit.socializer.services.chat.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import ie.gmit.socializer.services.chat.common.SecurityUtil;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;

@Table(keyspace = "app_user_data", name = "user_secret",
       caseSensitiveKeyspace = false,
       caseSensitiveTable = false)
public class UserSecret {
    @PartitionKey
    private UUID user_uuid;
    private String secret_hash;
    private String challenge;
    private String iv;
    private String salt;
    private String enc_private_key;
    private String public_key;
    private Date created;
    private Date updated;

    public UserSecret() {
    }

    public UserSecret(UUID user_uuid) {
        this.user_uuid = user_uuid;
        created = new Date();
        updated = new Date();
        initializeNewUserRandoms();
    }

    private void initializeNewUserRandoms(){
        int byteLen = SecurityUtil.getRandomIntInRange(64, 256);
        challenge = new String(Hex.encodeHex(SecurityUtil.getSecureBytes(byteLen)));
        iv = new String(Hex.encodeHex(SecurityUtil.getSecureBytes(16)));
        salt = new String(Hex.encodeHex(SecurityUtil.getSecureBytes(16)));
    }
    
    
    public UUID getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(UUID user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getSecret_hash() {
        return secret_hash;
    }

    public void setSecret_hash(String secret_hash) {
        this.secret_hash = secret_hash;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEnc_private_key() {
        return enc_private_key;
    }

    public void setEnc_private_key(String enc_private_key) {
        this.enc_private_key = enc_private_key;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public void setUpdated() {
        this.updated = new Date();
    }
}
