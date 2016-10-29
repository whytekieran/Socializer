/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.gmit.socializer.SocializerAPI.responseObjects;

/**
 *
 * @author ciaran
 */
public class NewConnectionResponse extends Response {
    private String newFriendID;
    private String currentUserID;
    
    //Empty constructor for jersey framework
    public NewConnectionResponse(){
    
    }
    
    //Constructor
    public NewConnectionResponse(String success, String newFriendID, String currentUserID){
        super.success = success;
        this.currentUserID = currentUserID;
        this.newFriendID = newFriendID;
    }

    //Getters and Setters
    public String getNewFriendID() {
        return newFriendID;
    }

    public void setNewFriendID(String newFriendID) {
        this.newFriendID = newFriendID;
    }

    
    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }
}
