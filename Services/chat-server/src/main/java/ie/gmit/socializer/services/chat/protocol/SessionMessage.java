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
 * @description SessionMessage - Short description
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

public class SessionMessage extends SocketMessage {
    //Regular message in existing session
    private String skey;// The session key
    //Every session
    private String session;// The messsage session id
    private String msgbody;// The message body

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getMsgbody() {
        return msgbody;
    }

    public void setMsgbody(String msgbody) {
        this.msgbody = msgbody;
    }
}
