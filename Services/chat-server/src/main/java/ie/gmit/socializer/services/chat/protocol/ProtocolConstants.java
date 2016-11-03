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
 * @description ProtocolConstants - Shared constant exchange class
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

public class ProtocolConstants {
    public static final int VERSION = 100;
    public static final int AUTHENTICATION_MESSAGE_MAX = 9; // 0-9
    public static final int ACTION_MESSAGE_MAX = 19; // 10-19
    public static final int SESSION_MESSAGE_MAX = 29; // 20-29
    public static final String APP_DATA_KS = "app_data";
    public static final String APP_USER_DATA_KS = "app_user_data";
}
