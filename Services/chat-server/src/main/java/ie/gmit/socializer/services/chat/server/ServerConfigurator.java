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

import org.apache.commons.cli.CommandLine;

public class ServerConfigurator {
    
    /**
     * Route the command line parameters to server objects 
     *
     * @param cli - Initialized CommandLine object with current parameters
     */
    protected static Object routeParams(final CommandLine cli) {
        if (cli.hasOption('b')) {
            //prepare banchmark server configuration
            return null;
        } else {
            return null;
        }
    }
}
