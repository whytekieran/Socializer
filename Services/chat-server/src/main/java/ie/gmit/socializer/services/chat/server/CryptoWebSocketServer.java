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
 * @description CryptoWebSocketServer - Abstraction to support hashCode and equals
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class CryptoWebSocketServer  extends WebSocketServer{

    public CryptoWebSocketServer() throws UnknownHostException {
    }

    public CryptoWebSocketServer(InetSocketAddress isa) {
        super(isa);
    }

    public CryptoWebSocketServer(InetSocketAddress isa, int i) {
        super(isa, i);
    }

    public CryptoWebSocketServer(InetSocketAddress isa, List<Draft> list) {
        super(isa, list);
    }

    public CryptoWebSocketServer(InetSocketAddress isa, int i, List<Draft> list) {
        super(isa, i, list);
    }

    public CryptoWebSocketServer(InetSocketAddress isa, int i, List<Draft> list, Collection<WebSocket> clctn) {
        super(isa, i, list, clctn);
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMessage(WebSocket ws, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onError(WebSocket ws, Exception excptn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
