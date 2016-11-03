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
 * @description ActionMessageIMPL - Handler class for action message
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import ie.gmit.socializer.services.chat.common.DataParseHelper;
import ie.gmit.socializer.services.chat.model.MessageSession;
import ie.gmit.socializer.services.chat.model.MessageSessionMapper;
import ie.gmit.socializer.services.chat.model.OauthToken;
import ie.gmit.socializer.services.chat.model.UserSecret;
import ie.gmit.socializer.services.chat.model.UserSecretMapper;
import java.util.List;
import java.util.UUID;

public class ActionMessageHandler implements Commandable {
    private final DataParseHelper dataParseHelper;
    private final Cluster cluster;
    private String message;
    private OauthToken token;

    public ActionMessageHandler(Cluster cluster) {
        this.cluster = cluster;
        dataParseHelper = DataParseHelper.getInstance();
    }

    public ActionMessageHandler(DataParseHelper dataParseHelper, Cluster cluster) {
        this.dataParseHelper = dataParseHelper;
        this.cluster = cluster;
    }
    
    /**
     * Initialize the data to handle
     * 
     * @param message - the message to parse
     * @param token - the token for current message
     */
    public void initialize(String message, OauthToken token){
        this.message = message;
        this.token = token;
    }
    
    /**
     * Execute the message processing
     * 
     * @return the result of execution
     */
    public String execute(){
        ActionMessage am = dataParseHelper.tryParseJsonString(message, ActionMessage.class);
        SocketMessage result = handleActionMessage(am, token);
        
        return dataParseHelper.convertObjectToJsonString(result);
    }
    
    /**
     * Route the action message to handler
     * @param actionMessage
     * @param token
     * @return 
     */
    public SocketMessage handleActionMessage(ActionMessage actionMessage, OauthToken token){
        SocketMessage response = null;
        switch(actionMessage.getAction()){
            case ActionMessage.CREATE_MESSAGE_SESSION:
                /**
                 * At this stage if all is correct, the public keys are available this makes the session key generation possible server side
                 * Not too cool :( should break into client steps
                 */
                try{
                    UUID session_uuid = createSessionMessage(actionMessage);
                    actionMessage.setResult(session_uuid.toString());
                    response = actionMessage;
                }catch(ProtocolException ex){
                    //Pass the message onto client
                    response = new ErrorMessage("", 400, ex.getMessage(), actionMessage.getState(), ProtocolConstants.VERSION);
                }
                break;
        }
        
        return response;
    }
    
    /**
     * Validate and create message session
     * 
     * @param actionMessage
     * @return UUID - the message session uuid
     * @throws ProtocolException 
     */
    protected UUID createSessionMessage(ActionMessage actionMessage) throws ProtocolException{
        List<UUID> userUuids = dataParseHelper.convertStringListToUUIDList(actionMessage.getAction_values());
        if(null != userUuids){
            try(Session currentSession = cluster.newSession()){
                UserSecretMapper usm = new UserSecretMapper(cluster.newSession(), ProtocolConstants.APP_USER_DATA_KS);
                PreparedStatement prepared = currentSession.prepare(String.format("select * from %s.user_secret where user_uuid in ?", ProtocolConstants.APP_USER_DATA_KS));
        
                BoundStatement bound = prepared.bind(userUuids);
                List<UserSecret> users = usm.getMultiple(bound).all();
                if(users.size() == userUuids.size()){
                    //create entry without crypto
                    MessageSession ms = new MessageSession(userUuids, "Chat session", 1);
                    MessageSessionMapper msm = new MessageSessionMapper(currentSession, ProtocolConstants.APP_USER_DATA_KS);
                    boolean created = msm.createEntry(ms);
                    if(created){
                        return ms.getMsession_uuid();
                    }else{
                        //Should try async
                        throw new ProtocolException(500, "API.CHAT.PROTOCOL.ACTION.DB", "Could not create message session");
                    }
                }else{
                    throw new ProtocolException(500, "API.CHAT.PROTOCOL.ACTION.VALIDATION", "Not all users have secrets");
                }
            }
        }else{
            throw new ProtocolException(500, "API.CHAT.PROTOCOL.ACTION.PARSE", "Invalid uuid(s)");
        }
    }
    
}
