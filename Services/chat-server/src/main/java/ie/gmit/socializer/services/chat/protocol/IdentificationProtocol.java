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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentificationProtocol {
    private final int VERSION = 100;
    private final ObjectMapper objectMapper;

    public IdentificationProtocol() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);//Avoid generating null values
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Remove the unknown fields
    }
    
    /**
     * 
     * @todo: Should check token modification attack (use a hashmap as current connection => key, value => token )
     * 
     * @param jsonMessage
     * @return 
     */
    public String routeSocketMessage(final String jsonMessage){
        SocketMessage sm = tryConvertSocketMessage(jsonMessage, SocketMessage.class);
        int messageState = sm == null ? -1 : sm.getState();
        int msVersion = sm == null ? 99 : sm.getVersion();
        SocketMessage response;
        
        if(messageState < 0 || msVersion != VERSION){
            //invalid message state
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state or structure. Please use js library", messageState, VERSION);
        }else if(messageState < 10){
            //Authentication protocol call
            //Validate token and state
            //Check the client generated hash agains the stored hash // or store it
            //check if fileds are initialized for keys 
                //if are and nothing is stored => store them
                //else ignore (possible smart ass)
            response = tryConvertSocketMessage(jsonMessage, AuthenticationMessage.class);
        }else if(messageState < 20){
            //Action message
            //Validate token and state
            //Validate action, possibly route to existing session or execute action
            response = tryConvertSocketMessage(jsonMessage, ActionMessage.class);
        }else if(messageState < 30){
            //Session message
            //Validate token and state
            //Get the session id and handle the storage of the message
            response = tryConvertSocketMessage(jsonMessage, SessionMessage.class);
        }else{
            //Can not idetify
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state", messageState, VERSION);
        }
        
        
        return convertObjectToString(response);
    }
    
    
    /**
     * Try convert socket message json string to object
     * 
     * @param <T> - a SocketMessage object to serialize
     * @param jsonMessage - 
     * @param type
     * @return 
     */
    private <T extends Object> T tryConvertSocketMessage(final String jsonMessage, Class<T> type){
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
