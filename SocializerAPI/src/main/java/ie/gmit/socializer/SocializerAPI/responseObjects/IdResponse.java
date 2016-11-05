package ie.gmit.socializer.SocializerAPI.responseObjects;

import java.util.UUID;

public class IdResponse extends Response {
	
	//Fields must be public for object mapping
	public UUID user_uuid;
	
	//Constructor
	public IdResponse(String success, UUID user_uuid){
		
		super.success = success;
		this.user_uuid = user_uuid;
	}
	
	//Getters and Setters
	public UUID getUser_uuid() {
		return user_uuid;
	}

	public void setUser_uuid(UUID user_uuid) {
		this.user_uuid = user_uuid;
	}
}
