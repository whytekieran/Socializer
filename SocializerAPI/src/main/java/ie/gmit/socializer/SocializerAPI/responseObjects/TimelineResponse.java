/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.gmit.socializer.SocializerAPI.responseObjects;

import ie.gmit.socializer.SocializerAPI.models.Timeline;
import java.util.List;

/**
 *
 * @author ciaran
 */
public class TimelineResponse extends Response {
    
    private List<Timeline> timelineList;
    private int startRecord;
    private int endRecord;
    
    public TimelineResponse(String success, List<Timeline> timelineList, int startRecord, int endRecord){
        
        super.success = success;
        this.timelineList = timelineList;
        this.startRecord = startRecord;
        this.endRecord = endRecord;
    }

    /**
     * @return the timelineList
     */
    public List<Timeline> getTimelineList() {
        return timelineList;
    }

    /**
     * @param timelineList the timelineList to set
     */
    public void setTimelineList(List<Timeline> timelineList) {
        this.timelineList = timelineList;
    }

    /**
     * @return the startRecord
     */
    public int getStartRecord() {
        return startRecord;
    }

    /**
     * @param startRecord the startRecord to set
     */
    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    /**
     * @return the endRecord
     */
    public int getEndRecord() {
        return endRecord;
    }

    /**
     * @param endRecord the endRecord to set
     */
    public void setEndRecord(int endRecord) {
        this.endRecord = endRecord;
    }
}
