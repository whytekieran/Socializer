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
 * @description ErrorMessage - Message object for serializable error messages
 * @package ie.gmit.socializer.services.chat.protocol
 */
package ie.gmit.socializer.services.chat.protocol;

public class ErrorMessage extends SocketMessage{
    private String src;//The source module of the error
    private int code;//The error code for this error - based on http status codes
    private String desc;//The human readable error message

    public ErrorMessage(String src, int code, String desc, int state, int version) {
        super.setState(state);
        super.setVersion(version);
        this.src = src;
        this.code = code;
        this.desc = desc;
    }

    
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
