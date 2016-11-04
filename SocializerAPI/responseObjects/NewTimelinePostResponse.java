/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.gmit.socializer.SocializerAPI.responseObjects;

import ie.gmit.socializer.SocializerAPI.models.Timeline;

/**
 *
 * @author ciaran
 */
public class NewTimelinePostResponse extends Response {
    
    private Timeline newPost;
    
    public NewTimelinePostResponse(String success, Timeline newPost){
        super.success = success;
        this.newPost = newPost;
    }

    /**
     * @return the newPost
     */
    public Timeline getNewPost() {
        return newPost;
    }

    /**
     * @param newPost the newPost to set
     */
    public void setNewPost(Timeline newPost) {
        this.newPost = newPost;
    }
    
}
