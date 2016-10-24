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
 * @description FormattedWriter - Format output 
 * @package ie.gmit.socializer.services.chat.log
 */
package ie.gmit.socializer.services.chat.log;

public class LogFormatter {
    private static final String MESSAGE_DIVIDER_SINGLE = "-------------------------------";
    private static final String MESSAGE_DIVIDER_DOUBLE = "===============================";
    
    /**
     * Format a header for application startup
     * 
     * @param hostname - as per supplied hostname
     * @param port - as per supplied port
     * @param logPath - the log path used
     * @return formated string for logging
     */
    public static String applicationStart(String hostname, String port, String logPath){
        return addLineMessages(false, MESSAGE_DIVIDER_DOUBLE) 
                + addLineMessages(true, "Websocket server start", MESSAGE_DIVIDER_SINGLE, "Hostname: " + hostname, "Port: " + port, "Log directory: " + logPath, MESSAGE_DIVIDER_SINGLE)
                + addLineMessages(false, MESSAGE_DIVIDER_DOUBLE);
    }
    
    /**
     * Add line message to string for output on separate lines
     * 
     * @param isTabbed - put tab in front of each message
     * @param message - the initial message
     * @param messages - any other messages 
     * @return the formatted string
     */
    public static String addLineMessages(boolean isTabbed, String message, String... messages){
        String result = "\n" + (isTabbed ? "\t" + message : message);
        for(String msg : messages)
            result += "\n" + (isTabbed ? "\t" + msg : msg);
        
        return result + "\n";
    } 
}
