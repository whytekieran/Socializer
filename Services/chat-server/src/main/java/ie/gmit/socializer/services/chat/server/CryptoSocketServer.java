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

import ie.gmit.socializer.services.chat.common.DataParseHelper;
import ie.gmit.socializer.services.chat.log.LogFormatter;
import ie.gmit.socializer.services.chat.protocol.IdentificationProtocol;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
    private final ConcurrentHashMap<UUID, List<WebSocket>> userConnectionMap;
    private final ConcurrentHashMap<WebSocket, UUID> connectionUserMap;
    private final DataParseHelper dataParseHelper = DataParseHelper.getInstance();
    
    public CryptoSocketServer(InetSocketAddress address){
        super(address);
        this.protocol = new IdentificationProtocol();
        IS_DEBUG = false;
        IS_VERBOSE = false;
        userConnectionMap = new ConcurrentHashMap<>();
        connectionUserMap = new ConcurrentHashMap<>();
    }
    
    public CryptoSocketServer(InetSocketAddress address, boolean isDebug, boolean isVerbose) {
        super(address);
        this.protocol = new IdentificationProtocol();
        IS_DEBUG = isDebug;
        IS_VERBOSE = isVerbose;
        if(IS_DEBUG)
            WebSocketImpl.DEBUG = true;
        
        userConnectionMap = new ConcurrentHashMap<>();
        connectionUserMap = new ConcurrentHashMap<>();
    }
    
    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {
        UUID userId = protocol.identifyUser(ch.getResourceDescriptor().replace("/", ""));
        if(userId != null){
            addMapConnection(userId, ws);
            ws.send(protocol.getConnectionWelcomMessage(""));
        }else{
            ws.send(protocol.getConnectionErrorMessage("Invalid resource used, use / + auth token and try again"));
            ws.close();
            Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "Invalid connection: " +  ws.getRemoteSocketAddress().getAddress().getHostAddress()));
        }
        
        if(IS_DEBUG || IS_VERBOSE)
            Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "New Connection: " +  ws.getRemoteSocketAddress().getAddress().getHostAddress()));
    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        removeConnection(ws);
        if(IS_DEBUG || IS_VERBOSE)
            Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "Connection closed: " +  ws.getRemoteSocketAddress().getAddress().getHostAddress()));
        
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        //start the custom protocol
        String response = protocol.routeSocketMessage(message);
        ws.send(response);
    }

    @Override
    public void onError(WebSocket ws, Exception excptn) {
        Logger.getLogger(CryptoSocketServer.class.getName()).log(Level.INFO, LogFormatter.addLineMessages(false, "Connection error: " +  ws.getRemoteSocketAddress()  + ":" + excptn));
        if(!IS_DEBUG)
            ws.close();
    }
    
    /**
     * Add connection to userConnectionMap and connectionUserMap
     * @param userUUID
     * @param instanceConnection 
     */
    protected void addMapConnection(UUID userUUID, WebSocket instanceConnection){
        List<WebSocket> connections;
        if(userConnectionMap.containsKey(userUUID)){
            connections = userConnectionMap.get(userUUID);
        }else{
            System.out.println("Added 1");
            connections = new ArrayList<>();
        }
        connections.add(instanceConnection);
        userConnectionMap.put(userUUID, connections);
        
        connectionUserMap.put(instanceConnection, userUUID);
    }
    
    /**
     * Remove connection from userConnectionMap and connectionUserMap
     * @param instanceConnection 
     */
    protected void removeMapConnection(WebSocket instanceConnection){
        if(connectionUserMap.containsKey(instanceConnection)){
            System.out.println("Removed 1");
            UUID userUUID = connectionUserMap.get(instanceConnection);
            if(userConnectionMap.containsKey(userUUID)){
                System.out.println("Removed 2");
                List<WebSocket> connections = userConnectionMap.get(userUUID);
                connections.remove(instanceConnection);//O(n) but not to likely to have 10 connections per user
                if(connections.size() > 0){
                    userConnectionMap.put(userUUID, connections);
                }else{
                    userConnectionMap.remove(userUUID);
                }
            }

            connectionUserMap.remove(instanceConnection);
        }
    }
}
