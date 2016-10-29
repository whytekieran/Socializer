package ie.gmit.socializer.SocializerAPI.models;

import java.util.UUID;

/**
 *
 * @author ciaran
 */
public class AddUserConnection {
    
    private String currentUserId;
    private String requestedFriendId;
    
    //Empty Constructor for jersey framework
    public AddUserConnection(){
        
    }
    
    //Constructor
    public AddUserConnection(String currentUserId, String requestedFriendId){
        
        this.currentUserId = currentUserId;
        this.requestedFriendId = requestedFriendId;    
    }

    //Getters and Setters
    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getRequestedFriendId() {
        return requestedFriendId;
    }

    public void setRequestedFriendId(String requestedFriendId) {
        this.requestedFriendId = requestedFriendId;
    }
}
