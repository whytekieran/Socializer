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

import com.datastax.driver.core.Cluster;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.gmit.socializer.services.chat.model.DataParseHelper;
import ie.gmit.socializer.services.chat.model.OauthToken;
import ie.gmit.socializer.services.chat.model.OauthTokenMapper;
import ie.gmit.socializer.services.chat.storage.CassandraConnector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentificationProtocol {
    private final ObjectMapper objectMapper;
    private Cluster cluster;
    private OauthTokenMapper otm;
    private final DataParseHelper dataParseHelper;
    private final ActionMessageHandler actionMessageHandler;
    private final SessionMessageHandler sessionMessageHandler;
    
    public IdentificationProtocol() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);//Avoid generating null values
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Remove the unknown fields
        
        initializeDataConnection();
        dataParseHelper = DataParseHelper.getInstance();
        actionMessageHandler = new ActionMessageHandler( dataParseHelper, cluster);
        sessionMessageHandler = new SessionMessageHandler(dataParseHelper, cluster);
    }
    
    /**
     * Initialize the data connection
     * 
     * Note: can be called if session or cluster is closed
     */
    protected final void initializeDataConnection(){
        if(null == cluster || cluster.isClosed())
            cluster = CassandraConnector.initalizeConnection(ProtocolConstants.APP_DATA_KS);
        
        if(otm == null)
            otm = new OauthTokenMapper(cluster.newSession(), ProtocolConstants.APP_DATA_KS);
    }
    
    /**
     * Route socket message to handler
     * @todo: Should check token modification attack (use a hashmap as current connection => key, value => token )
     * 
     * @param jsonMessage
     * @return 
     */
    public String routeSocketMessage(final String jsonMessage){
        SocketMessage sm = tryParseSocketMessage(jsonMessage, SocketMessage.class);
        int messageState = sm == null ? -1 : sm.getState();
        int msVersion = sm == null ? 99 : sm.getVersion();
        OauthToken token = sm == null ? null : otm.validateToken(sm.getToken());
        SocketMessage response;
        
        //Use fuzzy-logic
        if(messageState < 0 || msVersion != ProtocolConstants.VERSION || null == token){
            //Invalid message state
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state or structure. Please use js library", messageState, ProtocolConstants.VERSION);
        }else if(messageState < ProtocolConstants.AUTHENTICATION_MESSAGE_MAX){
            AuthenticationMessage authMessage = tryParseSocketMessage(jsonMessage, AuthenticationMessage.class);
            if(null != authMessage){
                response = handleAthenticationMessage(authMessage, token);
            }else{
                response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSING", 400, "Invalid message state or structure. Please use js library", messageState, ProtocolConstants.VERSION);
            }
        }else if(messageState < ProtocolConstants.ACTION_MESSAGE_MAX){
            ActionMessage actionMessage = tryParseSocketMessage(jsonMessage, ActionMessage.class);
            if(null != actionMessage){
                response = actionMessageHandler.handleActionMessage(actionMessage, token);
            }else{
                response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSING", 400, "Invalid message state or structure. Please use js library", messageState, ProtocolConstants.VERSION);  
            }
        }else if(messageState < ProtocolConstants.SESSION_MESSAGE_MAX){
            SessionMessage sessionMessage = tryParseSocketMessage(jsonMessage, SessionMessage.class);
            if(null != sessionMessage){
                response = sessionMessageHandler.handleSessionMessage(sessionMessage, token);
            }else{
                response = new ErrorMessage("API.CHAT.PROTOCOL.MESSAGE.PARSING", 400, "Invalid message state or structure. Please use js library", messageState, ProtocolConstants.VERSION);
            }
        }else{
            //Can not idetify
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state", messageState, ProtocolConstants.VERSION);
        }
        
        return convertObjectToString(response);
    }
    
    protected AuthenticationMessage handleAthenticationMessage(AuthenticationMessage authMessage, OauthToken token){
        //Check the client generated hash agains the stored hash // or store it
            //check if fileds are initialized for keys 
                //if are and nothing is stored => store them
                //else ignore (possible smart ass)
        
        return authMessage;
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
