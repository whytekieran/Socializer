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
 * @since November 2016
 * @version 0.1
 * @description SessionMessageHandler - Handler class for session message
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import ie.gmit.socializer.services.chat.model.DataParseHelper;
import ie.gmit.socializer.services.chat.model.Message;
import ie.gmit.socializer.services.chat.model.MessageMapper;
import ie.gmit.socializer.services.chat.model.MessageSession;
import ie.gmit.socializer.services.chat.model.MessageSessionMapper;
import ie.gmit.socializer.services.chat.model.OauthToken;
import java.util.UUID;

public class SessionMessageHandler {
    private final DataParseHelper dataParseHelper;
    private final Cluster cluster;

    public SessionMessageHandler(Cluster cluster) {
        this.cluster = cluster;
        dataParseHelper = DataParseHelper.getInstance();
    }

    public SessionMessageHandler(DataParseHelper dataParseHelper, Cluster cluster) {
        this.dataParseHelper = dataParseHelper;
        this.cluster = cluster;
    }

    public SocketMessage handleSessionMessage(SessionMessage sessionMessage, OauthToken token){
        SocketMessage response = null;
        UUID msession_uuid = dataParseHelper.convertStringToUUID(sessionMessage.getSession());
        if(null != msession_uuid){
            try(Session session = cluster.newSession()){
                MessageSessionMapper msm = new MessageSessionMapper(session, ProtocolConstants.APP_USER_DATA_KS);
                MessageSession ms = msm.getEntry(msession_uuid);
                if(null != ms && ms.containsUser(token.getUser_uuid())){
                    Message msg = new Message(msession_uuid, token.getUser_uuid(), sessionMessage.getMsgbody(), 0);
                    MessageMapper mp = new MessageMapper(session, ProtocolConstants.APP_USER_DATA_KS);
                    if(mp.createEntry(msg)){
                        sessionMessage.setMsgid(msg.getMessage_uuid().toString());
                        sessionMessage.setStatus(200);
                        response = sessionMessage;
                    }else{
                        response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.DB", 500, "Could not store message", sessionMessage.getState(), ProtocolConstants.VERSION);
                    }
                }else{
                    response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.VALIDATION", 400, "User is not part of the session", sessionMessage.getState(), ProtocolConstants.VERSION);
                }
            }//End try with resources             
        }else{
            response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSE", 400, "Could not parse session id", sessionMessage.getState(), ProtocolConstants.VERSION);
        }
        
        return response;
    }
}
