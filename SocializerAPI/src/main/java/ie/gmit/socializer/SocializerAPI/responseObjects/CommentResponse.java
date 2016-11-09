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
public class CommentResponse extends Response {
    
    private List<Timeline> commentList;
    
    public CommentResponse(String success, List<Timeline> commentList){
        super.success = success;
        this.commentList = commentList;
    }

    /**
     * @return the commentList
     */
    public List<Timeline> getCommentList() {
        return commentList;
    }

    /**
     * @param commentList the commentList to set
     */
    public void setCommentList(List<Timeline> commentList) {
        this.commentList = commentList;
    }
    
    
}
