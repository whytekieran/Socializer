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
 * @description ServerRunnerTest - Test cases for server runner
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.server;

import org.apache.commons.cli.CommandLine;
import org.java_websocket.server.WebSocketServer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

//Required to get cli param test
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerRunnerTest {
    
    public ServerRunnerTest() {
    
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of tryParseCliInput method, of class ServerRunner.
     */
    @Test
    public void test_001TryParseCliInput() {
        System.out.println("tryParseCliInput - valid input");
        String[] args = new String[]{"-i", "127.0.0.1", "-p", "80", "-s", "wss"};
        CommandLine result = ServerRunner.tryParseCliInput(args);
        
        assertEquals(true, null != result && result.getOptionValue('s').equalsIgnoreCase("wss")); 
    }
    
    /**
     * Test of tryParseCliInput method, of class ServerRunner.
     */
    @Test
    public void test_002TryParseCliInputTestFalse() {
        System.out.println("tryParseCliInput - invalid input");
        String[] args = new String[]{"-i", "127.0.0.1", "--this-does-not-exist", "80", "-s", "wss"};
        CommandLine result = ServerRunner.tryParseCliInput(args);
        
        assertEquals(true, null == result); 
    }

    /**
     * Test of buildCliOptions method, of class ServerRunner.
     */
    @Test
    public void test_003BuildCliOptions() {
        System.out.println("buildCliOptions");
        Exception exc = null;
        try{
            ServerRunner.buildCliOptions();
        }catch(Exception e){
            exc = e;
        }
        
        assertEquals(true, null == exc && null != ServerRunner.availableOptions);
    }
    
    /**
     * Test of tryParsePort method, of class ServerRunner.
     */
    @Test
    public void test_004TryParsePort() {
        System.out.println("tryParsePort");
        boolean result = ServerConfigurator.tryParsePort("80");
        
        assertEquals(true, result);
    }
    
    /**
     * Test inverse of tryParsePort method, of class ServerRunner.
     */
    @Test
    public void test_005TryParsePort() {
        System.out.println("tryParsePort inverse");
        boolean result = ServerConfigurator.tryParsePort("82");
        
        assertEquals(false, result);
    }
    
    /**
     * Test of tryParseHostname method, of class ServerRunner.
     */
    @Test
    public void test_006TryParseHostname() {
        System.out.println("tryParseHostname");
        boolean result = ServerConfigurator.tryParseHostname("127.0.0.1");
        
        assertEquals(true, result);
    }
    
    /**
     * Test inverse of tryParseHostname method, of class ServerRunner.
     */
    @Test
    public void test_007TryParseHostname() {
        System.out.println("tryParseHostname inverse");
        boolean result = ServerConfigurator.tryParseHostname("127.0.0");
        
        assertEquals(false, result);
    }
    
    /**
     * Test of configureServer method, of class ServerRunner.
     */
    @Test
    public void test_008ConfigureServer() {
        System.out.println("ConfigureServer");
        String[] args = new String[]{"-i", "127.0.0.1", "-p", "80", "-s", "wss"};
        CommandLine result = ServerRunner.tryParseCliInput(args);
        WebSocketServer server = ServerConfigurator.configureServer(result);
        
        assertEquals(true, null != server);
    }
    
    /**
     * Test inverse of configureServer method, of class ServerRunner.
     */
    @Test
    public void test_009ConfigureServer() {
        System.out.println("ConfigureServer");
        String[] args = new String[]{"-i", "127.0.0.1"};
        CommandLine result = ServerRunner.tryParseCliInput(args);
        WebSocketServer server = ServerConfigurator.configureServer(result);
        
        assertEquals(true, null == server);
    }
}
