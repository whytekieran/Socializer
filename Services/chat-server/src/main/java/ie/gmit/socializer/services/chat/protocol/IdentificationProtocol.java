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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentificationProtocol {
    private final int VERSION = 100;
    private final ObjectMapper objectMapper;

    public IdentificationProtocol() {
        objectMapper = new ObjectMapper();
    }
    
    public String routeSocketMessage(final String jsonMessage){
        SocketMessage sm = tryConvertSocketMessage(jsonMessage);
        int messageState = sm == null ? -1 : sm.getState();
        SocketMessage response = null;
        System.err.println("sm " + sm.getToken());
        
        if(messageState < 0){
            //invalid message state
            response = new ErrorMessage("API.CHAT.PROTOCOL.VERIFICATION", 400, "Invalid message state", sm.getState(), VERSION);
        }else if(messageState < 10){
            
        }
        
        
        return convertObjectToString(response);
    }
    
    private SocketMessage tryConvertSocketMessage(final String jsonMessage){
        try {
            return objectMapper.readValue(jsonMessage.getBytes("UTF-8"), SocketMessage.class);
        } catch (IOException ex) {
            Logger.getLogger(IdentificationProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private String convertObjectToString(SocketMessage sm){
        try {
            return objectMapper.writeValueAsString(sm);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IdentificationProtocol.class.getName()).log(Level.SEVERE, "Could not convert protocol message - IdentificationProtocol", ex);
        }
        
        return "";
    }
}
