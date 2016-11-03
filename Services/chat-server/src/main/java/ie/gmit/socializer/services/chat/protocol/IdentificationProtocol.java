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
import ie.gmit.socializer.services.chat.common.DataParseHelper;
import ie.gmit.socializer.services.chat.log.LogFormatter;
import ie.gmit.socializer.services.chat.model.OauthToken;
import ie.gmit.socializer.services.chat.model.OauthTokenMapper;
import ie.gmit.socializer.services.chat.model.UserSecretMapper;
import ie.gmit.socializer.services.chat.storage.CassandraConnector;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdentificationProtocol {
    private Cluster cluster;
    private OauthTokenMapper otm;
    private UserSecretMapper usm;
    private final DataParseHelper dataParseHelper;
    
    private MessageCommand messageCommand;
    
    public IdentificationProtocol() {
        initializeDataConnection();
        dataParseHelper = DataParseHelper.getInstance();
        messageCommand = MessageCommand.getInstance(cluster);
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
        
        if(usm == null)
            usm = new UserSecretMapper(cluster.newSession(), ProtocolConstants.APP_USER_DATA_KS);
    }
    
    /**
     * Validate token by secret
     * 
     * @param tokenUUID
     * @return 
     */
    public OauthToken getToken(UUID tokenUUID){
        return otm.getEntry(tokenUUID);
    }
    
    /**
     * Identify user based on auth toke
     * 
     * @param strTokenUUID
     * @return 
     */
    public UUID identifyUser(String strTokenUUID){
        UUID tokenUUID = dataParseHelper.convertStringToUUID(strTokenUUID);
        if(tokenUUID != null){
            OauthToken token = getToken(tokenUUID);
            if(token != null){
                return token.getUser_uuid();
            }
        }
        
        return null;
    }
    
    /**
     * Get protocol connection error message
     * 
     * @param message
     * @return 
     */
    public String getConnectionErrorMessage(String message){
        return dataParseHelper.convertObjectToJsonString(
                new ErrorMessage("API.CHAT.SERVER", 400, message, -1, ProtocolConstants.VERSION));
    }
    
    /**
     * Send welcome message
     * 
     * @param message
     * @return 
     */
    public String getConnectionWelcomMessage(String message){
        return dataParseHelper.convertObjectToJsonString(
                new SocketMessage(){{
                    setState(100);
                    setStatus(200);
                    setVersion(ProtocolConstants.VERSION);
                }});
    }
    
    /**
     * Route socket message to handler
     * @todo: Should check token modification attack (use a hashmap as current connection => key, value => token )
     * 
     * @param jsonMessage
     * @return 
     */
    public String routeSocketMessage(final String jsonMessage){
        try{
            SocketMessage socketMessage = validateMessage(jsonMessage);
            OauthToken token = otm.validateToken(socketMessage.getToken());
            if(token == null) throw new ProtocolException(404, "API.CHAT.VALIDATION.TOKEN", "Token not found");
            
            Commandable message = messageCommand.newCommandable(socketMessage.getState());
            message.initialize(jsonMessage, token);
            
            return message.execute();
        }catch(ProtocolException ex){
            Logger.getLogger(IdentificationProtocol.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "Socket message error: " +  ex.getSource() + ":" + ex));
            return dataParseHelper.convertObjectToJsonString(new ErrorMessage(ex.getSource(), ex.getStatusCode(), ex.getMessage(), -1, ProtocolConstants.VERSION));
        }
    }
    
    /**
     * Validate socket message
     * 
     * @param message - the json message string
     * @return initialized socket message
     * 
     * @throws ie.gmit.socializer.services.chat.protocol.ProtocolException
     */
    protected SocketMessage validateMessage(String message) throws ProtocolException{
        SocketMessage socketMessage = dataParseHelper.tryParseJsonString(message, SocketMessage.class);
        if(socketMessage == null){
            throw new ProtocolException(400, "API.CHAR.PROTOCOL.PARSE", "Could not parse message");
        }else if(socketMessage.getVersion() != ProtocolConstants.VERSION){
            throw new ProtocolException(400, "API.CHAR.PROTOCOL.PARSE",  "Invalid protocol version");
        }else if(socketMessage.getToken() == null || dataParseHelper.convertStringToUUID(socketMessage.getToken()) == null){
            throw new ProtocolException(400, "API.CHAR.PROTOCOL.PARSE", "Invalid auth token - parse error");
        }else{
            return socketMessage;
        }
    }
    
    protected AuthenticationMessage handleAthenticationMessage(AuthenticationMessage authMessage, OauthToken token){
        //Check the client generated hash agains the stored hash // or store it
            //check if fileds are initialized for keys 
                //if are and nothing is stored => store them
                //else ignore (possible smart ass)
        
        return authMessage;
    }
}
