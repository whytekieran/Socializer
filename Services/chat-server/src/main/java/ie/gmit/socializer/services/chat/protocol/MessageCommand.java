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
 * @description MessageCommand - Singleton for message command access
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

import com.datastax.driver.core.Cluster;
import org.apache.commons.lang3.NotImplementedException;

public class MessageCommand {
    private static MessageCommand messageCommand;
    private final Cluster cluster;
    
    private MessageCommand(Cluster cluster){
        this.cluster = cluster;
    }
    
    /**
     * Get singleton instance of class
     * 
     * @param cluster - the db cluster connection
     * @return 
     */
    public static MessageCommand getInstance(Cluster cluster){
        if(messageCommand == null){
            messageCommand = new MessageCommand(cluster);
        }
        
        return messageCommand;
    }
    
    /**
     * Get a new instance of message handler
     * 
     * @param commandType
     * @return 
     */
    public Commandable newCommandable(int commandType){
        if(commandType > 0 && commandType <= ProtocolConstants.AUTHENTICATION_MESSAGE_MAX) throw new NotImplementedException("Authentication message not implemented yet");
        else if(commandType <= ProtocolConstants.ACTION_MESSAGE_MAX) return new ActionMessageHandler(cluster);
        else if(commandType <= ProtocolConstants.SESSION_MESSAGE_MAX) return new SessionMessageHandler(cluster);
        else return null;//this should return Error message 
    }
}
