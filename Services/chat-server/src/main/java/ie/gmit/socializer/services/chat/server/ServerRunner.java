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
 * @description ServerRunner - Chat server runner class - handles the service
 * @package ie.gmit.socializer.services.chat.server
 */
package ie.gmit.socializer.services.chat.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class ServerRunner extends ServerConfigurator {
    
    
    protected static boolean isVerbose = false;
    protected static Options availableOptions;
    
    /**
     * Cli entry point 
     * @param args - cli parameters
     */
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        
        Object o = tryParseCliInput(args);
        if(o != null)
            //add the server execution here
        
        if (isVerbose) {
            System.out.printf("\n Process finished in %dms\n\n", System.currentTimeMillis() - startTime);
        }
    }
    
    protected static Object tryParseCliInput(String[] args){
        buildCliOptions();
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(availableOptions, args);
            isVerbose = line.hasOption('v');

            return routeParams(line);
            
        } catch (org.apache.commons.cli.ParseException ex) {
            printCliHelp();
            //@todo: override the logger if not in debug mode
            Logger.getLogger(ServerConfigurator.class.getName()).log(Level.SEVERE, "Server configurator commandline pars error - tryParseCliInput", ex);
        }
        //Force quit as the server could not been initialized
        return null;
    }
    
    /**
     * Build the command-line options for service
     */
    protected static void buildCliOptions() {
        availableOptions = new Options();
        availableOptions.addOption("b", "banchmark", false, "Run a banchmark test on implementation simulating client");
        availableOptions.addOption("d", "debug", false, "Run the service in debug mode, will output all information on screen instead log file");
        availableOptions.addOption("h", "help", false, "Show help options (this menu)");
        availableOptions.addOption("i", "ip-address", true, "The service ip address or hostname");
        availableOptions.addOption("p", "port", true, "The port the service should listen on");
        availableOptions.addOption("s", "socket-type", true, "The type of socket to user (WS or WSS)");
        availableOptions.addOption("v", "verbose", false, "Show details of the process, outputs connection information");
    }

    /**
     * Print the cli help with options and exit
     */
    protected static void printCliHelp() {
        String helpHeader = "Socializer chat server cli options\n=======================================";
        String helpFooter = new StringBuilder("\nExamples:\n=======================================").append("\nRun service: -i 127.0.0.1 -p 81 -s ws").append("\nRun in debug mode service: -i 127.0.0.1 -p 81 -s ws -d").append("\nRun in verbose mode service: -i 127.0.0.1 -p 81 -s ws -v").append("\nRun benchmark test: -i 127.0.0.1 -p 81 -s ws -b").append("\n=======================================\n\n").toString();
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Socializer chat server", helpHeader, availableOptions, helpFooter, true);
    }
    
}
