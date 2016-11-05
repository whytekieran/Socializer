/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.gmit.socializer.SocializerAPI.models;

/**
 *
 * @author ciaran
 */
public class TimelinePagination {
    
    private String user_uuid;
    private int currentTimelineEnd;
    
    public TimelinePagination(String user_uuid, int currentTimelineEnd){
       
        this.user_uuid = user_uuid;
        this.currentTimelineEnd = currentTimelineEnd;
    }
    
    /**
     * @return the currentTimelineEnd
     */
    public int getCurrentTimelineEnd() {
        return currentTimelineEnd;
    }

    /**
     * @param currentTimelineEnd the currentTimelineEnd to set
     */
    public void setCurrentTimelineEnd(int currentTimelineEnd) {
        this.currentTimelineEnd = currentTimelineEnd;
    }

    /**
     * @return the user_uuid
     */
    public String getUser_uuid() {
        return user_uuid;
    }

    /**
     * @param user_uuid the user_uuid to set
     */
    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }
}
