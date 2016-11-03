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
import ie.gmit.socializer.services.chat.log.LogFormatter;
import java.io.File;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.apache.commons.cli.CommandLine;
import org.java_websocket.server.WebSocketServer;

public class ServerConfigurator {

    protected static final String DEFAULT_LOG_PATH = "/logs/";
    protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    protected static String usedLogPath = "n/a";
    /**
     * Route the command line parameters to server objects
     *
     * @todo: Implement benchmark server
     * @todo: Implement keyring management and generation for wss support
     *
     * @param cli - Initialized CommandLine object with current parameters
     * @return initialized server or null
     */
    protected static WebSocketServer configureServer(final CommandLine cli) {
        configureServerLogging(cli);
        
        if (cli.hasOption('b')) {
            //prepare benchmark server configuration
            //run benchmark
            //Exit after benchmark
            return null;
        } else if (cli.hasOption('i') && tryParseHostname(cli.getOptionValue('i'))
                && cli.hasOption('p') && tryParsePort(cli.getOptionValue('p'))) {
            //Check protocol params
            if (cli.hasOption('s')
                    && (cli.getOptionValue('s').equalsIgnoreCase("ws") || cli.getOptionValue('s').equalsIgnoreCase("wss"))) {
                //start with specified socket type
                //@todo: this has to implement server side keyring management (use keytool)
            } else {
                //normal run
            }
            
            System.out.println(LogFormatter.applicationStart(
                                cli.getOptionValue('i'), cli.getOptionValue('p'), usedLogPath));
            
            return new CryptoSocketServer(
                    new InetSocketAddress(cli.getOptionValue('i'), Integer.parseInt(cli.getOptionValue('p'))), cli.hasOption('d'), cli.hasOption('v'));
        }

        return null;
    }
    
    /**
     * Configure the logging for current execution
     * 
     * @param cli - the parsed command line parameters 
     */
    protected static void configureServerLogging(final CommandLine cli){
        String logPath = cli.hasOption('l') ? cli.getOptionValue('l') : "";
        setupLoging(logPath, cli.hasOption('v'), cli.hasOption('d'));
    }

    /**
     * Setup logger for logging on default
     *
     * @param logPath - valid path string or ""
     * @param isVerbose - if true output to console (screen)
     * @param isDebug - dump all info to the screen, nothing will be loggen in file
     */
    protected static void setupLoging(String logPath, boolean isVerbose, boolean isDebug) {
        if (!isDebug) {//save to file
            if (!isVerbose) {//don't print on screen
                Logger rootLogger = Logger.getLogger("");
                Handler[] handlers = rootLogger.getHandlers();

                for (Handler h : handlers) {
                    //Remove the general log handler
                    if (h instanceof ConsoleHandler) {
                        rootLogger.removeHandler(h);
                        break;
                    }
                }
            }
            
            //Format and handle file based loggin
            SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
            usedLogPath = validatePath(logPath) ? logPath : getCreateDefaultLogDir();

            FileHandler fileHandler;
            try {
                fileHandler = new FileHandler(usedLogPath
                        + format.format(Calendar.getInstance().getTime()) + ".log");
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LOGGER.setLevel(Level.INFO);
    }

    /**
     * Check and create default log directory if not exists
     *
     * @throws RuntimeException - if the default directory does not exist loggin
     * won't work, should create directory
     * @return String - default log directory
     */
    protected static String getCreateDefaultLogDir() {
        try {
            File logDir = new File(System.getProperty("user.home") + DEFAULT_LOG_PATH);
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            return System.getProperty("user.home") + DEFAULT_LOG_PATH;
        } catch (Exception e) {
            throw new RuntimeException("Could not create default log directory at: " + System.getProperty("user.home") + DEFAULT_LOG_PATH, e);
        }
    }

    /**
     * Validate if path is directory
     *
     * @param path
     * @return
     */
    protected static boolean validatePath(String path) {
        try {
            File f = new File(path);
            if (f.isDirectory()) {
                return true;
            }
        } catch (Exception e) {
            //Nothing to do will falback to default
        }

        return false;
    }

    /**
     * Try parse port with validation
     *
     * @param number
     * @return
     */
    protected static boolean tryParsePort(String number) {
        try {
            int num = Integer.parseInt(number);
            if (num == 80 || num == 443
                    || (num > 1000 && num < 65535)) {
                return true;
            }
        } catch (Exception e) {
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
    protected static boolean tryParseHostname(String hostname) {
        try {
            //Check ip address
            return InetAddresses.isInetAddress(hostname);
        } catch (Exception e) {
            //Not inet address 
        }

        return false;

    }
}
