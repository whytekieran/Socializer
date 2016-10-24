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
 * @description SocketMessage - Authentication message entity used for json communication to share encrypted keys
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.protocol;

public class AuthenticationMessage extends SocketMessage {    
    private String chash;//The challenge hash to verify 
    private String iv;//The initialization vector used by client (generated on server and stored)
    private String salt;//The salt used by client 
    private String prkey;// The private key (ecnryted)
    private String pukey;// The public key (clear-text)

    public String getChash() {
        return chash;
    }

    public void setChash(String chash) {
        this.chash = chash;
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

    public String getPrkey() {
        return prkey;
    }

    public void setPrkey(String prkey) {
        this.prkey = prkey;
    }

    public String getPukey() {
        return pukey;
    }

    public void setPukey(String pukey) {
        this.pukey = pukey;
    }
}
