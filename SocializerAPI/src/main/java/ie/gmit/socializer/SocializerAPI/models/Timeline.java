package ie.gmit.socializer.SocializerAPI.models;

import java.util.Date;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

//User class written by Ciaran Whyte
//Defines this as a Cassandra "Table"
@Table(keyspace = "app_user_data", name = "timeline",
caseSensitiveKeyspace = false,
caseSensitiveTable = false)
public class Timeline {
		
	//Instance variables
	@PartitionKey(0)
	private UUID post_uuid;
	private UUID user_uuid;
	private UUID album_uuid;
	private UUID parrent_uuid;
	private String content_data;
	private String content_type;
	private int like_count;
	private int unlike_count;
	private String visibility;
	@ClusteringColumn
	private Date created;
	private Date updated;
	
	//Empty constructor needed for the jersey framework.
	public Timeline()
	{
		
	}

	//Getters and Setters
	public UUID getPost_uuid() {
		return post_uuid;
	}

	public void setPost_uuid(UUID post_uuid) {
		this.post_uuid = post_uuid;
	}

	public UUID getUser_uuid() {
		return user_uuid;
	}

	public void setUser_uuid(UUID user_uuid) {
		this.user_uuid = user_uuid;
	}

	public UUID getAlbum_uuid() {
		return album_uuid;
	}

	public void setAlbum_uuid(UUID album_uuid) {
		this.album_uuid = album_uuid;
	}

	public UUID getParrent_uuid() {
		return parrent_uuid;
	}

	public void setParrent_uuid(UUID parrent_uuid) {
		this.parrent_uuid = parrent_uuid;
	}

	public String getContent_data() {
		return content_data;
	}

	public void setContent_data(String content_data) {
		this.content_data = content_data;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public int getUnlike_count() {
		return unlike_count;
	}

	public void setUnlike_count(int unlike_count) {
		this.unlike_count = unlike_count;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
