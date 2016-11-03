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
 * @description DataParseHelper - Short description
 * @package ie.gmit.socializer.services.chat.model
 */
package ie.gmit.socializer.services.chat.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.gmit.socializer.services.chat.protocol.ErrorMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataParseHelper {
    private final ObjectMapper objectMapper;
    private static DataParseHelper instance;

    private DataParseHelper() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//Avoid generating null values
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Remove the unknown fields
    }
    
    /**
     * Get singleton instance of data parser
     * 
     * @return initialized Object 
     */
    public static DataParseHelper getInstance(){
        if(instance == null){
            instance = new DataParseHelper();
        }
        
        return instance;
    }
    
    /**
     * Convert String uuids to UUID objects
     * 
     * @param strUuidList - list of string uuids
     * @return populated list with UUID objects
     */
    public List<UUID> convertStringListToUUIDList(List<String> strUuidList){
        List<UUID> user_uuids = new ArrayList<>();
        for(String sUuid : strUuidList){
            UUID user_uuid = convertStringToUUID(sUuid);
            if(null != user_uuid)
                user_uuids.add(user_uuid);
            else
                return null;
        }
        
        return user_uuids;
    }
    
    /**
     * Convert string uuid to UUID object
     * 
     * @param token - string representation of UUID
     * @return UUID or null
     */
    public UUID convertStringToUUID(String token){
        try{
            return UUID.fromString(token);
        }catch(Exception ex){
            //Invalid format
            Logger.getLogger(DataParseHelper.class.getName()).log(Level.SEVERE, "Could not parse uuid - convertStringToUUID", ex);
        }
        
        return null;
    }
    
    /**
     * Try convert socket message json string to object
     * 
     * @param <T> - a SocketMessage object to serialize
     * @param jsonMessage - 
     * @param type
     * @return 
     */
    public <T extends Object> T tryParseJsonString(final String jsonMessage, Class<T> type){
        try {
            return objectMapper.readValue(jsonMessage.getBytes("UTF-8"), type);
        } catch (IOException ex) {
            Logger.getLogger(DataParseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Try convert object to json string
     * 
     * @param sm
     * @return 
     */
    public String convertObjectToJsonString(Object sm){
        try {
            return objectMapper.writeValueAsString(sm);
        } catch (JsonProcessingException ex) {
            //This should not really happen because of strict use
            Logger.getLogger(DataParseHelper.class.getName()).log(Level.SEVERE, "Could not convert protocol message - DataParseHelper", ex);
        }
        
        return "";
    }
}
