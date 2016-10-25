package ie.gmit.socializer.SocializerAPI.models;

import java.util.*;

//import com.datastax.driver.core.TimestampGenerator;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

//User class written by Ciaran Whyte
//Defines this as a Cassandra "Table"
@Table(keyspace = "app_user_data", name = "user",
caseSensitiveKeyspace = false,
caseSensitiveTable = false)
public class User {

	@PartitionKey(0)
	//Instance variables
	private UUID user_uuid;
	@PartitionKey(1)
	private String email;
	private String password_hash;
	private String hash_secret;
	private String firstname;
	private String surname;
	@ClusteringColumn
	private String slug;
	private String dob;
	private String phone_number;
	private String address_1;
	private String address_2;
	private String address_3;
	private String address_city;
	private String address_county;
	private String address_country;
	private UUID profile_pic_uuid;
	private UUID background_pic_uuid;
	private List<String> workplaces;
	private List<String> professional_skills;
	private List<String> lived_in;
	private List<UUID> connections;
	private Date created; 
	private Date updated;		
	
	//Empty constructor needed if we are returning JSON or XML responses
	//Important for XML or JSON conversion
	//Jersey framework needs to be able to create a new instance of the class hence the empty constructor
	public User()
	{
		
	}
		
	//Constructor new instance of a user - can overload constructor later for possible user update
	/*public User(String email, String firstname, String surname, String dob, String phone_number, 
			    String address1, String address2, String address3, String address_city, 
			    String address_county, String address_country) {
		
		this.user_uuid = UUIDs.random();
		this.email = email;
		this.firstname = firstname;
		this.surname = surname;
		this.address_1 = address1;
		this.address_2 = address2;
		this.address_3 = address3;
		this.address_city = address_city;
		this.address_county = address_county;
		this.address_country = address_country;
		this.phone_number = phone_number;
		this.created = new Date();
		this.updated = new Date();
		this.dob = dob;
	}*/

	//Getters and Setters
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getAddress_city() {
		return address_city;
	}

	public void setAddress_city(String address_city) {
		this.address_city = address_city;
	}

	public String getAddress_county() {
		return address_county;
	}

	public void setAddress_county(String address_county) {
		this.address_county = address_county;
	}

	public String getAddress_country() {
		return address_country;
	}

	public void setAddress_country(String address_country) {
		this.address_country = address_country;
	}
	
	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getAddress_3() {
		return address_3;
	}

	public void setAddress_3(String address_3) {
		this.address_3 = address_3;
	}
	
	public UUID getProfile_pic_uuid() {
		return profile_pic_uuid;
	}

	public void setProfile_pic_uuid(UUID profile_pic_uuid) {
		this.profile_pic_uuid = profile_pic_uuid;
	}

	public UUID getBackground_pic_uuid() {
		return background_pic_uuid;
	}

	public void setBackground_pic_uuid(UUID background_pic_uuid) {
		this.background_pic_uuid = background_pic_uuid;
	}

	public UUID getUser_uuid() {
		return user_uuid;
	}

	public void setUser_uuid(UUID user_uuid) {
		this.user_uuid = user_uuid;
	}


	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getProfessional_skills() {
		return professional_skills;
	}

	public void setProfessional_skills(List<String> professional_skills) {
		this.professional_skills = professional_skills;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getHash_secret() {
		return hash_secret;
	}

	public void setHash_secret(String hash_secret) {
		this.hash_secret = hash_secret;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public List<String> getWorkplaces() {
		return workplaces;
	}

	public List<UUID> getConnections() {
		return connections;
	}

	public void setConnections(List<UUID> connections) {
		this.connections = connections;
	}

	public void setWorkplaces(List<String> workplaces) {
		this.workplaces = workplaces;
	}

	public List<String> getLived_in() {
		return lived_in;
	}

	public void setLived_in(List<String> lived_in) {
		this.lived_in = lived_in;
	}
}
