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
 * @description ServerConfigurator - The server configuration handler for cli
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.server;

import com.google.common.net.InetAddresses;
import java.net.InetSocketAddress;
import org.apache.commons.cli.CommandLine;
import org.java_websocket.server.WebSocketServer;

public class ServerConfigurator {
    
    /**
     * Route the command line parameters to server objects 
     * @todo: Implement benchmark server
     * @todo: Implement keyring management and generation for wss support
     * 
     * @param cli - Initialized CommandLine object with current parameters
     */
    protected static WebSocketServer configureServer(final CommandLine cli) {
        if (cli.hasOption('b')) {
            //prepare benchmark server configuration
            //run benchmark
            //Exit after benchmark
            return null;
        }else if(cli.hasOption('i') && tryParseHostname(cli.getOptionValue('i')) 
                    && cli.hasOption('p') && tryParsePort(cli.getOptionValue('p'))){
            //Check protocol params
            if(cli.hasOption('s') 
                    && cli.getOptionValue('s').equalsIgnoreCase("ws") || cli.getOptionValue('s').equalsIgnoreCase("wss")){
                //start with specified socket type
                //@todo: this has to implement server side keyring management (use keytool)
            }else{
                //normal run
            }
            return new CryptoSocketServer(
                            new InetSocketAddress(cli.getOptionValue('i'), Integer.parseInt(cli.getOptionValue('p')))
                            , cli.hasOption('d'), cli.hasOption('v'));
        }
        
        return null;
    }
    
    /**
     * Try parse port with validation
     * 
     * @param number
     * @return 
     */
    protected static boolean tryParsePort(String number){
        try{
            int num = Integer.parseInt(number);
            if(num == 80 || num == 443 
                    || (num > 1000 && num < 65535))
                return true;
        }catch(Exception e){
            //Not a number no way to resolve
        }
        
        return false;
    }
    
    /**
     * Check if given hostname is valid
     * 
     * @todo: should check for dns
     * @todo: should validate ip is available for current server
     * 
     * @param hostname
     * @return true if valid
     */
    protected static boolean tryParseHostname(String hostname){
        try{
            //Check ip address
            return InetAddresses.isInetAddress(hostname);
        }catch(Exception e){
            //Not inet address 
        }
        
        return false;
        
    }
}
