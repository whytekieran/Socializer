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
 * @description IdentificationProtocol - Protocol to support user identification with encrypted messages
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.protocol;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Result;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.gmit.socializer.services.chat.model.MessageSession;
import ie.gmit.socializer.services.chat.model.MessageSessionMapper;
import ie.gmit.socializer.services.chat.model.OauthToken;
import ie.gmit.socializer.services.chat.model.OauthTokenMapper;
import ie.gmit.socializer.services.chat.model.UserSecret;
import ie.gmit.socializer.services.chat.model.UserSecretMapper;
import ie.gmit.socializer.services.chat.storage.CassandraConnector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentificationProtocol {
    private final int VERSION = 100;
    private final int AUTHENTICATION_MESSAGE_MAX = 9; // 0-9
    private final int ACTION_MESSAGE_MAX = 19; // 10-19
    private final int SESSION_MESSAGE_MAX = 29; // 20-29
    private final String APP_DATA_KS = "app_data";
    private final String APP_USER_DATA_KS = "app_user_data";
    private final ObjectMapper objectMapper;
    private Cluster cluster;
    private OauthTokenMapper otm;
    
    public IdentificationProtocol() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);//Avoid generating null values
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Remove the unknown fields
        
        initializeDataConnection();
    }
    
    /**
     * Initialize the data connection
     * 
     * Note: can be called if session or cluster is closed
     */
    protected final void initializeDataConnection(){
        if(null == cluster || cluster.isClosed())
            cluster = CassandraConnector.initalizeConnection(APP_DATA_KS);
        
        if(otm == null)
            otm = new OauthTokenMapper(cluster.newSession(), APP_DATA_KS);
    }
    
    /**
     * 
     * @todo: Should check token modification attack (use a hashmap as current connection => key, value => token )
     * 
     * @param jsonMessage
     * @return 
     */
    public String routeSocketMessage(final String jsonMessage){
        SocketMessage sm = tryParseSocketMessage(jsonMessage, SocketMessage.class);
        int messageState = sm == null ? -1 : sm.getState();
        int msVersion = sm == null ? 99 : sm.getVersion();
        OauthToken token = sm == null ? null : validateToken(sm.getToken());
        SocketMessage response;
        
        //Use fuzzy-logic
        if(messageState < 0 || msVersion != VERSION || null == token){
            //Invalid message state
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state or structure. Please use js library", messageState, VERSION);
        }else if(messageState < AUTHENTICATION_MESSAGE_MAX){
            AuthenticationMessage authMessage = tryParseSocketMessage(jsonMessage, AuthenticationMessage.class);
            if(null != authMessage){
                response = handleAthenticationMessage(authMessage, token);
            }else{
                response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSING", 400, "Invalid message state or structure. Please use js library", messageState, VERSION);
            }
        }else if(messageState < ACTION_MESSAGE_MAX){
            ActionMessage actionMessage = tryParseSocketMessage(jsonMessage, ActionMessage.class);
            if(null != actionMessage){
                response = handleActionMessage(actionMessage, token);
            }else{
                response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSING", 400, "Invalid message state or structure. Please use js library", messageState, VERSION);  
            }
        }else if(messageState < SESSION_MESSAGE_MAX){
            SessionMessage sessionMessage = tryParseSocketMessage(jsonMessage, SessionMessage.class);
            if(null != sessionMessage){
                response = handleSessionMessage(sessionMessage, token);
            }else{
                response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSING", 400, "Invalid message state or structure. Please use js library", messageState, VERSION);
            }
        }else{
            //Can not idetify
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state", messageState, VERSION);
        }
        
        return convertObjectToString(response);
    }
    
    protected SocketMessage handleActionMessage(ActionMessage actionMessage, OauthToken token){
        SocketMessage response = null;
        switch(actionMessage.getAction()){
            case ActionMessage.CREATE_MESSAGE_SESSION:
                /**
                 * At this stage if all is correct, the public keys are available this makes the session key generation possible server side
                 * Not too cool :( should break into client steps
                 */
                try{
                    response = new SocketMessage();
                }catch(Exception ex){
                    //Pass the message onto client
                    response = new ErrorMessage("", 400, ex.getMessage(), actionMessage.getState(), VERSION);
                }
                break;
        }
        
        return response;
    }
    
    protected UUID createSessionMessage(ActionMessage actionMessage) throws Exception{
        List<UUID> userUuids = convertStringListToUUIDList(actionMessage.getAction_values());
        if(null != userUuids){
            try(Session currentSession = cluster.newSession()){
                UserSecretMapper usm = new UserSecretMapper(cluster.newSession(), APP_USER_DATA_KS);
                PreparedStatement prepared = currentSession.prepare(String.format("select * from %s.user_secret where user_uuid in ?", APP_USER_DATA_KS));
        
                BoundStatement bound = prepared.bind(userUuids);
                List<UserSecret> users = usm.getMultiple(bound).all();
                if(users.size() == userUuids.size()){
                    //create entry without crypto
                    MessageSession ms = new MessageSession(userUuids, "Chat session", 1);
                    MessageSessionMapper msm = new MessageSessionMapper(currentSession, APP_USER_DATA_KS);
                    boolean created = msm.createEntry(ms);
                    if(created){
                        return ms.getMsession_uuid();
                    }else{
                        //should try async
                        throw new Exception("Could not create message session");
                    }
                }else{
                    throw new Exception("Not all users have secrets");
                }
            }
        }else{
            throw new Exception("Invalid uuid(s) passed ");
        }
    }
    
    /**
     * Convert String uuids to UUID objects
     * 
     * @param strUuidList - list of string uuids
     * @return populated list with UUID objects
     */
    protected List<UUID> convertStringListToUUIDList(List<String> strUuidList){
        List<UUID> user_uuids = new ArrayList<>();
        for(String sUuid : strUuidList){
            UUID user_uuid = convertStringToUUID(sUuid);
            if(null != user_uuid)
                user_uuids.add(user_uuid);
            else
                return null;
        }
        
        return user_uuids;
    }
    
    protected AuthenticationMessage handleAthenticationMessage(AuthenticationMessage authMessage, OauthToken token){
        //Check the client generated hash agains the stored hash // or store it
            //check if fileds are initialized for keys 
                //if are and nothing is stored => store them
                //else ignore (possible smart ass)
        
        return authMessage;
    }
    
    protected SessionMessage handleSessionMessage(SessionMessage sessionMessage, OauthToken token){
        return sessionMessage;
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
    protected OauthToken validateToken(String token){
        UUID uuid = convertStringToUUID(token);
        if(null != token)
            return otm.getEntry(uuid);
        else 
            return null;
    }
    
    protected UUID convertStringToUUID(String token){
        try{
            return UUID.fromString(token);
        }catch(Exception ex){
            //Invalid format
            Logger.getLogger(IdentificationProtocol.class.getName()).log(Level.SEVERE, "Could not parse Auth token - validateToken", ex);
        }
        
        return null;
    }
    
    /**
     * Try convert socket message json string to object
     * 
     * @param <T> - a SocketMessage object to serialize
     * @param jsonMessage - 
     * @param type
     * @return 
     */
    private <T extends Object> T tryParseSocketMessage(final String jsonMessage, Class<T> type){
        try {
            return objectMapper.readValue(jsonMessage.getBytes("UTF-8"), type);
        } catch (IOException ex) {
            Logger.getLogger(IdentificationProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Try convert object to json string
     * @param sm
     * @return 
     */
    private String convertObjectToString(SocketMessage sm){
        try {
            return objectMapper.writeValueAsString(sm);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IdentificationProtocol.class.getName()).log(Level.SEVERE, "Could not convert protocol message - IdentificationProtocol", ex);
        }
        
        return "";
    }
}
