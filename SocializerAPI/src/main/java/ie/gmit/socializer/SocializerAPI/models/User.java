package ie.gmit.socializer.SocializerAPI.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private UUID uuid;
	@PartitionKey(1)
	private String email;
	private String password_hash;
	private String hash_secret;
	private String firstname;
	private String surname;
	private String slug;
	private Date dob;
	private String phone_number;
	private String address1;
	private String address2;
	private String address_city;
	private String address_county;
	private String address_country;
	private String profilePicUuid;
	private String backgroundPicUuid;
	private List<String> workplaces;
	private List<String> professionalSkills;
	private List<String> livedIn;
	private List<String> connections;
	@ClusteringColumn
	private Date created; 
	private Date updated;	
	private DateFormat dateFormat;	
	
	//Empty constructor needed if we are returning JSON or XML responses
	//Important for XML or JSON conversion
	//Jersey framework needs to be able to create a new instance of the class hence the empty constructor
	public User()
	{
		
	}
		
	//Constructor new instance of a user - can overload constructor later for possible user update
	public User(String email, String firstname, String surname, String dob, String phone_number, 
			    String address1, String address2, String address_city, 
			    String address_county, String address_country) {
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		this.uuid = UUIDs.random();
		this.email = email;
		this.firstname = firstname;
		this.surname = surname;
		this.address1 = address1;
		this.address2 = address2;
		this.address_city = address_city;
		this.address_county = address_county;
		this.address_country = address_country;
		this.phone_number = phone_number;
		this.created = new Date();
		this.updated = new Date();
		try {
			this.dob = dateFormat.parse(dob);
		} catch (ParseException e) {
			System.out.println("Problem parsing date");
		}
	}
	
	//Getters and Setters
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

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstname) {
		this.firstname = firstname;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddressCity() {
		return address_city;
	}

	public void setAddressCity(String addressCity) {
		this.address_city = addressCity;
	}

	public String getAddressCounty() {
		return address_county;
	}

	public void setAddressCounty(String addressCounty) {
		this.address_county = addressCounty;
	}

	public String getAddressCountry() {
		return address_country;
	}

	public void setAddressCountry(String addressCountry) {
		this.address_country = addressCountry;
	}

	public String getProfilePicUuid() {
		return profilePicUuid;
	}

	public void setProfilePicUuid(String profilePicUuid) {
		this.profilePicUuid = profilePicUuid;
	}

	public String getBackgroundPicUuid() {
		return backgroundPicUuid;
	}

	public void setBackgroundPicUuid(String backgroundPicUuid) {
		this.backgroundPicUuid = backgroundPicUuid;
	}

	public List<String> getWorkplaces() {
		return workplaces;
	}

	public void setWorkplaces(List<String> workplaces) {
		this.workplaces = workplaces;
	}

	public List<String> getProfessionalSkills() {
		return professionalSkills;
	}

	public void setProfessionalSkills(List<String> professionalSkills) {
		this.professionalSkills = professionalSkills;
	}

	public List<String> getLivedIn() {
		return livedIn;
	}

	public void setLivedIn(List<String> livedIn) {
		this.livedIn = livedIn;
	}

	public List<String> getConnections() {
		return connections;
	}

	public void setConnections(List<String> connections) {
		this.connections = connections;
	}
}
