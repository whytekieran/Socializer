package ie.gmit.socializer.SocializerAPI.models;

import com.datastax.driver.core.utils.UUIDs;

public class Timeline {
		
	//Instance variables
	private UUIDs user_uuid;
	private UUIDs post_uuid;
	private UUIDs parent_uuid;
	private UUIDs album_uuid;
	//content
	//content type
	private int likeCount;
	private int unlikeCount;
	//visibility
	//private long created;    //hold long time stamps when time line is added to
	//private long updated;	   //used to hold value when time line updated
	
	//Empty constructor need for the jersey framework.
	public Timeline()
	{
		
	}
}
