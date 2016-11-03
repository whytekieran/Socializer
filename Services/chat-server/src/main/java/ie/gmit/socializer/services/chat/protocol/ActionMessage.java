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
 * @description ActionMessage - Handles masseges leading to actions like create session, open session etc.
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

import java.util.List;

public class ActionMessage extends SocketMessage{
    /**
     * Action breakdown:
     * 200-299 >> create
     *      210 >> create-message-session
     *      220 >> create-message-session-key
     * 300-399 >> update
     * 
     */
    public static final int CREATE_MESSAGE_SESSION = 210;
    
    private int action;// 200-299 >> create, consider using string
    private List<String> action_values;
    private String result;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<String> getAction_values() {
        return action_values;
    }

    public void setAction_values(List<String> action_values) {
        this.action_values = action_values;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
