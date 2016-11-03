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
 * @description CryptoSocketServer - Websocket connection handler class
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.server;

import ie.gmit.socializer.services.chat.log.LogFormatter;
import ie.gmit.socializer.services.chat.protocol.IdentificationProtocol;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class CryptoSocketServer extends WebSocketServer{

    private final boolean IS_DEBUG;
    private final boolean IS_VERBOSE;
    private final IdentificationProtocol protocol;
    
    public CryptoSocketServer(InetSocketAddress address) {
        super(address);
        this.protocol = new IdentificationProtocol();
        IS_DEBUG = false;
        IS_VERBOSE = false;
    }
    
    public CryptoSocketServer(InetSocketAddress address, boolean isDebug, boolean isVerbose) {
        super(address);
        this.protocol = new IdentificationProtocol();
        IS_DEBUG = isDebug;
        IS_VERBOSE = isVerbose;
        if(IS_DEBUG)
            WebSocketImpl.DEBUG = true;
    }
    
    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {
        if(IS_DEBUG || IS_VERBOSE)
            Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "New Connection: " +  ws.getRemoteSocketAddress().getAddress().getHostAddress()));
        
    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        if(IS_DEBUG || IS_VERBOSE)
            Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "Connection closed: " +  ws.getRemoteSocketAddress().getAddress().getHostAddress()));
        
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        //start the custom protocol
        System.out.println(message);
        String response = protocol.routeSocketMessage(message);
        ws.send(response);
    }

    @Override
    public void onError(WebSocket ws, Exception excptn) {
        Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "Connection error: " +  ws.getRemoteSocketAddress()  + ":" + excptn));
        if(!IS_DEBUG)
            ws.close();
    }
}
